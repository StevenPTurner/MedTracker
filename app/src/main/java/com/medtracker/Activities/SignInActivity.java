package com.medtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

//used to gather and return user info sign in to the starting activitiy
//used for e-mail sign in
public class SignInActivity extends Activity {
    private final String TAG = LogTag.signInActivity;
    //initialising input fields
    private EditText editEmail;
    private EditText editPassword;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Log.d(TAG, "Activity loaded");
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
    }

    //gathers are returns data when button is pressed
    public void signInDetails(View view) {
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();

        //returns data to sign in activity
        Intent resultIntent = getIntent();
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("password", password);
        setResult(Activity.RESULT_OK, resultIntent);
        Log.d(TAG, "returning data to start activity");
        finish();
    }

}
