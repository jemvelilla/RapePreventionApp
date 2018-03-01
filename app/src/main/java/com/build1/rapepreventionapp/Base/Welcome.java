package com.build1.rapepreventionapp.Base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.build1.rapepreventionapp.Home.BottomNavigation;
import com.build1.rapepreventionapp.Login.AccountVerification;
import com.build1.rapepreventionapp.Login.LogIn2;
import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.R;
import com.build1.rapepreventionapp.Registration.RegisterStep1;
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
        setContentView(R.layout.activity_welcome_page);

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
                        Log.d("rehd", "hoy bawal bumalik");
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
        startActivity(i);
        finish();
    }

    public void btnOnClickLoginPage(View v){

        if (!savedAccount.isEmpty()){
            Intent i = new Intent(Welcome.this, LogIn2.class);
            startActivity(i);
        } else {
            Intent i = new Intent(Welcome.this, Login.class);
            startActivity(i);
        }
    }
}
