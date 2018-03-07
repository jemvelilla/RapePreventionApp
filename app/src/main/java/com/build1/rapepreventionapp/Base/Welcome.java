package com.build1.rapepreventionapp.Base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.build1.rapepreventionapp.Home.BottomNavigation;
import com.build1.rapepreventionapp.Home.Help;
import com.build1.rapepreventionapp.Login.AccountVerification;
import com.build1.rapepreventionapp.Login.LogIn2;
import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.R;
import com.build1.rapepreventionapp.Registration.RegisterStep1;
import com.build1.rapepreventionapp.Registration.Slides;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Welcome extends AppCompatActivity {

    String savedAccount;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isConnected(Welcome.this)) buildDialog(Welcome.this).show();
        else {
            //Toast.makeText(Welcome.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_welcome_page);
        }

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        savedAccount = preferences.getString("savedAccount", "");

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){

                    SharedPreferences preferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("savedAccount", mAuth.getCurrentUser().getUid());
                    editor.commit();

                    if(!mAuth.getCurrentUser().isEmailVerified()){
                        Intent intent = new Intent(Welcome.this, AccountVerification.class);
                        finish();
                        startActivity(intent);
                    } else{
                        Intent intent = new Intent(Welcome.this, BottomNavigation.class);
                        finish();
                        startActivity(intent);
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("message","onStart");
        mAuth.addAuthStateListener(authStateListener);
    }

    public void btnOnClickSignUp(View v){

        Intent i = new Intent(getApplicationContext(), RegisterStep1.class);
        finish();
        startActivity(i);

    }

    public void btnOnClickLoginPage(View v){

        if (!savedAccount.isEmpty()){
            Intent i = new Intent(Welcome.this, LogIn2.class);
            finish();
            startActivity(i);
        } else {
            Intent i = new Intent(Welcome.this, Login.class);
            finish();
            startActivity(i);
        }
    }

    public void btnOnClickHelp(View view) {
        Intent i = new Intent(Welcome.this, Help.class);
        startActivity(i);
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
}
