package com.medtracker.Fragments.Medication;


import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medtracker.Models.AlarmManager;
import com.medtracker.Models.Medication;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

/**
 * A simple {@link Fragment} subclass.
 * user for adding new medications to the database
 */
public class MedicationAddFragment extends Fragment {
    private static final String TAG = "MedicationAddFragment";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;
    private Button saveMedication;

    public MedicationAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the adapter_item_alarm_manager for this fragment
        return inflater.inflate(R.layout.fragment_medication_add, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "loadedFragment");
        //Get logged in user info
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUID = mFirebaseUser.getUid();
        Log.d(TAG, mFirebaseUser.getUid());
        //get database object
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "Setup complete");

        //button listener to preform actions when pressed
        saveMedication = (Button) getView().findViewById(R.id.button_save_medication);
        saveMedication.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Log.d(TAG, "Save medication button pressed");
            addToDatabase(buildMedication());
            getFragmentManager().popBackStackImmediate();
            }
        });
    }

    //gathers all the user input and builds a medication object with it
    private Medication buildMedication() {
        //gathers user input
        EditText editName = (EditText) getView().findViewById(R.id.edit_name);
        EditText editDose = (EditText) getView().findViewById(R.id.edit_dose);
        EditText editInstructions = (EditText) getView().findViewById(R.id.edit_instructions);

        //formats it correctly
        String medicationName = editName.getText().toString();
        String medicationDoseText = editDose.getText().toString();
        String medicationInstructions = editInstructions.getText().toString();
        int medicationDoseValue = Integer.parseInt(medicationDoseText);

        //builds and returns user object
        Medication medication = new Medication(
                medicationInstructions,
                medicationName,
                false,
                false,
                medicationDoseValue);
        Log.d(TAG, "Medication has been built");
        return medication;
    }

    private AlarmManager getNewManager(String medicationKey) {
        AlarmManager alarmManager = new AlarmManager(medicationKey, false, "none", 0, 0);
        return alarmManager;
    }

    //adds medication to database
    private void addToDatabase(Medication medication) {
        //correct json formatting for keys
        String medicationKey = Utility.nameToKey(medication.getMedication_name());
        Log.d(TAG, "Database Key:" + medicationKey);
        mDatabase.child("medications").child(userUID).child(medicationKey).setValue(medication);
        Log.d(TAG, "Medication added to database");
        mDatabase.child("alarm_manager").child(userUID).child(medicationKey)
                .setValue(getNewManager(medicationKey));
        Log.d(TAG, "Alarm manager added to database");

    }

}