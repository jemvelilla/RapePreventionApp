package com.build1.rapepreventionapp;


import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;

import com.build1.rapepreventionapp.Model.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by Darise on 24/01/2018.
 */

public class Map extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private static final String TAG = "Map";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private final float DEFAULT_ZOOM = 16f;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private ImageButton mInfo;
    List<Address> addresses;

    GoogleMap mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //fragment.getMapAsync(this);
        // Construct a GeoDataClient.
        //mGeoDataClient = Places.getGeoDataClient(getActivity().getApplicationContext(), null);

        // Construct a PlaceDetectionClient.
        //mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity().getApplicationContext(), null);

        // Construct a FusedLocationProviderClient.
        //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        //SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //fragment.getMapAsync(this);
        //Log.e(TAG, "onViewCreated: asasa" );
        getLocationPermission();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home,container, false);

        //View v = inflater.inflate(R.layout.activity_home, container, false);

        //mInfo = (ImageButton) v.findViewbyId(R.id.place_info);

        //ImageButton mInfo = (ImageButton) v.findViewById(R.id.place_info);
        //mInfo.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {
        //        try {
        //            if (mMarker.isInfoWindowShown()){
        //                mMarker.hideInfoWindow();
        //            }else{
        //                mMarker.showInfoWindow();
        //            }

        //        }catch (NullPointerException e){
        //            Log.e(TAG, "onClick: NULLPOINTEREXCEPTION" + e.getMessage());
        //        }
        //    }
        //});

        //return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    /*private void init(){

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity().getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();


    }*/

    /*private void initMap() {
        Log.d(TAG, "initMap: initmap");
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }*/

    private void geoLocate(){
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());

    }

    private void geoDataClient(){
        mGeoDataClient = Places.getGeoDataClient(getActivity().getApplicationContext(), null);
    }

    private void placeDetectionClient() {
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity().getApplicationContext(), null);
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());

        try {
            if (mLocationPermissionGranted) {
                final com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        //final String placeId = task.getPlaceId();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                            try {
                                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(),1);

                                String address = addresses.get(0).getAddressLine(0);
                                //String area = addresses.get(0).getLocality();
                                Log.e(TAG, "onComplete: "+addresses );

                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM, ""+address+"");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e(TAG, "onComplete: " +currentLocation);

                            //final String placeId = task.getResult();




                        } else {
                            Log.d(TAG, "onComplete: NASAN");

                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }
    }

    /*private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: moving the camera to :");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();
        mMap.setInfoWindowAdapter(new CustomWindowInfoAdapter(getActivity().getApplicationContext()));

        if (placeInfo != null){
            try {
                String snippet = "Address: " +placeInfo.getAddress()+ "\n" +"";
            MarkerOptions options = new MarkerOptions().position(latLng).title(placeInfo.getName()).snippet(snippet);
            mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: " );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }*/

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to :");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //if (!title.equals("You are here!")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        //}

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //BottomNavigation bottomNavigation = new BottomNavigation();
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            //initMap();


        }

        Log.d(TAG, "onMapReady: Map is Ready");
        }

        public void getLocationPermission(){
            Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                //initMap();
            }else{
                ActivityCompat.requestPermissions(getActivity(),permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        Log.d(TAG, "onRequestPermissionsResult: called");
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: failes");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: granted");
                    mLocationPermissionGranted = true;
                    //initMap();
                }
            }
        }
    }

    /*private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            if (!places.getStatus().isSuccess()){
                places.release();
                return;
            }

            final Place place = places.get(0);
            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setId(place.getId());
                mPlace.setLatLng(place.getLatLng());

            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NULLPOINTEREXCEPTION" + e.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude),DEFAULT_ZOOM, mPlace.getName());
            places.release();
        }
    };*/

        ////////////////////////////////////////////





}



