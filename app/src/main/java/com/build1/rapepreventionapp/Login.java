package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnOnClickLogin(View v){


    }

    public void btnOnClickSignUp(View view) {
        Intent i = new Intent(getApplicationContext(), RegisterStep1.class);
        startActivity(i);
    }
}
