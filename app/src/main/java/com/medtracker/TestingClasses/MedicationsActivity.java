package com.medtracker.TestingClasses;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medtracker.medtracker.R;
import com.medtracker.models.Medication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spt10 on 21/01/2017.
 */

public class MedicationsActivity extends Activity {

    private static final String TAG = "MedicationsActivity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;
    private List<String> medicationID = new ArrayList<>();
    private List<Medication> medications = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUID = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("medications").child(userUID);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onStart() {
        super.onStart();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, medicationID);

        listView.setAdapter(adapter);


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Medication medication = dataSnapshot.getValue(Medication.class);
                String medicationInfo = "Name: " + medication.getMedication_name() + "\n" +
                        "Info: " + medication.getInstructions() + "\n" +
                        "Dose: " + medication.getDosage() + "mg\n";
                medications.add(medication);
                medicationID.add(medicationInfo);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                Medication medication = dataSnapshot.getValue(Medication.class);
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

}