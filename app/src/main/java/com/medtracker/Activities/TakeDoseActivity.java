package com.medtracker.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medtracker.Models.Alarm;
import com.medtracker.Models.AlarmManager;
import com.medtracker.Models.Medication;
import com.medtracker.Models.Record;
import com.medtracker.Utilities.LogTag;
import com.medtracker.Utilities.NotificationManager;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

// //https://gist.github.com/BrandonSmith/6679223
public class TakeDoseActivity extends Activity {
    private static final String TAG = LogTag.takeDoseActivity;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;
    private Button takeDose;
    private String alarmKey;
    private String medicationKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_dose);

        Log.d(TAG, "loaded activity"); //Get logged in user info
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUID = mFirebaseUser.getUid();
        Log.d(TAG, userUID);
        mDatabase = FirebaseDatabase.getInstance().getReference(); //get database object
        Log.d(TAG, "Setup complete");

        //button listener to preform actions when pressed
        takeDose = (Button) findViewById(R.id.button_take_dose);
        takeDose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "take dose button pressed");
                writeRecord();
            }
        });

        alarmKey = getIntent().getStringExtra("alarmKey");
        medicationKey = getIntent().getStringExtra("medicationKey");
    }

    public void writeRecord() {
        final DatabaseReference databaseReference = mDatabase.child("alarms").child(userUID).
                child(medicationKey).child(alarmKey);

        databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                Alarm alarm = dataSnapshot.getValue(Alarm.class);
                Record record = Utility.alarmToRecord(alarm);
                String recordKey = 0 + "" + record.getMinute() + record.getHour() + "-" + record.getDay()
                        + record.getMonth() + record.getYear();

                mDatabase.child("records").child(userUID).child(recordKey).setValue(record);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });


    }
}
