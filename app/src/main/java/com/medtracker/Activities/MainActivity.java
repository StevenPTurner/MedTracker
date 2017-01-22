package com.medtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medtracker.TestingClasses.DataBaseTestActivity;
import com.medtracker.medtracker.R;
import com.medtracker.models.User;

public class MainActivity extends Activity {
    private static final String TAG = "LogMainActivity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUID = mFirebaseUser.getUid();
        Log.d(TAG, userUID);
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userUID);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    updateUI(user.getDisplay_name(),user.getEmail());
                } else {
                    Intent temp = getIntent();
                    User user = new User(mFirebaseUser.getEmail(),
                            temp.getStringExtra("displayName"),
                            1111);
                    mDatabase.setValue(user);
                    updateUI(user.getDisplay_name(),user.getEmail());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addListenerForSingleValueEvent(userListener);
  }

    private void signOut() {
        // Firebase sign out
        mFirebaseAuth.signOut();
    }

//    public void medicationsActivity(View view) {
//        Intent intent = new Intent(this, DataBaseTestActivity.MedicationsActivity.class);
//        startActivity(intent);
//    }

    private void updateUI(String displayName, String email) {
        TextView textViewDisplayName = (TextView) findViewById(R.id.textView_name);
        TextView textViewEmail = (TextView) findViewById(R.id.textView_email);

        textViewDisplayName.setText("Name: " + displayName);
        textViewEmail.setText("Email: " + email);
    }
}
