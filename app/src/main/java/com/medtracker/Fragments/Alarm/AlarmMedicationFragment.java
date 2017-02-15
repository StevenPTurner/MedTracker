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
import android.widget.ListView;

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
import com.medtracker.Models.Medication;
import com.medtracker.Adapters.AlarmAdapter;
import com.medtracker.Utilities.LogTag;
import com.medtracker.Utilities.RC;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmMedicationFragment extends Fragment implements AlarmAdapter.AlarmAdapterCallback {
    private static final String TAG = LogTag.alarmMedicationFragment;
    private final int RC_TIME_PICKER = RC.DATE_PICKER;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;

    private ArrayList<Alarm> alarms = new ArrayList<>();
    private ListView listView;
    private AlarmAdapter adapter;
    private int alarmCount;
    private String medicationKey;
    private Button addAlarm;

    public AlarmMedicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.alarmCount = args.getInt("maxCount");
        this.medicationKey = args.getString("medicationKey");
        // Inflate the adapter_item_alarm_manager for this fragment
        return inflater.inflate(R.layout.fragment_alarm_medication, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "loadedFragment");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUID = mFirebaseUser.getUid();
        Log.d(TAG, mFirebaseUser.getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) getView().findViewById(R.id.listView);


        addAlarm = (Button) getView().findViewById(R.id.button_add_alarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(AlarmMedicationFragment.this, RC_TIME_PICKER);
                timePicker.show(getFragmentManager().beginTransaction(), "timePicker");
            }
        });


        if(listView == null) {
            Log.d(TAG, "ListView is null");
        } else {
            Log.d(TAG, "ListVIew is not null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new AlarmAdapter
                (getActivity().getApplicationContext(),
                        alarms, alarmCount, userUID);
        adapter.setCallback(this);
        listView.setAdapter(adapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Alarm alarm = dataSnapshot.getValue(Alarm.class);
                alarms.add(alarm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
//                Medication medication = dataSnapshot.getValue(Medication.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
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
        };
        mDatabase.child("alarms").child(userUID).child(medicationKey).
                addChildEventListener(childEventListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_TIME_PICKER:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    int hourOfDay = Integer.parseInt(bundle.getString("hourOfDay"));
                    int minute = Integer.parseInt(bundle.getString("minute"));
                    addAlarm(hourOfDay,minute);
                }
                break;
        }
    }

    private void addAlarm(int hour, int minute) {
        Alarm toAdd = alarmBuilder(hour,minute, alarms.size(),medicationKey);
        String alarmKey = toAdd.getMedication_key() + "_" + toAdd.getId();

        mDatabase.child("alarms").child(userUID).child(medicationKey).child(alarmKey).
                setValue(toAdd);
        Log.d(TAG, alarmKey + " added to database");
        updateAlarmManager(toAdd.getMedication_key(), "add");
    }

    @Override
    public void editAlarm(Alarm toEdit) {

    }

    @Override
    public void deleteAlarm(Alarm toDelete) {
        Log.d(TAG, "AlarmKey to delete:" + toDelete.getMedication_key());
        String alarmKey = toDelete.getMedication_key() + "_" + toDelete.getId();

        mDatabase.child("alarms").child(userUID).child(toDelete.getMedication_key()).
                child(alarmKey).removeValue();
        updateAlarmManager(toDelete.getMedication_key(), "delete");
        Log.d(TAG, "Alarm deleted from the database");

    }

    //used to update the alarm manager
    private void updateAlarmManager(String medicationKey, String method){
        //reference to object location
        final String action = method;
        final DatabaseReference databaseReference = mDatabase.child("alarm_manager").child(userUID).
                child(medicationKey);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                AlarmManager alarmManager = dataSnapshot.getValue(AlarmManager.class);
                int maxCount = alarmManager.getMax_count();

                switch(action) {
                    case "add":
                        if (maxCount < 1)
                            alarmManager.setHas_alarm(true);

                        maxCount = maxCount + 1;
                        alarmManager.setMax_count(maxCount);
                        break;
                    case "delete":
                        maxCount = maxCount - 1;
                        alarmManager.setMax_count(maxCount);
                        if (maxCount < 1)
                            alarmManager.setHas_alarm(false);

                        break;
                }
                databaseReference.setValue(alarmManager);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addListenerForSingleValueEvent(postListener);
    }

    private Alarm alarmBuilder(int hour, int minute, int currentID, String medicationKey){
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        Log.d(TAG, "currentHour: " + currentHour + ":" + hour);

        if (currentHour > hour)
            currentDay = currentDay + 1;

        Alarm alarm = new Alarm(currentID+1, minute, hour, currentDay, currentMonth, currentYear,
                medicationKey);

        return alarm;
    }



    //    //when the user returns to this fragment from another
//    @Override
//    public void onResume() {
//        Log.d(TAG, "Fragment resumed");
//        //adapter.clear();
//        super.onResume();
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }

}
