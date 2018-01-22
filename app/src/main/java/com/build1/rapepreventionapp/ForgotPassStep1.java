package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class ForgotPassStep1 extends AppCompatActivity {

    FirebaseAuth auth;

    EditText emailAdd = (EditText) findViewById(R.id.editTextEmailAdd);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step1);
    }


    public void btnOnClickSearchEmail (View view){

        auth.fetchProvidersForEmail(emailAdd.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                boolean check = !task.getResult().getProviders().isEmpty();

                if (!check){

                    Intent i = new Intent(getApplicationContext(), ForgotPassStep2.class);
                    startActivity(i);

                } else {
                    emailAdd.setError("Email address doesn't exist.");
                }

            }
        });
    }
}
