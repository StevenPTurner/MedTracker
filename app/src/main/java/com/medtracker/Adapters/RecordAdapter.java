package com.medtracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medtracker.Models.Record;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by spt10 on 11/02/2017.
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class RecordAdapter extends ArrayAdapter<Record> {

    public RecordAdapter(Context context, ArrayList<Record> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Record record = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_record, parent, false);
        }
        // Lookup view for data population
        TextView recordName = (TextView) convertView.findViewById(R.id.record_name);
        TextView recordDosage = (TextView) convertView.findViewById(R.id.record_dosage);
        TextView recordTime = (TextView) convertView.findViewById(R.id.record_time);
        TextView recordDate = (TextView) convertView.findViewById(R.id.record_date);

        //collects and gets dates as strings for displaying
        String medicationName = Utility.keyToName(record.getMedication_key());
        Calendar calendar = Utility.recordToCalendar(record);
        String time = Utility.calendarToTime(calendar);
        String date = Utility.calendarToSDate(calendar);

        // Populate the data into the template view using the data object
        recordName.setText(medicationName);
        recordDosage.setText(String.valueOf(record.getDose()));
        recordTime.setText(time);
        recordDate.setText(date);

        // Return the completed view to render on screen
        return convertView;
    }

}
