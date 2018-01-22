package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassStep2 extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step2);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        email = (String) getIntent().getSerializableExtra("email");
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassStep2.this, "Email sent.", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(ForgotPassStep2.this, "Sending failed. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }


    public void btnOnClickContinueCode (View view){
        Intent i = new Intent(getApplicationContext(), ForgotPassStep3.class);
        startActivity(i);
    }
}
