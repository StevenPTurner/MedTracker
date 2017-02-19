package com.medtracker.Activities;

import android.app.Activity;
import android.os.Bundle;

import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

public class TakeDoseActivity extends Activity {
    private static final String TAG = LogTag.takeDoseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_dose);

    }
}
