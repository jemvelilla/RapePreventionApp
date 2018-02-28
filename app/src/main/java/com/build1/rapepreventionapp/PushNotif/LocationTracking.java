package com.build1.rapepreventionapp.PushNotif;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

import de.hdodenhof.circleimageview.CircleImageView;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

/**
 * Created by JEMYLA VELILLA on 11/02/2018.
 */

public class LocationTracking extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, DirectionFinderListener {

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
    private GeoDataClient mGeoDataClient;
    Location currentLocation;
    LatLng currentLocationLatlng;
    LatLng victimLocation;
    private PlaceDetectionClient mPlaceDetectionClient;
    private final float DEFAULT_ZOOM = 16f;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private ImageButton mInfo;
    private String addressVictim;
    List<Address> addresses;
    List<Address> victimAddress;
    ArrayList<LatLng> listPoints;
    int PROXIMITY_RADIUS = 1000;
    double latitude,longtitude;
    double latDB, lonDB;
    LatLng origin, destination;

    GoogleMap mMap;
    private FirebaseFirestore mFirestore;
    String dataFrom;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracking);
        getLocationPermission();

        mFirestore = FirebaseFirestore.getInstance();

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


        name = (TextView) findViewById(R.id.name);
        location = (TextView) findViewById(R.id.location);
        //eta = (TextView) findViewById(R.id.eta);
        //kms_away = (TextView) findViewById(R.id.kmsAway);
        mProfilePicture = (CircleImageView) findViewById(R.id.profilePicture);

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        listPoints =new ArrayList<>();

        sendRequest();
        Log.d(TAG, "onCreate: pumasok ba?");

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
                double latitude = Double.parseDouble(documentSnapshot.getString("latitude"));
                double longitude = Double.parseDouble(documentSnapshot.getString("longitude"));
                latDB = 14.612584;
                lonDB = 120.98239;
                location.setText(getLocation(latitude, longitude));
                victimLocation = new LatLng(latDB,lonDB);
                Log.d(TAG, "onSuccess:lat " +latitude);
                Log.d(TAG, "onSuccess:lon " +longitude);
                Log.d(TAG, "onSuccess: " +victimLocation);

            }
        });
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
        Geocoder geocoder = new Geocoder(getApplicationContext());

    }

    private void geoDataClient(){
        mGeoDataClient = Places.getGeoDataClient(getApplicationContext(), null);
    }

    private void placeDetectionClient() {
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getApplicationContext(), null);
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        //latitude = location.getLatitude();
        //longtitude = location.getLongitude();
        //lastlocation = location;

        try {
            if (mLocationPermissionGranted) {
                final com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        //final String placeId = task.getPlaceId();
                        if (task.isSuccessful()) {

                            Log.d(TAG, "onComplete: found location");
                            currentLocation = new Location(task.getResult());
                            latitude = currentLocation.getLatitude();

                            longtitude = currentLocation.getLongitude();
                            currentLocationLatlng = new LatLng(latitude,longtitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), longtitude,1);

                                String address = addresses.get(0).getAddressLine(0);
                                //String area = addresses.get(0).getLocality();
                                Log.e(TAG, "onComplete: "+addresses );
                                listPoints.add(new LatLng(latitude, longtitude));

                                moveCamera(new LatLng(latitude, longtitude),
                                        DEFAULT_ZOOM, ""+address+"");

                                getNearbyPoliceStation();



                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e(TAG, "onComplete: " +latitude);
                            Log.e(TAG, "onComplete: " +longtitude);

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
            //if (currentLocationLatlng != null){
            //    getNearbyPoliceStation();
            //}


            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void twoPointsMarker(LatLng latLng) {

    }




    private String getRequestUrl(LatLng origin, LatLng destination) {
        //value of origin
        String stringOrigin = "origin=" +origin.latitude+ "," +origin.longitude;
        Log.e(TAG, "getRequestUrl: "+origin );
        //value of destination
        String stringDestination = "destination="+destination.latitude+ ","+destination.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = stringOrigin +"&" + stringDestination + "&" +sensor+ "&" +mode;
        // output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" +output + "?" +param;
        Log.e(TAG, "getRequestUrl: "+url );
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);

            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;

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




    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //
            LocationTracking.TaskParser taskParser = new LocationTracking.TaskParser();
            taskParser.execute(s);
        }
    }
    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it sa map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists){
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path){
                    double lat  = Double.parseDouble(point.get("lat"));
                    double lon  = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));

                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);


            }
            if (polylineOptions!=null){
                mMap.addPolyline(polylineOptions);

            }else{
                Toast.makeText(getApplicationContext(), "DIRECTION NOT FOUND", Toast.LENGTH_SHORT).show();
            }
        }
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
            //String area = addresses.get(0).getLocality();
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


    //////// NEW GOOGLE DIRECTION WITH ETA AND DISTANCE
    private void sendRequest() {
        //private static final String TAG = "MapsActivity";
//        origin = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//        destination = new LatLng(latDB,lonDB);
        //latDB= currentLocationLatlng.latitude;
        //lonDB= currentLocationLatlng.longitude;
        origin = new LatLng(14.6184447,120.9873903);
        destination = new LatLng(latDB,lonDB);
//        double originLat = currentLocationLatlng.latitude;
//        double originLon = currentLocationLatlng.longitude;
        double originLat = 14.6184447;
        double originLon = 120.9873903;
        double destLat = 14.6184447;
        double destLon = 120.9873903;

        Log.d(TAG, "sendRequest: origin"+latDB);
        Log.d(TAG, "sendRequest: destination"+victimLocation);

        try {
            new DirectionFinder(this, originLat, originLon, destLat, destLon).execute();
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

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();


        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.eta)).setText(route.duration.text);
            ((TextView) findViewById(R.id.kmsAway)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hp))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hp))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
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
