package com.medtracker.Fragments.Alarm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medtracker.Models.AlarmManager;
import com.medtracker.Adapters.AlarmManagerAdapter;
import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmListFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String TAG = LogTag.alarmListFragment;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private String userUID;
    private ArrayList<AlarmManager> alarmManagers = new ArrayList<>();
    private ArrayList<String> alarmManagerKeys = new ArrayList<>();
    private ListView listView;
    private AlarmManagerAdapter adapter;

    public AlarmListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the adapter_item_alarm_manager for this fragment
        return inflater.inflate(R.layout.fragment_alarm_list, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "loadedFragment");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userUID = firebaseUser.getUid();
        Log.d(TAG, firebaseUser.getUid());
        database = FirebaseDatabase.getInstance().getReference().child("alarm_manager").
                child(userUID);
        listView = (ListView) getView().findViewById(R.id.listView);

        if(listView == null) {
            Log.d(TAG, "ListView is null");
        } else {
            Log.d(TAG, "ListVIew is not null");
        }

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new AlarmManagerAdapter(getActivity().getApplicationContext(), alarmManagers);
        listView.setAdapter(adapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                AlarmManager alarmManager = dataSnapshot.getValue(AlarmManager.class);
                alarmManagers.add(alarmManager);
                alarmManagerKeys.add(dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                AlarmManager changedAlarmManager = dataSnapshot.getValue(AlarmManager.class);
                String alarmManagerKey = dataSnapshot.getKey();
                int index = alarmManagerKeys.indexOf(alarmManagerKey);

                if (index > -1) {
                    alarmManagers.set(index, changedAlarmManager);
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + alarmManagerKey);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String keyToRemove = dataSnapshot.getKey();
                int index = alarmManagerKeys.indexOf(keyToRemove);

                if (index > -1){
                    alarmManagers.remove(index);
                    alarmManagerKeys.remove(index);
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                } else {
                    Log.d(TAG, "Index: " + index + " is an invalid index");
                }
                Log.d(TAG, "adapterNotified");
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
        database.addChildEventListener(childEventListener);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.d(TAG, "starting alarms fragment");
        Fragment alarmFragment = new AlarmMedicationFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        AlarmManager current = adapter.getItem(position);
        args.putInt("maxCount", current.getMax_count());
        args.putString("medicationKey", current.getMedication_key());

        alarmFragment.setArguments(args);
        transaction.replace(R.id.content_frame, alarmFragment);
        transaction.addToBackStack(null);
        Log.d(TAG, "starting single alarm fragment");
        transaction.commit();
    }

    //when the user returns to this fragment from another
    @Override
    public void onResume() {
        Log.d(TAG, "Fragment resumed");
        alarmManagers.clear();
        alarmManagerKeys.clear();
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
