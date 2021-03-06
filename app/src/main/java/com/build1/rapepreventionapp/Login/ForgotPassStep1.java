package com.build1.rapepreventionapp.Login;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class ForgotPassStep1 extends AppCompatActivity {

    private ImageView loading;
    AnimationDrawable animation;
    private Button btnSearch;

    private FirebaseAuth auth;

    private EditText emailAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step1);

        loading = (ImageView) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        btnSearch = (Button) findViewById(R.id.buttonNextone);

        emailAdd = (EditText) findViewById(R.id.editTextEmailAdd);

        auth = FirebaseAuth.getInstance();
    }


    public void btnOnClickSearchEmail (View view){

        loading.setVisibility(View.VISIBLE);
        animation.start();
        btnSearch.setVisibility(View.INVISIBLE);

        String email = emailAdd.getText().toString().trim();
        Log.v("email",email);

        if (isNetworkAvailable()){

            auth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    if (task.isSuccessful()){
                        boolean check = !task.getResult().getProviders().isEmpty();

                        if (check){

                            Intent i = new Intent(getApplicationContext(), ForgotPassStep2.class);
                            i.putExtra("email", emailAdd.getText().toString());
                            startActivity(i);

                        } else {
                            loading.setVisibility(View.INVISIBLE);
                            animation.stop();
                            btnSearch.setVisibility(View.VISIBLE);

                            emailAdd.setError("Email address doesn't exist.");
                        }
                    } else {
                        btnSearch.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);
                        animation.stop();
                        Log.v("result", "Error getting documents: ", task.getException());
                        Toast.makeText(ForgotPassStep1.this, "Slow or no internet connection.", Toast.LENGTH_LONG).show();

                    }
                }
            });
        } else{

            loading.setVisibility(View.INVISIBLE);
            animation.stop();
            btnSearch.setVisibility(View.VISIBLE);

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
