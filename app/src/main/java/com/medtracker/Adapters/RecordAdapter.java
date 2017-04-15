package com.medtracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medtracker.Models.Record;
import com.medtracker.Utilities.Convert;
import com.medtracker.medtracker.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Used for displaying lists of records in a custom table like format
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_record,
                    parent, false);
        }

        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.table_row);
        if(position % 2 == 0)
            layout.setBackgroundColor(Color.parseColor("#BBDEFB"));

        // Lookup views for data population
        TextView recordName = (TextView) convertView.findViewById(R.id.record_name);
        TextView recordDosage = (TextView) convertView.findViewById(R.id.record_dosage);
        TextView recordTime = (TextView) convertView.findViewById(R.id.record_time);
        TextView recordDate = (TextView) convertView.findViewById(R.id.record_date);

        //collects and gets dates as strings for displaying
        String medicationName = Convert.keyToName(record.getMedication_key());
        Calendar calendar = Convert.recordToCalendar(record);
        String time = Convert.calendarToTime(calendar);
        String date = Convert.calendarToDate(calendar);

        // Populate the data into the template view using the data object
        recordName.setText(medicationName);
        recordDosage.setText(String.valueOf(record.getDose()));
        recordTime.setText(time);
        recordDate.setText(date);

        // Return the completed view to render on screen
        return convertView;
    }
}
