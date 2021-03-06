package com.medtracker.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medtracker.Fragments.AboutFragment;
import com.medtracker.Fragments.Alarm.AlarmManagerListFragment;
import com.medtracker.Fragments.Medication.MedicationListFragment;
import com.medtracker.Fragments.Pharmacy.PharmacyMapFragment;
import com.medtracker.Fragments.RecordListFragment;
import com.medtracker.Fragments.StatisticsFragment;
import com.medtracker.Models.User;
import com.medtracker.Utilities.Convert;
import com.medtracker.Utilities.Factory;
import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

//Home activity used to switch between fragments that contain functionality, also used to manage
//the navigation bar and navigation drawer along with action bar.

//These docs were used to help the creation of this app:
//  https://developer.android.com/training/implementing-navigation/nav-drawer.html
public class HomeActivity extends FragmentActivity {
    private final String TAG = LogTag.homeActivity;

    //user details objects & database
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String userUID;

    //navigation bar
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerLinearLayout;

    //Used to hold titles of menu items
    private String[] mMenuTitles;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //get instance of firebase user and their ID for database calls and set up database object
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUID = mFirebaseUser.getUid();
        Log.w(TAG, "User Uid: " + userUID);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);

        //set up event listener to detect user account changes
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if it exists pull data and update side bar
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    updateSideBar(user.getDisplay_name(),user.getEmail());
                } else {
                    //if not create user account and update side bar
                    Intent lastIntent = getIntent();
                    User user = Factory.user(mFirebaseUser.getEmail(),
                            lastIntent.getStringExtra("displayName"));
                    mDatabase.setValue(user);
                    updateSideBar(user.getDisplay_name(),user.getEmail());
                    Log.w(TAG, "new user " + userUID + "created");
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        };

        //setting up navigation drawer objects
        mTitle = mDrawerTitle = getTitle();
        mMenuTitles = getResources().getStringArray(R.array.menu_titles_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDatabase.addListenerForSingleValueEvent(userListener);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinearLayout);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the fragment to show
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new MedicationListFragment();
                Log.w(TAG, "Medications fragment selected");
                callFragment(fragment,position);
                break;
            case 1:
                fragment = new AlarmManagerListFragment();
                Log.w(TAG, "Alarms fragment selected");
                Log.w(TAG, Convert.keyToName("sodium_valproate"));
                callFragment(fragment,position);
                break;
            case 2:
                fragment = new RecordListFragment();
                Log.w(TAG, "Records fragment selected");
                callFragment(fragment,position);
                break;
            case 3:
                fragment = new PharmacyMapFragment();
                Log.w(TAG, "Pharmacy map fragment selected");
                callFragment(fragment,position);
                break;
            case 4:
                fragment = new StatisticsFragment();
                Log.w(TAG, "Statistics fragment selected");
                callFragment(fragment,position);
                break;
            case 5:
                fragment = new AboutFragment();
                Log.w(TAG, "About fragment selected");
                callFragment(fragment,position);
                break;
            case 6:
                FirebaseAuth.getInstance().signOut();
                Intent startScreenIntent = new Intent(this,StartActivity.class);
                startActivity(startScreenIntent);
                Toast toast = Toast.makeText(this, "You have been signed out", Toast.LENGTH_SHORT);
                toast.show();
                Log.w(TAG, "userSignedOut");
                break;
        }


    }

    public void callFragment(Fragment fragment, int position){
        FragmentManager fragmentManager = getFragmentManager();
        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerLinearLayout);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //updates sidebar display
    private void updateSideBar(String displayName, String email) {
        displayName = Convert.keyToName(displayName);
        TextView textViewDisplayName = (TextView) findViewById(R.id.text_display_name);
        TextView textViewEmail = (TextView) findViewById(R.id.text_email);

        textViewDisplayName.setText(displayName);
        textViewEmail.setText(email);
    }

    //used to overall manage back stack of fragments propperly
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
