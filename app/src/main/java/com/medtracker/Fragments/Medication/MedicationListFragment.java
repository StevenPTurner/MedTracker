package com.medtracker.Fragments.Medication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medtracker.Models.Medication;
import com.medtracker.Utilities.Adapters.MedicationAdapter;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
//used to list fragments and some basic details that the user can click to get more info
//mainly used to house a listView
public class MedicationListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MedicationListFragment";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;

    private ArrayList<Medication> medications = new ArrayList<>();
    private ListView listView;
    private Button addMedication;
    private MedicationAdapter adapter;


    public MedicationListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medication_list, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "loadedFragment");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUID = mFirebaseUser.getUid();
        Log.d(TAG, mFirebaseUser.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("medications").child(userUID);
        listView = (ListView) getView().findViewById(R.id.listView);

        addMedication = (Button) getView().findViewById(R.id.button_add_medication);
        addMedication.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment newFragment = new MedicationAddFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);
                Log.d(TAG, "starting add medications fragment");
                transaction.commit();
            }
        });

        if(listView == null) {
            Log.d(TAG, "ListView is null");
        } else {
            Log.d(TAG, "ListVIew is not null");
        }

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new MedicationAdapter
                (getActivity().getApplicationContext(),
                medications);
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (getActivity().getApplicationContext(),
//                        R.adapter_item_alarm_manager.list_item_medication,
//                        medicationID);

        listView.setAdapter(adapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Medication medication = dataSnapshot.getValue(Medication.class);
//                String medicationInfo = "Name: " + medication.getMedication_name() + "\n" +
//                        "Info: " + medication.getInstructions() + "\n" +
//                        "Dose: " + medication.getDosage() + "mg";
                medications.add(medication);
                //medicationID.add(medicationInfo);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                Medication medication = dataSnapshot.getValue(Medication.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                Medication medication = dataSnapshot.getValue(Medication.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "MedicationsActivity:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addChildEventListener(childEventListener);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.d(TAG, "starting edit medications fragment");
        Fragment medicationEditFragment = new MedicationEditFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle args = new Bundle();

        Medication current = adapter.getItem(position);
        String medicationKey = Utility.nameToKey(current.getMedication_name());

        args.putString("medicationKey", medicationKey);
        args.putString("medicationName", current.getMedication_name());
        args.putString("medicationDose", Integer.toString(current.getDosage()));
        args.putString("medicationInstructions", current.getInstructions());

        medicationEditFragment.setArguments(args);
        transaction.replace(R.id.content_frame, medicationEditFragment);
        transaction.addToBackStack(null);
        Log.d(TAG, "starting edit medications fragment");
        transaction.commit();
    }



    //when the user returns to this fragment from another
    @Override
    public void onResume() {
        Log.d(TAG, "Fragment resumed");
        adapter.clear();
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
