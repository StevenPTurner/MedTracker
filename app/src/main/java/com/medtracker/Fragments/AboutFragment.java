package com.medtracker.Fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.medtracker.medtracker.R;

/**
 * A simple {@link Fragment} subclass.
 * https://github.com/PhilJay/MPAndroidChart
 * icons
 * firebase
 */
public class AboutFragment extends Fragment {




    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override //used for setting up variables
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Log.d(TAG, "loadedFragment");
        TextView openSource = (TextView) getView().findViewById(R.id.open_source);

        String LicenseInfo = GoogleApiAvailability
                .getInstance()
                .getOpenSourceSoftwareLicenseInfo(getActivity());

        openSource.setText(LicenseInfo);
    }



}
