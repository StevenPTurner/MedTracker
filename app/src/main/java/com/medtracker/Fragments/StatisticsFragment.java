package com.medtracker.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medtracker.Models.Record;
import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * http://stackoverflow.com/questions/7855387/percentage-of-two-int
 */
public class StatisticsFragment extends Fragment {
    private static final String TAG = LogTag.statisticsFragment;

    //firebase components
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private String userUID;
    private ArrayList<Record> records = new ArrayList<>();
    private ArrayList<String> recordKeys = new ArrayList<>();

    //pie chart
    private PieChart pieChart;
    private PieDataSet set;
    private List<PieEntry> entries = new ArrayList<>();
    private int totalOnTime;
    private int totalLate;
    private int totalMissed;

    //UI views
    private TextView lateAlertBox;
    private TextView missedAlertBox;

    public StatisticsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override //used for setting up variables
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "loadedFragment");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userUID = firebaseUser.getUid();
        Log.d(TAG, firebaseUser.getUid());
        database = FirebaseDatabase.getInstance().getReference().child("records").child(userUID);

        pieChart = (PieChart) getView().findViewById(R.id.chart);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);

        set = new PieDataSet(entries, "");
        lateAlertBox = (TextView) getActivity().findViewById(R.id.alert_box_late);
        missedAlertBox = (TextView) getActivity().findViewById(R.id.alert_box_missed);
        initialisePieChartStyle();
        initialiseLegend();
    }

    @Override //used mainly to get data from database
    public void onStart() {
        super.onStart();

        ChildEventListener childEventListener = new ChildEventListener() {

            @Override //when a new item is added
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Record record = dataSnapshot.getValue(Record.class);
                records.add(record);
                recordKeys.add(dataSnapshot.getKey());

                if (record.isLate()) {
                    totalLate++;
                } else if(record.isMissed()) {
                    totalMissed++;
                } else {
                    totalOnTime ++;
                }

                updateStats(records.size());
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
            }

            @Override // when an item is edited
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Record changedRecord = dataSnapshot.getValue(Record.class);
                String recordKey = dataSnapshot.getKey();
                int index = recordKeys.indexOf(recordKey);

                if (index > -1) {
                    records.set(index, changedRecord);
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + recordKey);
                }
            }

            @Override //when an item is removed
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String keyToRemove = dataSnapshot.getKey();
                int index = recordKeys.indexOf(keyToRemove);

                if (index > -1) {
                    records.remove(index);
                    recordKeys.remove(index);
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                } else {
                    Log.d(TAG, "Index: " + index + " is an invalid index");
                }
                Log.d(TAG, "adapterNotified");
            }

            @Override //when a child is moved
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override //when it is canceled
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "MedicationsActivity:onCancelled", databaseError.toException());
            }
        };
        database.addChildEventListener(childEventListener);
    }

    //get's stats and sets up other components with them
    private void updateStats(int totalRecords) {
        float onTimePercentage = calcPercentage(totalRecords, totalOnTime);
        float latePercentage = calcPercentage(totalRecords, totalLate);
        float missedPercentage = calcPercentage(totalRecords, totalMissed);

        setChartValues(onTimePercentage, latePercentage, missedPercentage);
        initialiseAlertBox(latePercentage, missedPercentage);

        PieData data = new PieData(set);
        pieChart.setData(data);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.invalidate(); // refresh
    }

    private void setChartValues(float onTime, float late, float missed) {
        entries.clear();
        entries.add(new PieEntry(onTime, "On Time"));
        entries.add(new PieEntry(late, "Late"));
        entries.add(new PieEntry(missed, "Missed"));
    }

    //sets up alert box colour and text
    private void initialiseAlertBox(float latePercentage, float missedPercentage) {
        if(latePercentage >15) {
            lateAlertBox.setVisibility(View.VISIBLE);
            lateAlertBox.setText(getLateText(latePercentage));
            lateAlertBox.setBackgroundColor(getAlertColour(latePercentage,50));
        }

        if(missedPercentage >10) {
            missedAlertBox.setVisibility(View.VISIBLE);
            missedAlertBox.setText(getMissedText(missedPercentage));
            missedAlertBox.setBackgroundColor(getAlertColour(missedPercentage,25));
        }
    }

    //sets up the style of the pie chart
    private void initialisePieChartStyle() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ColorTemplate.rgb("#66BB6A"));
        colors.add(ColorTemplate.rgb("#FFA726"));
        colors.add(ColorTemplate.rgb("#EF5350"));
        colors.add(ColorTemplate.getHoloBlue());
        set.setColors(colors);
        set.setSliceSpace(3f);
    }

    //sets up the legend
    private void initialiseLegend() {
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setDrawInside(false);
        legend.setTextSize(16f);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(7f);
    }

    //used to define the severity ratings of an issue and get a message
    private String getLateText(float late) {
        if(late >50){
            return genRandomPhrase("late","high");
        } else if (late > 25) {
            return genRandomPhrase("late","med");
        } else if (late  > 15) {
            return genRandomPhrase("late","low");
        } else {
            return null;
        }
    }

    //used to define the severity ratings of an issue and get a message
    private String getMissedText(float missed) {
        if(missed >25){
            return genRandomPhrase("missed","high");
        } else if (missed > 20) {
            return genRandomPhrase("missed","med");
        } else if (missed  > 10) {
            return genRandomPhrase("missed","low");
        } else {
            return null;
        }
    }

    private float calcPercentage(float total, float current){
        return ((current * 100.0f) / total);
    }

    //Gets the colour for the alert bar based on percentage of issue compared to the limit
    private int getAlertColour(float percentage, float limit) {
        int[] colours = getActivity().getResources().getIntArray(R.array.alert_box_colours);
        float threshold = limit/colours.length; //divides limits to work out colour for each stage
        int alertSeverity = (int) (percentage/threshold); //works out current stage

        if (alertSeverity > (colours.length-1)) //used to prevent going over max stage (red)
            alertSeverity = (colours.length-1);

        return colours[alertSeverity]; //returns correct colour for stage
    }

    //Picks a random phrase from an array of strings based on type of issue and severity
    private String genRandomPhrase(String type, String severity){
        Random random = new Random();
        String arrayName = type + "_alerts_" + severity + "_array"; //builds array name
        int arrayId = getResources().getIdentifier(arrayName, "array", //converts to id
                getActivity().getPackageName());

        String phrases[] = getResources().getStringArray(arrayId); //gets resource into an array
        return phrases[random.nextInt(phrases.length)]; //return random phrase between 0 and length
    }

    //when the user returns to this fragment from another
    @Override
    public void onResume() {
        Log.d(TAG, "Fragment resumed");
        records.clear();
        recordKeys.clear();
        super.onResume();
    }

}
