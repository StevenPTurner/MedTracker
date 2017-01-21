package com.medtracker.TestingClasses;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medtracker.medtracker.R;
import com.medtracker.models.Medication;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzs.TAG;

public class DataBaseTestActivity extends Activity {

    private DatabaseReference database;
    TextView textViewMessage;
    TextView textViewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_test);

        database = FirebaseDatabase.getInstance().getReference();
        textViewMessage = (TextView) findViewById(R.id.textView_message);
        textViewUser = (TextView) findViewById(R.id.textView_user);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            textViewUser.setText("User: " + user.getDisplayName());
        } else {
            textViewUser.setText("User: ERROR");
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String currentValue = dataSnapshot.getValue(String.class);
                textViewMessage.setText("Message: " + currentValue);
                Log.w(TAG, "CurrentValue: " + currentValue);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        database.child("message").addValueEventListener(postListener);


    }

    public void updateDatabase(View view) {
          EditText editTextValue = (EditText) findViewById(R.id.edittext_to_insert);
          String value = editTextValue.getText().toString();
          database.child("message").setValue(value);
    }



}

