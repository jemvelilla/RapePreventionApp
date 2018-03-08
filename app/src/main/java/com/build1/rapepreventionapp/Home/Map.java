package com.build1.rapepreventionapp.Home;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
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

import com.build1.rapepreventionapp.GooglePlacesAPI.GetNearbyPlacesData;
import com.build1.rapepreventionapp.Model.PlaceInfo;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private static final String TAG = "Map";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    Location currentLocation;
    LatLng currentLocationLatlng;
    private final float DEFAULT_ZOOM = 16f;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private ImageButton mInfo;
    List<Address> addresses;
    List<Address> victimAddress;
    ArrayList<LatLng> listPoints;
    int PROXIMITY_RADIUS = 1000;
    double latitude,longtitude;

    GoogleMap mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocationPermission();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home,container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        listPoints =new ArrayList<>();

        double latitude;
        double longitude;
    }

    public void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: getting the device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());

        try {
            if (mLocationPermissionGranted) {
                final com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        if (task.isSuccessful()) {

                            currentLocation = new Location(task.getResult());
                            currentLocation.getAccuracy();
                            Log.d("location accuracy", "onComplete: " +currentLocation.getAccuracy());

                            latitude = currentLocation.getLatitude();
                            longtitude = currentLocation.getLongitude();
                            currentLocationLatlng = new LatLng(latitude,longtitude);

                            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                            try {
                                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), longtitude,1);

                                String address = addresses.get(0).getAddressLine(0);

                                moveCamera(new LatLng(latitude, longtitude),
                                        DEFAULT_ZOOM, ""+address+"");

                                getNearbyPoliceStation();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to :");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    public void getLocationPermission(){

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            }else{
                ActivityCompat.requestPermissions(getActivity(),permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            mMap.setMyLocationEnabled(true);

                            return;
                        }
                    }

                    mLocationPermissionGranted = true;
                }
                break;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Log.d(TAG, "onProviderDisabled: LOCATION IS OFF");

    }

    public void getNearbyPoliceStation(){

        latitude = currentLocationLatlng.latitude;
        longtitude = currentLocationLatlng.longitude;
        String police = "police";
        String url = getUrl(latitude, longtitude, police);
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(getActivity().getApplicationContext(), "Showing Nearby Police Station", Toast.LENGTH_SHORT);
        Log.e(TAG, "getNearbyPoliceStation: asasa" +url);
    }

    private String getUrl(double latitude , double longtitude, String nearbyPlace){

         StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
         googlePlaceUrl.append("location="+latitude+","+longtitude);
         googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
         googlePlaceUrl.append("&type="+nearbyPlace);
         googlePlaceUrl.append("&sensor=false");
         googlePlaceUrl.append("&key="+"AIzaSyCuTUpBYWXg3DeUUwecjW4NuACBl3SFueE");
        Log.e(TAG, "getUrl: "+googlePlaceUrl.toString());
        Log.e(TAG, "getUrl: " +latitude );
        Log.e(TAG, "getUrl: " +longtitude );

         return googlePlaceUrl.toString();
    }
}