package com.medtracker.medtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.medtracker.database.MedtrackerDbHelper;

public class CreateAccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void createAccount(View view) {
        MedtrackerDbHelper db = new MedtrackerDbHelper(this);
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editTextFirstName = (EditText) findViewById(R.id.edittext_firstname);
        EditText editTextLastName = (EditText) findViewById(R.id.edittext_lastname);
        EditText editTextEmail = (EditText) findViewById(R.id.edittext_email);
        EditText editTextPin = (EditText) findViewById(R.id.edittext_pin);

        String email = editTextEmail.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        int pin = Integer.parseInt(editTextPin.getText().toString());

        db.createUser(email, firstName, lastName, pin, 0);
        db.close();
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
        //TextView text = (TextView) findViewById(R.id.textview_register_account);
        //text.setText("yay!");
    }
}
