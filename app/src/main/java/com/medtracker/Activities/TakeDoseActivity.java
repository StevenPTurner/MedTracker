package com.medtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.medtracker.Utilities.Convert;
import com.medtracker.Utilities.LogTag;
import com.medtracker.Utilities.NotificationManager;
import com.medtracker.Utilities.TimeCalc;
import com.medtracker.medtracker.R;


// This activity appears after user slects a notification
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
    private Medication medication;
    private Alarm alarm;


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
        final Intent intent = new Intent(this, HomeActivity.class);
        takeDose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "take dose button pressed");
                writeRecord();
                updateAlarmManager();
                startActivity(intent);
            }
        });

        alarmKey = getIntent().getStringExtra("alarmKey");
        medicationKey = getIntent().getStringExtra("medicationKey");

        getData();
        Log.d(TAG, medicationKey);
    }

    public void getData() {
        final DatabaseReference databaseReference = mDatabase;

        databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                alarm = dataSnapshot.child("alarms").child(userUID).child(medicationKey)
                        .child(alarmKey).getValue(Alarm.class);
                medication = dataSnapshot.child("medications").child(userUID).child(medicationKey)
                        .getValue(Medication.class);
                TextView messageText = (TextView) findViewById(R.id.medication_info);
                messageText.setText(medication.getDosage() + "mg of " + medication.getMedication_name());
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void writeRecord() {
        int dose = medication.getDosage();
        Record record = Convert.alarmToRecord(alarm, dose);
        String recordKey = 0 + "" + record.getMinute() + record.getHour() + "-" + record.getDay()
                + record.getMonth() + record.getYear();

        mDatabase.child("records").child(userUID).child(recordKey).setValue(record);
    }

    public void updateAlarmManager() {
        final DatabaseReference databaseReference = mDatabase.child("alarm_manager").child(userUID).
                child(medicationKey);

        databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                AlarmManager alarmManager = dataSnapshot.getValue(AlarmManager.class);
                int currentCount = alarmManager.getCurrent_count();
                currentCount = currentCount + 1;
                if(currentCount > alarmManager.getMax_count()) {
                    currentCount = 1;
                }
                alarmManager.setCurrent_count(currentCount);
                String alarmKey = medicationKey + "_" + currentCount;
                Log.d(TAG, "alarmKey, " + alarmKey); //Get logged in user info
                alarmManager.setCurrent_alarm(alarmKey);
                databaseReference.setValue(alarmManager);
                setNextAlarm(alarmKey);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void setNextAlarm(String alarmKey) {
        final DatabaseReference databaseReference = mDatabase.child("alarms").child(userUID).
                child(medicationKey).child(alarmKey);
        Log.d(TAG, "alarmKey, " + alarmKey); //Get logged in user info

        databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                Alarm alarm = dataSnapshot.getValue(Alarm.class);
                Log.d(TAG, "medKey, " + alarm.getMedication_key()); //Get logged in user info
                NotificationManager.enableNextAlarm(alarm, TakeDoseActivity.this);
                alarm = TimeCalc.incrementAlarm(alarm);
                databaseReference.setValue(alarm);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }








}
