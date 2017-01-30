package com.medtracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medtracker.medtracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicationEditFragment extends Fragment {


    public MedicationEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medication_edit, container, false);
    }

}
