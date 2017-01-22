package com.medtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.medtracker.database.MedtrackerDbHelper;
import com.medtracker.medtracker.R;

public class CreateAccountActivity extends Activity {

    private EditText editEmail;
    private EditText editPassword;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
    }

    public void RegisterAccountDetails(View view) {
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();

        Intent resultIntent = getIntent();
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("password", password);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
