package com.medtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.medtracker.medtracker.R;

//used to get user input for creating an account and returing it to the starting activity
//  user for e-mail accounts
public class CreateAccountActivity extends Activity {
    private static final String TAG = "LogCreateAccountActivity";
    private EditText editEmail;
    private EditText editPassword;
    private EditText editForename;
    private EditText editSurname;

    String email;
    String password;
    String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editForename = (EditText) findViewById(R.id.edit_forename);
        editSurname = (EditText) findViewById(R.id.edit_surname);
    }

    //when the user submits values this handles and returns them to the start activity to be
    // processed
    public void registerAccountDetails(View view) {
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        displayName = editForename.getText().toString() + " " + editSurname.getText().toString();

        Intent resultIntent = getIntent();
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("password", password);
        resultIntent.putExtra("displayName", displayName);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
