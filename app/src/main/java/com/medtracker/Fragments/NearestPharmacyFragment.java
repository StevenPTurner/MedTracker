package com.medtracker.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.medtracker.Models.Pharmacy;
import com.medtracker.medtracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearestPharmacyFragment extends DialogFragment {
    String name;
    String info;

    public NearestPharmacyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        name = args.getString("name");
        info = args.getString("info");
        getDialog().setTitle("Nearest Pharmacy");

        return inflater.inflate(R.layout.fragment_nearest_pharmacy, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView textName = (TextView) getView().findViewById(R.id.pharmacy_name);
        TextView textInfo = (TextView) getView().findViewById(R.id.pharmacy_info);
        String nameField = "Your nearest pharmacy is " + name;
        String infoField = "Located at " + info;

        textName.setText(nameField);
        textInfo.setText(infoField);

        // Watch for button clicks.
        Button button = (Button) getView().findViewById(R.id.okay);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

    }



}
