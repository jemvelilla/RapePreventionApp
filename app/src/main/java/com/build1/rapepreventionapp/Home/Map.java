package com.build1.rapepreventionapp.Home;


import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;

import com.build1.rapepreventionapp.GooglePlacesAPI.DirectionsParser;
import com.build1.rapepreventionapp.GooglePlacesAPI.GetNearbyPlacesData;
import com.build1.rapepreventionapp.Model.PlaceInfo;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Darise on 24/01/2018.
 */

public class Map extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private FirebaseFirestore mFirestore;
    private UserInformation info;
    FirebaseDatabase database;
    DatabaseReference user;
    private static final String TAG = "Map";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GeoDataClient mGeoDataClient;
    Location currentLocation;
    LatLng currentLocationLatlng;
    private PlaceDetectionClient mPlaceDetectionClient;
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

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mFirestore = FirebaseFirestore.getInstance();
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
        listPoints =new ArrayList<>();

        double latitude;
        double longitude;


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

    public void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
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

                            Log.d(TAG, "onCompletemoto: " +currentLocationLatlng);


                            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
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
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    //reset when 2
                    if (listPoints.size() == 2) {
                        listPoints.clear();
                        mMap.clear();
                    }
                    //save first marker
                    listPoints.add(latLng);
                    //marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);

                    if (listPoints.size() == 1){
                        //add 1st marker
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        Log.e(TAG, "onMapLongClick: " +listPoints);
                        Log.e(TAG, "onMapLongClick: "+latLng );
                        //moveCamera(new LatLng(latLng,DEFAULT_ZOOM, ""++"");
                        Log.e(TAG, "onMapLongClick: " +listPoints);
                    }else{
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                        try {
                            victimAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                            String addressVictim = victimAddress.get(0).getAddressLine(0);
                            //String area = addresses.get(0).getLocality();
                            Log.e(TAG, "onComplete: "+victimAddress );

                            moveCamera(new LatLng(latLng.latitude, latLng.longitude), DEFAULT_ZOOM, ""+addressVictim+"");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    mMap.addMarker(markerOptions);
                    if (listPoints.size() == 2){
                        //url creation for request
                        String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
                        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                        taskRequestDirections.execute(url);
                        Log.e(TAG, "onMapLongClickasasa: " +listPoints);
                    }
                }
            });
            //initMap();


        }

        Log.d(TAG, "onMapReady: Map is Ready");
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
            TaskParser taskParser = new TaskParser();
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
                Toast.makeText(getActivity().getApplicationContext(), "DIRECTION NOT FOUND", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getActivity().getApplicationContext(), "Showing Nearby Plice Station", Toast.LENGTH_SHORT);
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

}



