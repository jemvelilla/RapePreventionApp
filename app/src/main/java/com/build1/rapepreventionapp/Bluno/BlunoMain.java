package com.build1.rapepreventionapp.Bluno;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.build1.rapepreventionapp.Model.EditInformation;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import android.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlunoMain extends BlunoLibrary {
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    private static final String TAG = "BlunoMain";
    private Button buttonScan;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String mCurrentId;

    //device location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionGranted = true;
    Location currentLocation;
    String latitude, longtitude;

    /**sending SMS**/
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    List<String> numList = new ArrayList<>();

    String contactNumber, contactId, automatedCallState, automatedCall;
    String[] numbers, ids;

    String current_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluno);

        SharedPreferences preferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        contactNumber = preferences.getString("contactNumbers", "");
        contactId = preferences.getString("contactIds", "");
        automatedCall = preferences.getString("automatedCall", "");
        automatedCallState = preferences.getString("automatedCallState", "");

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance(); //instantiate firestore
        mCurrentId = FirebaseAuth.getInstance().getUid();
        current_id = mAuth.getCurrentUser().getUid();

        ids = contactId.split(",");
        numbers = contactNumber.split(",");

        for (String number : numbers) {
            Log.v("message", number);
        }

        onCreateProcess();                                                        //onCreate Process by BlunoLibrary

        serialBegin(115200);                                                    //set the Uart Baudrate on BLE chip to 115200


        /**test notification sending**/

        /**end**/

        buttonScan = (Button) findViewById(R.id.buttonScan);                    //initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //setUpGClient();

                final String message = EditInformation.firstName + " " + EditInformation.lastName + " needs help.";

                Map<String, Object> notificationMessage = new HashMap<>();
                notificationMessage.put("message", message);
                notificationMessage.put("from", mCurrentId);

                for (String id : ids) {
                    mFirestore.collection("Users/" + id + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(BlunoMain.this, "Notification sent.", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BlunoMain.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //sendNotification(); //send text and call
                getDeviceLocation(); //store on database onlocationchanged

                //buttonScanOnClickProcess();                                        //Alert Dialog for selecting the BLE device                 //Alert Dialog for selecting the BLE device
            }
        });

        Log.v("message", "onCreate");
        getDeviceLocation2();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getDeviceLocation();
    }

    protected void onResume() {
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();
        //getDeviceLocation();
        //onResume Process by BlunoLibrary

        Log.v("message", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //getDeviceLocation();
        //onPauseProcess();
        //onPause Process by BlunoLibrary

        Log.v("message", "onPauseProcess");
    }

    protected void onStop() {
        super.onStop();
        //getDeviceLocation();
        //onStopProcess();														//onStop Process by BlunoLibrary

        Log.v("message", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //onDestroyProcess();														//onDestroy Process by BlunoLibrary

        Log.v("message", "onDestroy");
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {                                            //Four connection state
            case isConnected:
                buttonScan.setText("CONNECTED");
                break;
            case isConnecting:
                buttonScan.setText("CONNECTING");
                break;
            case isToScan:
                buttonScan.setText("CONNECT");
                break;
            case isScanning:
                buttonScan.setText("SCANNING");
                break;
            case isDisconnecting:
                buttonScan.setText("DISCONNECTING");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSerialReceived(String theString) {                            //Once connection data received, this function will be called
        // TODO Auto-generated method stub
        Log.v("message", theString);
        final String message = EditInformation.firstName + " " + EditInformation.lastName + " needs help.";

        if (theString.contains("Tulong Activated")) {

            Map<String, Object> notificationMessage = new HashMap<>();
            notificationMessage.put("message", message);
            notificationMessage.put("from", mCurrentId);

            for (String id : ids) {
                mFirestore.collection("Users/" + id + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(BlunoMain.this, "Notification sent.", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BlunoMain.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            sendNotification();
            Log.d(TAG, "onSerialReceived: notif lang");
            Log.d(TAG, "onSerialReceived: umabot");
        }

        getDeviceLocation();
    }

    public void sendNotification() {

        numbers = contactNumber.split(",");

        for (int i = 0; i < numbers.length; i++) {
            numList.add(numbers[i]);
        }

        String call = automatedCall;
        String message = EditInformation.firstName + " " + EditInformation.lastName + " needs your help!" +
                " Check this link to view the location. https://tulongrapepreventionapp.dev/"+current_id;

        try {
            SmsManager smsManager = SmsManager.getDefault();

            if (!numList.isEmpty()) {
                for (int counter = 0; counter < numList.size(); counter++) {
                    smsManager.sendTextMessage(numList.get(counter), null, message, null, null);
                }

                if (automatedCallState.equals("on")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + call));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent); //edited darise
                }
            }

            return;
        } catch(Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void getDeviceLocation(){

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Log.d("pangdebug", "pumasok");
        try {
            if (mLocationPermissionGranted) {

                final com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull final com.google.android.gms.tasks.Task<Location> task) {
                        //final String placeId = task.getPlaceId();
                        if (task.isSuccessful()) {

                            Log.d("pangdebug", "pumasok sa onLocationChanged");

                            final LocationListener listener = new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {

                                    Log.d("pangdebug", "pumasok sa onLocationChanged");

                                    mylocation = location;
                                    if (mylocation != null) {
                                        Log.d("pangdebug", "pumasok sa myLocation");

                                        //Double latitude=mylocation.getLatitude();
                                        //Double longitude=mylocation.getLongitude();
                                        //Or Do whatever you want with your location
                                        Log.v("message", "onComplete: found locationasd");
                                        currentLocation = new Location(task.getResult());
                                        latitude = String.valueOf(currentLocation.getLatitude());

                                        longtitude = String.valueOf(currentLocation.getLongitude());

                                        Map<String, Object> locationMap = new HashMap<>();
                                        locationMap.put("latitude", latitude);
                                        locationMap.put("longitude", longtitude);

                                        mFirestore.collection("Users").document(current_id).update(locationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Log.d("message", "location stored.");
                                            }
                                        });

                                        Log.v("message", "onComplete: " +latitude);
                                        Log.v("message", "onComplete: " +longtitude);

                                        //final String placeId = task.getResult();
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

                            };
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, (android.location.LocationListener) listener);

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
                            latitude = String.valueOf(currentLocation.getLatitude());

                            longtitude = String.valueOf(currentLocation.getLongitude());

                            Map<String, Object> locationMap = new HashMap<>();
                            locationMap.put("latitude", latitude);
                            locationMap.put("longitude", longtitude);

                            mFirestore.collection("Users").document(current_id).update(locationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("message", "location stored.");
                                }
                            });

                            Log.v("message", "onComplete: " + latitude);
                            Log.v("message", "onComplete: " + longtitude);

                            //final String placeId = task.getResult();

                        }

                    }
                });
            } else {
                Log.d(TAG, "onComplete: NASAN");
            }
        }catch(SecurityException e)
        {
            Log.e(TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }
    }
}