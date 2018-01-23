package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
<<<<<<< HEAD
import android.widget.TextView;
=======
>>>>>>> e58aef2390235de59606da5dc7feef9900674f06

public class ForgotPassStep3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step3);
<<<<<<< HEAD

        TextView user = (TextView) findViewById(R.id.tvName);
        user.setText( );
=======
>>>>>>> e58aef2390235de59606da5dc7feef9900674f06
    }


    public void btnOnClickSearchEmail (View view){
        Intent i = new Intent(getApplicationContext(), ForgotPassStep2.class);
        startActivity(i);
    }
}
