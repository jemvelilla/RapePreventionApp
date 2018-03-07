package com.build1.rapepreventionapp.PushNotif;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Bluno.BlunoMain;
import com.build1.rapepreventionapp.GooglePlacesAPI.DirectionsParser;
import com.build1.rapepreventionapp.GooglePlacesAPI.GetNearbyPlacesData;
import com.build1.rapepreventionapp.Model.EditInformation;
import com.build1.rapepreventionapp.PushNotif.LocationTracking;
import com.build1.rapepreventionapp.Model.PlaceInfo;
import com.build1.rapepreventionapp.R;
import com.build1.rapepreventionapp.Registration.RegisterStep3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

/**
 * Created by JEMYLA VELILLA on 11/02/2018.
 */

public class LocationTracking extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, DirectionFinderListener, LocationListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    TextView name, location, eta, kms_away;
    private CircleImageView mProfilePicture;

    private static final String TAG = "Map";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    Location currentLocation;
    LatLng currentLocationLatlng;
    LatLng victimLocation;
    private String addressVictim;
    List<Address> addresses;
    List<Address> victimAddress;
    ArrayList<LatLng> listPoints;
    int PROXIMITY_RADIUS = 1000;
    double latitude,longtitude;
    String latitudeDb, longitudeDb;
    double latDB, lonDB;
    LatLng origin, destination;

    GoogleMap mMap;
    private FirebaseFirestore mFirestore;
    String dataFrom;
    private Location mylocation;

    private FirebaseAuth mAuth;
    private String mCurrentId;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    String current_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracking);
        getLocationPermission();


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance(); //instantiate firestore
        mCurrentId = FirebaseAuth.getInstance().getUid();
        current_id = mAuth.getCurrentUser().getUid();

        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (getIntent().getStringExtra("message") != null
                && getIntent().getStringExtra("from_user_id") != null){
            String dataMessage = getIntent().getStringExtra("message");
            dataFrom = getIntent().getStringExtra("from_user_id");
        } else {
            if (Intent.ACTION_VIEW.equals(action)) {
                final List<String> segments = intent.getData().getPathSegments();
                for (int i=0; i< segments.size(); i++) {
                    dataFrom = segments.get(i);
                }
            }
        }
        getDeviceLocation2();


        name = (TextView) findViewById(R.id.name);
        location = (TextView) findViewById(R.id.location);
        mProfilePicture = (CircleImageView) findViewById(R.id.profilePicture);

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        listPoints =new ArrayList<>();
    }

    public void onStart(){
        super.onStart();
        mFirestore.collection("Users").document(dataFrom).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                RequestOptions placeHolderOptions = new RequestOptions();
                placeHolderOptions.placeholder(R.drawable.default_profile);

                Glide.with(getApplicationContext()).setDefaultRequestOptions(placeHolderOptions).load(documentSnapshot.getString("image")).into(mProfilePicture);

                name.setText(documentSnapshot.getString("first_name") + " " + documentSnapshot.getString("last_name"));
                double latitude = Double.parseDouble(documentSnapshot.get("latitude").toString());
                double longitude = Double.parseDouble(documentSnapshot.get("longitude").toString());
                latDB = latitude;
                lonDB = longitude;
                listPoints.add(new LatLng(latitude,longitude));

                DocumentReference contactListener = mFirestore.collection("Users").document(dataFrom);
                contactListener.addSnapshotListener(new EventListener< DocumentSnapshot >() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("ERROR", e.getMessage());
                            return;
                        }
                        if (documentSnapshot != null && documentSnapshot.exists()) {

                            if (destinationMarkers.size() == 1){
                                destinationMarkers.remove(0);
                            }else {
                                double latitude = Double.parseDouble(documentSnapshot.get("latitude").toString());
                                double longitude = Double.parseDouble(documentSnapshot.get("longitude").toString());
                                latDB = latitude;
                                lonDB = longitude;
                                listPoints.set(1, new LatLng(latitude,longitude));
                                location.setText(getLocation(latitude, longitude));
                                sendRequest();
                            }


                        }
                    }
                });

                sendRequest();

            }
        });
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        try {
            if (mLocationPermissionGranted) {
                final com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "onComplete: found location");
                            currentLocation = new Location(task.getResult());
                            latitude = currentLocation.getLatitude();

                            longtitude = currentLocation.getLongitude();
                            listPoints.add(new LatLng(latitude,longtitude));
                            currentLocationLatlng = new LatLng(latitude,longtitude);
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 2, LocationTracking.this);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 2, LocationTracking.this);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), longtitude,1);

                                String address = addresses.get(0).getAddressLine(0);

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

    private void getDeviceLocation2(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{

            if (mLocationPermissionGranted) {

                final com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull final com.google.android.gms.tasks.Task<Location> task) {
                        //final String placeId = task.getPlaceId();
                        if (task.isSuccessful()) {
                            currentLocation = new Location(task.getResult());
                            latitudeDb = String.valueOf(currentLocation.getLatitude());

                            longitudeDb = String.valueOf(currentLocation.getLongitude());

                            Map<String, Object> locationMap = new HashMap<>();
                            locationMap.put("latitude", latitudeDb);
                            locationMap.put("longitude", longitudeDb);

                            mFirestore.collection("Users").document(current_id).update(locationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("message", "location stored.");
                                }
                            });

                        }

                    }
                });
            } else {
            }
        }catch(SecurityException e)
        {
            Log.e(TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            getDeviceLocation2();
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
    }

    public void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(getApplicationContext(),COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                //initMap();
            }else{
                ActivityCompat.requestPermissions(this,permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
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
                            mMap.setMyLocationEnabled(true);
                            Log.d(TAG, "onRequestPermissionsResult: failes");
                            return;
                        }
                    }Log.d(TAG, "onRequestPermissionsResult: granted");


                    mLocationPermissionGranted = true;
                    //initMap();
                }
                break;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mylocation = location;
        if (mylocation != null) {
            double latitudeDb = location.getLatitude();
            double longitudeDb = location.getLongitude();

            listPoints.set(0, new LatLng(latitudeDb,longitudeDb));

            Map<String, Object> locationMap = new HashMap<>();
            locationMap.put("latitude", String.valueOf(latitudeDb));
            locationMap.put("longitude", String.valueOf(longitudeDb));

            mFirestore.collection("Users").document(current_id).update(locationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Log.d("pangdebug", "location stored.");
                }
            });
        } else{
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {


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
        Toast.makeText(getApplicationContext(), "Showing Nearby Plice Station", Toast.LENGTH_SHORT);
        Log.e(TAG, "getNearbyPoliceStation: asasa" +url);
    }

    private String getUrl(double latitude , double longtitude, String nearbyPlace){

        Log.e(TAG, "getUrlaaaaa: "+latitude );
        Log.e(TAG, "getUrlbbbbb: "+longtitude );
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

    private String getLocation(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            victimAddress = geocoder.getFromLocation(latitude, longitude, 1);

            addressVictim = victimAddress.get(0).getAddressLine(0);
            Log.e(TAG, "onComplete: "+victimAddress );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressVictim;
    }

    public void btnOnClickViewProfile(View v) {

        Intent i = new Intent(getApplicationContext(), ViewProfile.class);
        i.putExtra("user_id", dataFrom);
        startActivity(i);

    }

    public void sendRequest() {
        if (listPoints.size() == 2){
            destination = listPoints.get(1);
            origin = listPoints.get(0);
        }
        else{
        }


        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {



        for (Route route : routes) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(route.startLocation);
            builder.include(route.endLocation);
            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int padding = (int) (width*0.2);


            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 18));
            ((TextView) findViewById(R.id.eta)).setText(route.duration.text);
            ((TextView) findViewById(R.id.kmsAway)).setText(route.distance.text);
            ((TextView) findViewById(R.id.location)).setText(route.endAddress);
            //location.setText(getLocation(route.endAddress.latitude,route.endLocation.longitude));
            Log.d(TAG, "onDirectionFinderSuccesss: "+destinationMarkers);


            if (originMarkers.size() == 1){
                originMarkers.remove(routes);
            }


            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));

            if (destinationMarkers.size() == 1){
                destinationMarkers.remove(routes);
            }

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            victimLocation = route.endLocation;




            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.MAGENTA).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
