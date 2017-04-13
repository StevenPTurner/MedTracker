package com.medtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

//used to gather and return user info sign in to the starting activity
//used for e-mail sign in
public class SignInActivity extends Activity {
    private final String TAG = LogTag.signInActivity;
    //initialising input fields
    private EditText editEmail;
    private EditText editPassword;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.d(TAG, "Activity loaded");
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);

        Button signIn = (Button) findViewById(R.id.button_sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signInDetails();
            }
        });

        //starts process for resetting password
        Button resetPassword = (Button) findViewById(R.id.button_forgot_password);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, PasswordResetActivity.class);
                startActivity(intent);
            }
        });

    }

    //gathers are returns data when button is pressed
    //http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/
    public void signInDetails() {
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();

        //handles user errors
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter an e-mail address",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //returns data to sign in activity
        Intent resultIntent = getIntent();
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("password", password);
        setResult(Activity.RESULT_OK, resultIntent);
        Log.d(TAG, "returning data to start activity");
        finish();
    }

}
