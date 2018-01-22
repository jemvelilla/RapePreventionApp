package com.build1.rapepreventionapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterStep1 extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailAdd;

    UserInformation info = new UserInformation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step1);

        auth = FirebaseAuth.getInstance();

    }
    public void btnOnClickOne(View v){
        final EditText emailAdd = (EditText) findViewById(R.id.editTextEmailAdd);

        final String email = emailAdd.getText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern)){

            auth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                    boolean check = !task.getResult().getProviders().isEmpty();

                    if (!check){
                        info.setEmail(emailAdd.getText().toString());

                        Intent i = new Intent(getApplicationContext(), RegisterStep2.class);
                        i.putExtra("info", info);
                        startActivity(i);
                    } else {
                        emailAdd.setError("Email address already exists.");
                    }

                }
            });

        }else {
            emailAdd.setError("Invalid Email Address");
        }


    }

    public void btnOnClickLoginPage(View v){
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }
}
