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

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static NearestPharmacyFragment newInstance(Pharmacy pharmacy) {
        NearestPharmacyFragment fragment = new NearestPharmacyFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("name", pharmacy.getName());
        args.putString("info", pharmacy.getInfo());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        name = args.getString("name");
        info = args.getString("info");

        View v = inflater.inflate(R.layout.fragment_nearest_pharmacy, container, false);

        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.okay);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                //((FragmentDialog)getActivity()).showDialog();
            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textName = (TextView) getView().findViewById(R.id.pharmacy_name);
        TextView textInfo = (TextView) getView().findViewById(R.id.pharmacy_name);

        textName.setText(name);
        textInfo.setText(info);

    }



}
