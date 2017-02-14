package com.medtracker.Utilities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medtracker.Models.Alarm;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by spt10 on 08/02/2017.
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    private int alarmCount;

    public AlarmAdapter(Context context, ArrayList<Alarm> alarms, int alarmCount) {
        super(context, 0, alarms);
        this.alarmCount = alarmCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Alarm alarm = getItem(position);
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

        //collects raw data and parses into useable formats
        Calendar alarmCalendar = Utility.alarmToCalendar(alarm);
        String dateToDisplay = Utility.calendarToString(alarmCalendar);
        String timeTillAlarmText = Utility.calcTimeDif(alarmCalendar);

//
//        // Populate the data into the template view using the data object
        alarmNumber.setText("Alarm " + alarm.getId() + " of " + alarmCount);
        alarmTime.setText(dateToDisplay);
        timeTillAlarm.setText(timeTillAlarmText + " Hours");

        // Return the completed view to render on screen
        return convertView;
    }
}
