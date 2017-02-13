package com.medtracker.Utilities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medtracker.Models.AlarmManager;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

import java.util.ArrayList;

/**
 * Created by spt10 on 12/02/2017.
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class AlarmManagerAdapter extends ArrayAdapter<AlarmManager> {

    public AlarmManagerAdapter(Context context, ArrayList<AlarmManager> alarmManagers) {
        super(context, 0, alarmManagers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AlarmManager alarmManager = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_alarm_manager, parent, false);
        }

        // Lookup view for data population
        TextView medicationName = (TextView) convertView.findViewById(R.id.medication_name);
        TextView alarmCount = (TextView) convertView.findViewById(R.id.no_of_alarms);
        ImageView alarmIcon = (ImageView) convertView.findViewById(R.id.alarm_icon);

        //gathers raw data and convers to useable formats
        String medicationNameText = Utility.keyToName(alarmManager.getMedication_key());
        boolean hasAlarms = alarmManager.isHas_alarm();
        String noOfAlarms = String.valueOf(alarmManager.getMax_count());

        // Populate the data into the template view using the data object
        medicationName.setText(medicationNameText + ": ");
        alarmCount.setText(noOfAlarms + " alarms set.");

        //sets alarm icon
        if (hasAlarms)
            alarmIcon.setColorFilter(R.color.colorPrimary);

        // Return the completed view to render on screen
        return convertView;
    }

}
