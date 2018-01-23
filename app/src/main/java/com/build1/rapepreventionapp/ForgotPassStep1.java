package com.build1.rapepreventionapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class ForgotPassStep1 extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText emailAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step1);

        emailAdd = (EditText) findViewById(R.id.editTextEmailAdd);

        auth = FirebaseAuth.getInstance();
    }


    public void btnOnClickSearchEmail (View view){

        String email = emailAdd.getText().toString().trim();
        Log.v("email",email);

        if (isNetworkAvailable()){

            auth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                    boolean check = !task.getResult().getProviders().isEmpty();

                    if (check){

                        Intent i = new Intent(getApplicationContext(), ForgotPassStep2.class);
                        i.putExtra("email", emailAdd.getText().toString());
                        startActivity(i);

                    } else {
                        emailAdd.setError("Email address doesn't exist.");
                    }

                }
            });
        } else{
            Toast.makeText(ForgotPassStep1.this, "Slow or no internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
