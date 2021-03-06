package com.medtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

//Used to get user input for creating an account and returning it to the starting activity
//Used for e-mail accounts
public class CreateAccountActivity extends Activity {
    private final String TAG = LogTag.createAccountActivity;
    //user inputs
    private EditText editEmail;
    private EditText editPassword;
    private EditText editForename;
    private EditText editSurname;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //initialise input fields
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editForename = (EditText) findViewById(R.id.edit_forename);
        editSurname = (EditText) findViewById(R.id.edit_surname);
        resultIntent = getIntent();
        Log.d(TAG, "Activity initialised");
    }

    //When the user submits values this handles and returns them to the start activity to be
    //processed
    public void registerAccountDetails(View view) {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String displayName = editForename.getText().toString() + " " + editSurname.getText().toString();

        //if statements being used to prevent empty details being submitted
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter an email address",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter a password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(),
                    "Passwords must be at lest 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        //return this as the result to the start activity
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("password", password);
        resultIntent.putExtra("displayName", displayName);
        setResult(Activity.RESULT_OK, resultIntent);
        Log.d(TAG, "returning results to start activity");
        finish();
    }

    //used to prevent crashed when the back button is pressed
    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
        super.onBackPressed();
    }
}
