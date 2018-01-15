package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ForgotPassStep2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step2);
    }


    public void btnOnClickContinueCode (View view){
        Intent i = new Intent(getApplicationContext(), ForgotPassStep3.class);
        startActivity(i);
    }
}
