package com.medtracker.Activities;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

public class PasswordResetActivity extends Activity {
    private final String TAG = LogTag.passwordResetActivity;
    private EditText editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        Log.d(TAG, "Activity loaded");

        editEmail = (EditText) findViewById(R.id.edit_email);

        Button resetPassword = (Button) findViewById(R.id.button_reset_password);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter an email address",
                            Toast.LENGTH_SHORT).show();
                    return;
                };
                resetUserPassword(email);
                finish();
            }
        });
    }

    private void resetUserPassword(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(PasswordResetActivity.this, "Password reset email sent.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
