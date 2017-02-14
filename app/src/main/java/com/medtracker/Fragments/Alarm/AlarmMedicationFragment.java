package com.medtracker.Fragments.Alarm;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medtracker.Models.Alarm;
import com.medtracker.Models.Medication;
import com.medtracker.Utilities.Adapters.AlarmAdapter;
import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmMedicationFragment extends Fragment {
    private static final String TAG = LogTag.alarmMedicationFragment;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;

    private ArrayList<Alarm> alarms = new ArrayList<>();
    private ListView listView;
    private AlarmAdapter adapter;
    private int alarmCount;

    public AlarmMedicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        alarmCount = args.getInt("maxCount");

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
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("alarms").child(userUID);
        listView = (ListView) getView().findViewById(R.id.listView);

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
                        alarms, alarmCount);
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (getActivity().getApplicationContext(),
//                        android.R.layout.simple_list_item_1,
//                        alarms);

        listView.setAdapter(adapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Alarm alarm = dataSnapshot.getValue(Alarm.class);
//                String alarmInfo = "Name: " + alarm.getMedication_key() + "\n" +
//                    "Hour: " + alarm.getHour();
//                alarms.add(alarmInfo);
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
                Medication medication = dataSnapshot.getValue(Medication.class);
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
        mDatabase.addChildEventListener(childEventListener);
    }

    //when the user returns to this fragment from another
    @Override
    public void onResume() {
        Log.d(TAG, "Fragment resumed");
        //adapter.clear();
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




}