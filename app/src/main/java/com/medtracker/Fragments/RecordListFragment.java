package com.medtracker.Fragments;


import android.app.Fragment;
import android.content.Context;
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
import com.medtracker.Adapters.RecordAdapter;
import com.medtracker.Models.Record;
import com.medtracker.Utilities.LogTag;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordListFragment extends Fragment {
    private static final String TAG = LogTag.recordListFragment;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private String userUID;
    private ArrayList<Record> records = new ArrayList<>();
    private ArrayList<String> recordKeys = new ArrayList<>();
    private RecordAdapter adapter;
    private ListView listView;

    public RecordListFragment() { /* Required empty public constructor*/}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_list, container, false);
    }

    @Override //used for setting up variables
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "loadedFragment");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userUID = firebaseUser.getUid();
        Log.d(TAG, firebaseUser.getUid());
        database = FirebaseDatabase.getInstance().getReference().child("records").child(userUID);
        listView = (ListView) getView().findViewById(R.id.listView);

        //button listener to preform actions when pressed
        Button test = (Button) getView().findViewById(R.id.email_test);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createCSV();
            }
        });

        if(listView == null)
            Log.d(TAG, "ListView is null");
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new RecordAdapter(getActivity().getApplicationContext(), records);

        listView.setAdapter(adapter);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Record medication = dataSnapshot.getValue(Record.class);
                records.add(medication);
                recordKeys.add(dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Record changedRecord = dataSnapshot.getValue(Record.class);
                String recordKey = dataSnapshot.getKey();
                int index = recordKeys.indexOf(recordKey);

                if (index > -1) {
                    records.set(index, changedRecord);
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + recordKey);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String keyToRemove = dataSnapshot.getKey();
                int index = recordKeys.indexOf(keyToRemove);

                if (index > -1) {
                    records.remove(index);
                    recordKeys.remove(index);
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

    private String createCSV() {
        String titles = "Medication Name,Dose,Time,Date \n";
        String content = titles;

        for(int i=0;i<records.size();i++) {
            Record record = records.get(i);
            String name = Utility.keyToName(record.getMedication_key());
            int dose = record.getDose();

            String time = record.getHour() + ":" +
                    record.getMinute() + ":" +
                    record.getSecond();

            String date = record.getDay() + "/" +
                    record.getMonth() + "/" +
                    record.getYear();

            content = content + name + "," + dose + "," + time + "," + date + "\n";
        }

        Log.d(TAG,content);
        return content;
    }





}
