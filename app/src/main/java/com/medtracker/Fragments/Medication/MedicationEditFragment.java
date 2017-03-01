package com.medtracker.Fragments.Medication;


import android.app.Fragment;
import android.app.FragmentTransaction;
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
import com.medtracker.Models.Medication;
import com.medtracker.medtracker.R;

/**
 * A simple {@link Fragment} subclass.
 * used to edit the clicked medication
 */
public class MedicationEditFragment extends Fragment {
    private static final String TAG = "LogMedicationEditFrag";

    //medication info
    private String medicationKey;
    private String medicationName;
    private String medicationDose;
    private String medicationInstructions;

    //user and database variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private String userUID;

    //input fields
    private EditText editName;
    private EditText editDose;
    private EditText editInstructions;
    private Button applyEdit;
    private Button deleteMedication;

    public MedicationEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //gathers medication info and sets it
        Bundle args = getArguments();
        medicationKey = args.getString("medicationKey");
        medicationName = args.getString("medicationName");
        medicationDose = args.getString("medicationDose");
        medicationInstructions = args.getString("medicationInstructions");
        Log.d(TAG, "ID: " + medicationKey);
        Log.d(TAG, medicationName);
        Log.d(TAG, medicationDose);
        Log.d(TAG, medicationInstructions);

        // Inflate the adapter_item_alarm_manager for this fragment
        return inflater.inflate(R.layout.fragment_medication_edit, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "loadedFragment");
        //Get logged in user info
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userUID = firebaseUser.getUid();
        Log.d(TAG, firebaseUser.getUid());
        //get database object
        database = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "Setup complete");

        //setting up input fields
        editName = (EditText) getView().findViewById(R.id.edit_name);
        editDose = (EditText) getView().findViewById(R.id.edit_dose);
        editInstructions = (EditText) getView().findViewById(R.id.edit_instructions);

        setupCurrentValues();
        //button listener to preform actions when pressed
        applyEdit = (Button) getView().findViewById(R.id.button_edit_medication);
        applyEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "apply edit button pressed");
                updateDatabase(buildMedication(), medicationKey);
                returnToList();
            }
        });

        deleteMedication = (Button) getView().findViewById(R.id.button_delete_medication);
        deleteMedication.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "delete medication button pressed");
                deleteMedicationPressed(medicationKey);
                returnToList();
            }
        });
    }


    //used to setup current values
    private void setupCurrentValues() {
        editName.setText(medicationName);
        editDose.setText(medicationDose);
        editInstructions.setText(medicationInstructions);

        Log.d(TAG, editName.getText().toString());
        Log.d(TAG, editDose.getText().toString());
        Log.d(TAG, editInstructions.getText().toString());
    }

    //gathers all the user input and builds a medication object with it
    private Medication buildMedication() {
        //formats it correctly
        String medicationName = editName.getText().toString();
        String medicationDoseText = editDose.getText().toString();
        String medicationInstructions = editInstructions.getText().toString();
        int medicationDoseValue = Integer.parseInt(medicationDoseText);

        //builds and returns user object
        Medication medication = new Medication(
                medicationInstructions, medicationName, false, false, medicationDoseValue);
        Log.d(TAG, "Medication has been built");
        return medication;
    }

    //updates medication in database
    private void updateDatabase(Medication medication, String medicationKey) {
        Log.d(TAG, "Database Key:" + medicationKey);
        database.child("medications").child(userUID).child(medicationKey).setValue(medication);
        Log.d(TAG, "Medication updated in the database");
    }

    //deletes medication
    private void deleteMedication(String medicationKey) {
        Log.d(TAG, "Medication Key:" + medicationKey);
        database.child("medications").child(userUID).child(medicationKey).removeValue();
        Log.d(TAG, "Medication deleted from the database");
    }

    //deletes alarm manager
    private void deleteAlarmManager(String medicationKey) {
        Log.d(TAG, "Alarm manager key: " + medicationKey);
        database.child("alarm_manager").child(userUID).child(medicationKey).removeValue();
        Log.d(TAG, "Alarm manager key: " + medicationKey);
    }

    //deletes all the alarms
    private void deleteAlarms(String medicationKey) {
        Log.d(TAG, "Alarm key: " + medicationKey);
        database.child("alarms").child(userUID).child(medicationKey).removeValue();
        Log.d(TAG, "Alarm key: " + medicationKey);
    }

    //compisite method
    private void deleteMedicationPressed(String medicationKey) {
        deleteMedication(medicationKey);
        deleteAlarmManager(medicationKey);
        deleteAlarms(medicationKey);
    }

    //returns to previous fragment
    private void returnToList() {
        MedicationListFragment medicationListFragment = new MedicationListFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Log.d(TAG, "returning to list fragment");
        transaction.replace(R.id.content_frame, medicationListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
