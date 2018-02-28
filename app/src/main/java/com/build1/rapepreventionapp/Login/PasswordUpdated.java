package com.build1.rapepreventionapp.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.build1.rapepreventionapp.R;

public class PasswordUpdated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_success);
    }

    public void btnOnClickContinue (View view){

        Intent i = new Intent(this, Login.class);
        startActivity(i);

    }
}
