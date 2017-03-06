package com.medtracker.Fragments;

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
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

    //used objects
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private boolean locationPermission;
    private Location lastLocation;
    private CameraPosition cameraPosition;
    private ArrayList<Pharmacy> pharmacies = new ArrayList<>();

    public PharmacyMapFragment() {/* Required empty public constructor */}

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
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            Log.d(TAG,"Previous state loaded");
        }

        //set up api client for api calls
        googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .enableAutoManage((FragmentActivity) getActivity(), this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();
        Log.d(TAG,"API client created");

        //used to enable the use of a custom map marker
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MapFragment fragment = new MapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();
    }

    @Override //when map is read to be loaded
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        updateLocationUI(); //updates the UI, called first in case of previous sessions
        getDeviceLocation(); //get current device location
        //gets pharmacies from google places search api
        getPharmaciesFromAPI(lastLocation.getLatitude(), lastLocation.getLongitude());
    }

    //Used to update the UI
    private void updateLocationUI() {
        if (map == null) { //if map is null exit method
            return;
        }

        //if we have locations permission set permission to true
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermission = true;
            Log.d(TAG, "permission set to true");
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.d(TAG, "permission set to false");
        }

        //if we have location permissions update the ui
        if (locationPermission) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            lastLocation = null;
        }
    }

    //used to get hte device's current location
    private void getDeviceLocation() {
        //Get location permissions and try to locate current position
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                 android.Manifest.permission.ACCESS_COARSE_LOCATION )
                == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.d(TAG,"Lat: "+lastLocation.getLatitude()+" Lng: "+lastLocation.getLongitude());
        }

        // Set the map's camera position to the current location of the device.
        if (cameraPosition != null) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else if (lastLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastLocation.getLatitude(),
                            lastLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override //used when the premission result comes back
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationPermission = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = true;
                }
            }
        }
        updateLocationUI();
    }

    //gets list of pharmacies as a json object from the api
    public void getPharmaciesFromAPI(double lat, double lng){
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
                        addMapMarkers(pharmacies);
                        showDialog(pharmacies.get(0));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
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
                map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.
                                HUE_AZURE)));
            } else {
                map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title));
            }
        }
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
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastLocation);
            super.onSaveInstanceState(outState);
        }
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

    @Override // Build the map.
    public void onConnected(Bundle connectionHint) {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

}