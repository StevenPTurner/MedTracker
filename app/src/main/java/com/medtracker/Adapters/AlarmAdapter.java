package com.medtracker.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medtracker.Models.Alarm;
import com.medtracker.Models.AlarmManager;
import com.medtracker.Utilities.LogTag;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by spt10 on 08/02/2017.
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

//used to hold a list of card objects for alarms
public class AlarmAdapter extends ArrayAdapter<Alarm> {
    private static final String TAG = LogTag.alarmAdapter;

    private int alarmCount;
    private Button editAlarm;
    private Button deleteAlarm;
    private DatabaseReference mDatabase;
    private String userUid;
    private Alarm alarm;

    //constructor mainly used to get arguments passed in
    public AlarmAdapter(Context context, ArrayList<Alarm> alarms, int alarmCount,
                        String userUid) {
        super(context, 0, alarms);
        this.alarmCount = alarmCount;
        this.userUid = userUid;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "adapter initialised");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        alarm = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_alarm,
                    parent,
                    false);
        }

        // Lookup view for data population
        TextView alarmNumber = (TextView) convertView.findViewById(R.id.alarm_number);
        TextView alarmTime = (TextView) convertView.findViewById(R.id.alarm_time);
        TextView timeTillAlarm = (TextView) convertView.findViewById(R.id.time_till_alarm);

        //get buttons
        editAlarm = (Button) convertView.findViewById(R.id.button_edit_alarm);
        deleteAlarm = (Button) convertView.findViewById(R.id.button_delete_alarm);

        //collects raw data and parses into useable formats
        Calendar alarmCalendar = Utility.alarmToCalendar(alarm);
        String dateToDisplay = Utility.calendarToString(alarmCalendar);
        String timeTillAlarmText = Utility.calcTimeDif(alarmCalendar);
        Log.d(TAG, "parsed all data, alarm time: " + timeTillAlarmText);


        // Populate the data into the template view using the data object
        alarmNumber.setText("Alarm " + alarm.getId() + " of " + alarmCount);
        alarmTime.setText(dateToDisplay);
        timeTillAlarm.setText(timeTillAlarmText);

        //setup onclick listeners for each cards buttons
        editAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "edit alarm button pressed on card");

            }
        });

        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "delete button pressed on card");
                //generates key and calls delete alarm method
                String key = alarm.getMedication_key() + "_" + alarm.getId();
                deleteAlarm(key);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    //used to delete alarms from the database
    private void deleteAlarm(String alarmKey){
        Log.d(TAG, "AlarmKey to delete:" + alarmKey);
        //creates database object
        mDatabase.
                child("alarms").
                child(userUid).
                child(alarm.getMedication_key()).
                child(alarmKey).
                removeValue();

        //updates the alarm manager object
        updateAlarmManager();

        Log.d(TAG, "Alarm deleted from the database");
    }

    //used to update the alarm manager
    private void updateAlarmManager(){
        //reference to object location
        final DatabaseReference databaseReference = mDatabase.
                child("alarm_manager").
                child(userUid).
                child(alarm.getMedication_key());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retreives object updates it and sends the newly updates one back to the database
                AlarmManager alarmManager = dataSnapshot.getValue(AlarmManager.class);
                int maxCount = alarmManager.getMax_count();
                maxCount = maxCount - 1;
                alarmManager.setMax_count(maxCount);

                if (maxCount < 1)
                    alarmManager.setHas_alarm(false);

                databaseReference.setValue(alarmManager);
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addListenerForSingleValueEvent(postListener);
    }
}
