package com.medtracker.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.medtracker.Models.Alarm;
import com.medtracker.Utilities.Convert;
import com.medtracker.Utilities.LogTag;
import com.medtracker.Utilities.TimeCalc;
import com.medtracker.medtracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * used to display alarms in list format
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

//used to hold a list of card objects for alarms
public class AlarmAdapter extends ArrayAdapter<Alarm>  {
    private static final String TAG = LogTag.alarmAdapter;

    private AlarmAdapterCallback callback;
    private int alarmCount;
    private Alarm alarm;

    //constructor mainly used to get arguments passed in
    public AlarmAdapter(Context context, ArrayList<Alarm> alarms, int alarmCount) {
        super(context, 0, alarms);
        this.alarmCount = alarmCount;
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
        Button editAlarm = (Button) convertView.findViewById(R.id.button_edit_alarm);
        Button deleteAlarm = (Button) convertView.findViewById(R.id.button_delete_alarm);

        //collects raw data and parses into usable formats
        Calendar alarmCalendar = Convert.alarmToCalendar(alarm);
        String dateToDisplay = Convert.calendarToString(alarmCalendar);
        long timeTillAlarmInMillis = TimeCalc.calcTimeDiff(alarmCalendar);
        Log.d(TAG, "parsed all data, alarm time: " + timeTillAlarmInMillis);

        // Populate the data into the template view using the data object
        alarmNumber.setText("Alarm " + alarm.getId() + " of " + alarmCount);
        alarmTime.setText(dateToDisplay);
        timeTillAlarm.setText(createNextAlarmText(timeTillAlarmInMillis));

        //setup onclick listeners for each cards buttons
        editAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "edit alarm button pressed on card");
                callback.editAlarm(alarm);
            }
        });

        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "delete button pressed on card");
                callback.deleteAlarm(alarm);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private String createNextAlarmText(long timeTillNextAlarm) {
        long hours = TimeUnit.MILLISECONDS.toHours(timeTillNextAlarm);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeTillNextAlarm - (hours * 60 * 60 * 1000));
        return Long.toString(hours) + " Hours & " + Long.toString(minutes) + " Minutes from now";
    }

    public void setAlarmCount(int alarmCount) {
        this.alarmCount = alarmCount;
    }

    //initialises the callbacks
    public void setCallback(AlarmAdapterCallback callback){
        this.callback = callback;
    }

    //interface used to communicate with the parent fragment
    public interface AlarmAdapterCallback {
        void deleteAlarm(Alarm toDelete);
        void editAlarm(Alarm toEdit);
    }

}
