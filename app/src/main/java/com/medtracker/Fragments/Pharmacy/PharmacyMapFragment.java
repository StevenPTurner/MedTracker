package com.medtracker.Fragments.Pharmacy;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.medtracker.Models.Pharmacy;
import com.medtracker.Utilities.LogTag;
import com.medtracker.medtracker.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Fragment used to display a map of the current location with the nearest pharmacies and other
 * nearby pharmacies
 *
 * References -------------------------------------------------------------------------------------/
 *  https://github.com/googlemaps/android-samples/blob/master/tutorials/CurrentPlaceDetailsOnMap/app
 *  /src/main/java/com/example/currentplacedetailsonmap/MapsActivityCurrentPlace.java#
 *  https://developers.google.com/maps/documentation/android-api/current-place-tutorial
 *  https://developers.google.com/maps/documentation/android-api/hiding-features
 */
public class PharmacyMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    //static/default values for settings
    private static final String TAG = LogTag.pharmacyLogFragment;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private final LatLng mDefaultLocation = new LatLng(56.463190, -3.038596 );

    //used objects
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private GoogleApiClient mGoogleApiClient;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;

    private ArrayList<Pharmacy> pharmacies = new ArrayList<>();

    public PharmacyMapFragment() {}

    @Override //generates layout
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pharmacy_map, container, false);
    }

    @Override //setup method
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //used for smoother transitions if this is not the first state
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        //set up api client for api calls
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage((FragmentActivity) getActivity(), this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        updateLocationUI(); //updates the UI, called first in case of previous sessions
        getDeviceLocation(); //get current device location
        getPharmaciesFromAPI(); //gets pharmacies from google places search api

    }

    //Used to update the UI
    private void updateLocationUI() {
        if (mMap == null) {  //if map is null exit method
            return;
        }

        //if we have locations permission set permission to true
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        //if we have location permissions update the ui
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void getDeviceLocation() {
        //Get location permissions and try to locate current position
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Toast.makeText(getActivity(), "Cannot get location", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override //used when the premission result comes back
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    //gets list of pharmacies as a json object from the api
    public void getPharmaciesFromAPI(){
        double lat,lng;

        if(mLastKnownLocation != null) {
            lat = mLastKnownLocation.getLatitude();
            lng = mLastKnownLocation.getLongitude();
        } else {
            lat = mDefaultLocation.latitude;
            lng = mDefaultLocation.longitude;
        }

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String API_KEY = getString(R.string.API_KEY);
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=" + lat + "," + lng + "&rankby=distance&type=pharmacy&key=" + API_KEY;

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSON(response); //parse the json response into pharmacy objects
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                Toast.makeText(getActivity(), "Cannot connect to the internet", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }

    //parses the json
    private void parseJSON(JSONObject response){
        try {
            JSONArray results = response.getJSONArray("results");
            pharmacies.clear();

            for(int i=0; i<results.length();i++) {
                Pharmacy pharmacy = new Pharmacy();
                JSONObject current = results.getJSONObject(i);
                JSONObject location = current.getJSONObject("geometry").getJSONObject("location");

                pharmacy.setLat(location.getDouble("lat"));
                pharmacy.setLng(location.getDouble("lng"));
                pharmacy.setName(current.getString("name"));
                pharmacy.setInfo(current.getString("vicinity"));
                pharmacies.add(pharmacy);
            }
            addMapMarkers(pharmacies);
        }catch(Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    //adds markers for pharmacies onto the map
    private void addMapMarkers(ArrayList<Pharmacy> pharmacies) {
        for(int i=0;i<pharmacies.size();i++) {
            double lat = pharmacies.get(i).getLat();
            double lng = pharmacies.get(i).getLng();
            String title = pharmacies.get(i).getName();

            if(i==0) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.
                                HUE_AZURE)));
            } else {
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title));
            }
        }
        showDialog(pharmacies.get(0));
    }

    private void showDialog(Pharmacy pharmacy) {
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("name", pharmacy.getName());
        args.putString("info", pharmacy.getInfo());

        DialogFragment nearestPharmacy = new NearestPharmacyFragment();
        nearestPharmacy.setArguments(args);
        nearestPharmacy.show(getFragmentManager().beginTransaction(), "dialog");
    }

    /*
    *  API and connection call methods
    */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override // Build the map.
    public void onConnected(Bundle connectionHint) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MapFragment fragment = new MapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();
        fragment.getMapAsync(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended. Error code: " + cause);
    }

}