package com.build1.rapepreventionapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by JEMYLA VELILLA on 30/01/2018.
 */

public class AccountVerification extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    DatabaseReference databaseUsers;

    private WebView webView;
    TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);

        mAuth = FirebaseAuth.getInstance();

        Log.v("user", String.valueOf(mAuth.getCurrentUser().isEmailVerified()));

        tvUser = (TextView) findViewById(R.id.email);
        tvUser.setText(mAuth.getCurrentUser().getEmail());
    }

    public void btnOnClickVerify (View view){
        Log.v("user", mAuth.getCurrentUser().getEmail());
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Uri uri = Uri.parse("https://mail.google.com");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
//
//                    Intent i = new Intent(AccountVerification.this, WebActivity.class);
//                    startActivity(i);
                } else {
                    Toast.makeText(AccountVerification.this, "Failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void btnOnClickLogout (View view){
        Intent i = new Intent(getApplicationContext(), Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        mAuth.signOut();
    }

}
