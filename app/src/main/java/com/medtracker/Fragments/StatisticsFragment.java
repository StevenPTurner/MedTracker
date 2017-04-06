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
import com.github.mikephil.charting.data.Entry;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * http://stackoverflow.com/questions/7855387/percentage-of-two-int
 */
public class StatisticsFragment extends Fragment {
    private static final String TAG = LogTag.statisticsFragment;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private String userUID;
    private ArrayList<Record> records = new ArrayList<>();
    private ArrayList<String> recordKeys = new ArrayList<>();
    private PieChart pieChart;
    private PieDataSet set;

    private int totalOnTime;
    private int totalLate;
    private int totalMissed;
    List<PieEntry> entries = new ArrayList<>();

    private TextView alertBox;

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
        set = new PieDataSet(entries, "Medication statistics");
        alertBox = (TextView) getActivity().findViewById(R.id.alert_box);
        initialisePieChartStyle();
    }

    @Override
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

    private void setChartValues(float onTime, float late, float missed) {
        entries.clear();
        entries.add(new PieEntry(onTime, "On Time"));
        entries.add(new PieEntry(late, "Late"));
        entries.add(new PieEntry(missed, "Missed"));
    }

    private float calcPercentage(float total, float current){
        return ((current * 100.0f) / total);
    }

    private void updateStats(int totalRecords) {
        float onTimePercentage = calcPercentage(totalRecords, totalOnTime);
        float latePercentage = calcPercentage(totalRecords, totalLate);
        float missedPercentage = calcPercentage(totalRecords, totalMissed);

        PieData data = new PieData(set);
        alertBox.setText(getCenterText(latePercentage,missedPercentage));
        pieChart.setData(data);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.invalidate(); // refresh

        setChartValues(onTimePercentage, latePercentage, missedPercentage);
    }


    private void initialisePieChartStyle() {
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(ColorTemplate.rgb("#66BB6A"));
        colors.add(ColorTemplate.rgb("#FFA726"));
        colors.add(ColorTemplate.rgb("#ef5350"));
        colors.add(ColorTemplate.getHoloBlue());
        set.setColors(colors);
        set.setSliceSpace(3f);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(15f);
    }

    private String getCenterText(float late, float missed) {
        if(missed > 25) {
            return "Medication will not be effective at current usage rates";
        } else if (missed > 10) {
            return "Usage rates will effect medications effectiveness";
        } else if (late > 50) {
            return "Majority of doses are late, change dosage time";
        } else if (late  > 25) {
            return "Consider changing dosage time";
        } else {
            return "";
        }
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
