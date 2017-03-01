package com.medtracker.Fragments.Alarm;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medtracker.Fragments.TimePickerFragment;
import com.medtracker.Models.Alarm;
import com.medtracker.Models.AlarmManager;
import com.medtracker.Adapters.AlarmAdapter;
import com.medtracker.Utilities.LogTag;
import com.medtracker.Utilities.NotificationManager;
import com.medtracker.Utilities.RC;
import com.medtracker.medtracker.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmListFragment extends Fragment implements AlarmAdapter.AlarmAdapterCallback {
    //testing and tracking variables
    private static final String TAG = LogTag.alarmMedicationFragment;
    private final int RC_TIME_PICKER_ADD = RC.TIME_PICKER_ADD;
    private final int RC_TIME_PICKER_EDIT = RC.TIME_PICKER_EDIT;

    private DatabaseReference database;
    private String medicationKey;
    private String userUID;
    private int currentEdit;
    private int alarmCount;
    private ArrayList<Alarm> alarms = new ArrayList<>();
    private ArrayList<String> alarmKeys = new ArrayList<>();
    private AlarmAdapter adapter;
    private ListView listView;
    private int RCID;


    public AlarmListFragment() {}

    @Override //sets up ui and is used to get arguments pased to fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.alarmCount = args.getInt("maxCount");
        this.medicationKey = args.getString("medicationKey");
        // Inflate the UI
        return inflater.inflate(R.layout.fragment_alarm_medication, container, false);
    }

    @Override // This event is triggered soon after onCreateView().
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "loadedFragment"); //firebase setup
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUID = mFirebaseUser.getUid();
        Log.d(TAG, mFirebaseUser.getUid());

        database = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) getView().findViewById(R.id.listView);

        //button listener
        Button addAlarm = (Button) getView().findViewById(R.id.button_add_alarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "add alarm button clicked"); //firebase setup
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(AlarmListFragment.this, RC_TIME_PICKER_ADD);
                timePicker.show(getFragmentManager().beginTransaction(), "timePicker");
            }
        });

        Switch alarmSwitch = (Switch) getView().findViewById(R.id.alarm_switch);
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(alarmCount > 0) {
                    if (isChecked) {
                        Log.d(TAG, "Switch enabled");
                        enableAlarms();
                    } else {
                        Log.d(TAG, "Switch disabled");
                        disableAlarms();
                    }
                }
            }
        });

        final DatabaseReference databaseReference = database.child("system").child("RCNOT");
        databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RCID = dataSnapshot.getValue(Integer.class);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        //checks list has been populated and exists
        if(listView == null)
            Log.d(TAG, "ListView is null");
    }

    @Override
    public void onStart() {
        super.onStart();
        //sets up adapter and call back methods implemented from the adapter interface
        adapter = new AlarmAdapter(getActivity().getApplicationContext(),alarms, alarmCount);
        adapter.setCallback(this);
        listView.setAdapter(adapter);

        //one big listener for database events to enable realtime (needs further work
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Alarm alarm = dataSnapshot.getValue(Alarm.class);
                alarms.add(alarm);
                alarmKeys.add(dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Alarm changedAlarm = dataSnapshot.getValue(Alarm.class);
                String alarmKey = dataSnapshot.getKey();
                int index = alarmKeys.indexOf(alarmKey);

                if (index > -1) {
                    alarms.set(index, changedAlarm);
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + alarmKey);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String keyToRemove = dataSnapshot.getKey();
                int index = alarmKeys.indexOf(keyToRemove);

                if (index > -1){
                    alarms.remove(index);
                    alarmKeys.remove(index);
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                } else {
                    Log.d(TAG, "Index: " + index + " is an invalid index");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "MedicationsActivity:onCancelled", databaseError.toException());
            }
        }; database.child("alarms").child(userUID).child(medicationKey).
                addChildEventListener(childEventListener);
    }

    @Override //only really used for the time picker
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_TIME_PICKER_ADD: //if the result code is add
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    int hourOfDay = Integer.parseInt(bundle.getString("hourOfDay"));
                    int minute = Integer.parseInt(bundle.getString("minute"));
                    addAlarm(hourOfDay,minute);
                }
                break;
            case RC_TIME_PICKER_EDIT: //if the reseult code is edit
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    int hourOfDay = Integer.parseInt(bundle.getString("hourOfDay"));
                    int minute = Integer.parseInt(bundle.getString("minute"));
                    applyEdit(hourOfDay, minute);
                }
                break;
        }
    }

    //method to add an alarm to the database
    private void addAlarm(int hour, int minute) {
        //increment id input by one as this is a new alarm
        Alarm toAdd = alarmBuilder(hour,minute, alarms.size()+1,medicationKey);
        String alarmKey = toAdd.getMedication_key() + "_" + toAdd.getId();
        database.child("alarms").child(userUID).child(medicationKey).child(alarmKey).
                setValue(toAdd);
        RCID = RCID + 1;
        database.child("system").child("RCNOT").setValue(RCID);
        Log.d(TAG, alarmKey + " added to database");
        //make sure to update the manager
        updateAlarmManager(toAdd.getMedication_key(), "add");
    }

    @Override
    public void editAlarm(Alarm toEdit) {
        //interface method for editing, can't preform database read here as i need to use the
        //time picker fragment
        currentEdit = toEdit.getId();
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.setTargetFragment(AlarmListFragment.this, RC_TIME_PICKER_EDIT);
        timePicker.show(getFragmentManager().beginTransaction(), "timePicker");
    }

    @Override //interface to editing alarms
    public void deleteAlarm(Alarm toDelete) {
        Log.d(TAG, "AlarmKey to delete:" + toDelete.getMedication_key());
        String alarmKey = toDelete.getMedication_key() + "_" + toDelete.getId();

        database.child("alarms").child(userUID).child(toDelete.getMedication_key()).
                child(alarmKey).removeValue();
        updateAlarmManager(toDelete.getMedication_key(), "delete");
        Log.d(TAG, "Alarm deleted from the database");
    }

    //this method preforms the actual database edit, no need to edit alarmManager
    public void applyEdit(int hourOfDay, int minute) {
        Alarm toAdd = alarmBuilder(hourOfDay, minute, currentEdit, medicationKey);
        String alarmKey = toAdd.getMedication_key() + "_" + toAdd.getId();
        database.child("alarms").child(userUID).child(medicationKey).child(alarmKey).
                setValue(toAdd);
        Log.d(TAG, alarmKey + " edited");
    }

    //used to update the alarm manager
    private void updateAlarmManager(String medicationKey, String method){
        //reference to object location
        final String action = method;
        final DatabaseReference databaseReference = database.child("alarm_manager").child(userUID).
                child(medicationKey);

        databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                AlarmManager alarmManager = dataSnapshot.getValue(AlarmManager.class);
                int maxCount = alarmManager.getMax_count();
                int currentCount = alarmManager.getCurrent_count();

                switch(action) {
                    case "add":
                        if (maxCount < 1) {
                            alarmManager.setHas_alarm(true);
                        }
                        if(currentCount == 0) {
                            currentCount = 1;
                            alarmManager.setCurrent_count(currentCount);
                        }
                        maxCount = maxCount + 1;

                        alarmManager.setMax_count(maxCount);
                        break;
                    case "delete":
                        maxCount = maxCount - 1;
                        alarmManager.setMax_count(maxCount);
                        Log.d(TAG, "max count after set: " + alarmManager.getMax_count());
                        if(currentCount > maxCount) {
                            currentCount = currentCount - 1;
                            alarmManager.setCurrent_count(currentCount);
                        }
                        if (maxCount < 1) {
                            alarmManager.setHas_alarm(false);
                            alarmManager.setCurrent_alarm("none");
                        }
                        break;
                }
                databaseReference.setValue(alarmManager);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //used to build alarm objects
    private Alarm alarmBuilder(int hour, int minute, int id, String medicationKey){
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        Log.d(TAG, "currentHour: " + currentHour + ":" + hour);

        if (currentHour > hour)
            currentDay = currentDay + 1;

        return new Alarm(id, minute, hour, currentDay, currentMonth, currentYear, medicationKey, RCID);
    }

    //if the user enables the alarms for a medication
    private void enableAlarms() {
        final DatabaseReference databaseReference = database.child("alarm_manager").child(userUID).
                child(medicationKey);

        databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                AlarmManager alarmManager = dataSnapshot.getValue(AlarmManager.class);
                int currentCount = alarmManager.getCurrent_count();
                String current_alarm = alarmManager.getMedication_key() + "_"  + currentCount;

                alarmManager.setCurrent_alarm(current_alarm);
                NotificationManager.enableNextAlarm(alarms.get(currentCount-1),
                        getActivity().getApplicationContext());
                databaseReference.setValue(alarmManager);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void disableAlarms() {
        final DatabaseReference databaseReference = database.child("alarm_manager").child(userUID).
                child(medicationKey);

        databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                AlarmManager alarmManager = dataSnapshot.getValue(AlarmManager.class);
                String current_alarm = "none";
                alarmManager.setCurrent_alarm(current_alarm);
                databaseReference.setValue(alarmManager);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void updateAlarmCount() {
        final DatabaseReference databaseReference = database.child("alarm_manager").child(userUID).
                child(medicationKey).child("max_count");

        databaseReference.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                int maxAlarmCount = dataSnapshot.getValue(Integer.class);
                adapter.setAlarmCount(maxAlarmCount);
                alarms.clear();
                alarmKeys.clear();
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
