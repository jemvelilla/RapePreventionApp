package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
    }

    public void btnOnClickSignUp(View v){

        Intent i = new Intent(getApplicationContext(), RegisterStep1.class);
        startActivity(i);
    }

    public void btnOnClickLoginPage(View v){
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }
}
