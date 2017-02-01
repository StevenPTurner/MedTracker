package com.medtracker.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medtracker.medtracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicationEditFragment extends Fragment {
    private String medicationName;
    private static final String TAG = "LogMedicationEditFrag";

    public MedicationEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        medicationName = args.getString("medicationID");
        Log.d(TAG,medicationName);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medication_edit, container, false);
    }

}
