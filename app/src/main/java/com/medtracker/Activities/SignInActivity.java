package com.medtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;

import com.medtracker.medtracker.R;

public class SignInActivity extends Activity {

    private static final String TAG = "LogSignInActivity";
    private EditText editEmail;
    private EditText editPassword;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
    }

    public void signInDetails() {
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();

        Intent resultIntent = getIntent();
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("password", password);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
