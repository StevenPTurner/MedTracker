package com.medtracker.Fragments;


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
import com.medtracker.Models.Medication;
import com.medtracker.Utilities.Utility;
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
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;

    //input fields
    private EditText editName;
    private EditText editDose;
    private EditText editInstructions;
    private Button applyEdit;

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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medication_edit, container, false);
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
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("medications").child(userUID);
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
                getFragmentManager().popBackStackImmediate();
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
                medicationInstructions, medicationName, false, medicationDoseValue);
        Log.d(TAG, "Medication has been built");
        return medication;
    }

    //updates medication in database
    private void updateDatabase(Medication medication, String medicationID) {
        Log.d(TAG, "Database Key:" + medicationKey);
        mDatabase.child(medicationKey).setValue(medication);
        Log.d(TAG, "Medication updated in the database");
    }


}
