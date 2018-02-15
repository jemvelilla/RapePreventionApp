package com.build1.rapepreventionapp.Bluno;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Contacts.Contacts;
import com.build1.rapepreventionapp.Model.EditInformation;
import com.build1.rapepreventionapp.R;
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
    private Button buttonScan;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String mCurrentId;

    /**sending SMS**/
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    List<String> numList = new ArrayList<>();

    String contactNumber, contactId, automatedCallState, automatedCall;
    String[] numbers, ids;

    Button buttonSendNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluno);

        SharedPreferences preferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        contactNumber = preferences.getString("contactNumbers", "");
        contactId = preferences.getString("contactIds", "");
        automatedCall = preferences.getString("automatedCall", "");
        automatedCallState = preferences.getString("automatedCallState", "");

        mFirestore = FirebaseFirestore.getInstance(); //instantiate firestore
        mCurrentId = FirebaseAuth.getInstance().getUid();

        ids = contactId.split(",");
        numbers = contactNumber.split(",");

        for (String number : numbers){
            Log.v("message", number);
        }

        onCreateProcess();														//onCreate Process by BlunoLibrary

        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200


        /**test notification sending**/

        /**end**/

        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String message = EditInformation.firstName + " " + EditInformation.lastName + " needs help.";

                Map<String, Object> notificationMessage = new HashMap<>();
                notificationMessage.put("message", message);
                notificationMessage.put("from", mCurrentId);

                for(String id: ids){
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
//                buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
            }
        });
        Log.v("message", "onCreate");
    }

    protected void onResume(){
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();
        //onResume Process by BlunoLibrary

        Log.v("message", "onResume");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("message", "onActivityResult");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //onPauseProcess();
        //onPause Process by BlunoLibrary

        Log.v("message", "onPauseProcess");
    }

    protected void onStop() {
        super.onStop();
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
        switch (theConnectionState) {											//Four connection state
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
    public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
        // TODO Auto-generated method stub
        Log.v("message", theString);
        final String message = EditInformation.firstName + " " + EditInformation.lastName + " needs help.";

        if (theString.contains("Pq")){

            Map<String, Object> notificationMessage = new HashMap<>();
            notificationMessage.put("message", message);
            notificationMessage.put("from", mCurrentId);

            for(String id: ids){
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
        }
    }

    public void sendNotification() {

        numbers = contactNumber.split(",");

        for (int i=0; i < numbers.length; i++){
            numList.add(numbers[i]);
        }

        String call = automatedCall;
        String message = EditInformation.firstName + " " + EditInformation.lastName + " needs your help!" +
                " Check this link to view the location. <<Insert link here>>";

        try {
            SmsManager smsManager = SmsManager.getDefault();

            if(!numList.isEmpty()){
                for (int counter=0; counter<numList.size(); counter++){
                    smsManager.sendTextMessage(numList.get(counter), null, message, null, null);
                }

                if(automatedCallState.equals("on")){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + call));
                    startActivity(intent);
                }
            }

            return;
        } catch(Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

}