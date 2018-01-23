package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
<<<<<<< HEAD
import android.widget.TextView;
=======
>>>>>>> 2dba82e9477c8f28769e5f4e1cf130c58180da2d

public class ForgotPassStep3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step3);
<<<<<<< HEAD

        TextView user = (TextView) findViewById(R.id.tvName);
        user.setText("");
=======
>>>>>>> 2dba82e9477c8f28769e5f4e1cf130c58180da2d
    }


    public void btnOnClickSearchEmail (View view){
        Intent i = new Intent(getApplicationContext(), ForgotPassStep2.class);
        startActivity(i);
    }
}
