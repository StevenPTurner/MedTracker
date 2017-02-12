package com.medtracker.Utilities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medtracker.Models.Medication;
import com.medtracker.medtracker.R;

import java.util.ArrayList;

/**
 * Created by spt10 on 30/01/2017.
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class MedicationAdapter extends ArrayAdapter<Medication> {

    public MedicationAdapter(Context context, ArrayList<Medication> medications) {
        super(context, 0, medications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Medication medication = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_medication, parent, false);
        }
        // Lookup view for data population
        TextView medicationName = (TextView) convertView.findViewById(R.id.medication_name);
        TextView medicationDosage = (TextView) convertView.findViewById(R.id.medication_dosage);
        TextView medicationInstructions = (TextView) convertView.findViewById(R.id.medication_instructions);

        // Populate the data into the template view using the data object
        medicationName.setText(medication.getMedication_name() + ": ");
        medicationDosage.setText(String.valueOf(medication.getDosage()) + "mg");
        medicationInstructions.setText(medication.getInstructions());

        // Return the completed view to render on screen
        return convertView;
    }
}