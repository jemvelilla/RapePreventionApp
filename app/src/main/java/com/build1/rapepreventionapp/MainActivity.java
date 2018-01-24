package com.build1.rapepreventionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    List<String> numList = new ArrayList<>();

    String contactNumber;
    String[] numbers;

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences preferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        contactNumber = preferences.getString("contactNumbers", "");
       sendSMS();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homeIntent = new Intent(MainActivity.this, Welcome.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void sendSMS() {

        numbers = contactNumber.split(",");

        for (int i=0; i < numbers.length; i++){
            numList.add(numbers[i]);
            Log.v("preferences", numbers[1]);
        }

        String call = "09491036631";
        String message = "Jemyla is in danger.";

        try {
            SmsManager smsManager = SmsManager.getDefault();

            if(!numList.isEmpty()){
                for (int counter=0; counter<numList.size(); counter++){
                    smsManager.sendTextMessage(numList.get(counter), null, message, null, null);
                }

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + call));

                startActivity(intent);
            }

            return;
        } catch(Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

}
