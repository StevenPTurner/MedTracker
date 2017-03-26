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
    String aboutApp;
    String technologies;
    String technologiesTitle;


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
        setAboutApp();
        setTechnologiesUsed();
        setTitles();

        TextView textAbout = (TextView) getView().findViewById(R.id.about);
        TextView textTech = (TextView) getView().findViewById(R.id.technologies);
        TextView textTechTitles = (TextView) getView().findViewById(R.id.technologies_title);

        textAbout.setText(aboutApp);
        textTech.setText(technologies);
        textTechTitles.setText(technologiesTitle);
    }

    private void setTechnologiesUsed() {
        technologies = "" +
                "GitHub.\n" +
                "Android Studio.\n" +
                "Atom IDE.\n" +
                "Firebase Authentication.\n" +
                "Firebase Realtime Database.\n" +
                "Google maps API.\n" +
                "Google places API\n" +
                "MPAndroidChart library for Charts.\n" +
                "Volley for network HTTP requests.";
    }

    private void setAboutApp() {
        aboutApp = "MedTracker is an an Android application created by Steven Turner for the" +
                " purpose of an honours project during the period of 2016/17. It's main aim is to " +
                "simplify the act of taking medication and allow statistics to be recorded along the " +
                "way.";
    }

    private void setTitles() {
        technologiesTitle = "Many technologies have been used during developent including.";
    }



}
