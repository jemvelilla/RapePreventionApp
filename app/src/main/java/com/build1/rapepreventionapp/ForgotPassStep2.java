package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassStep2 extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step2);


        FirebaseAuth auth = FirebaseAuth.getInstance();

        email = (String) getIntent().getSerializableExtra("email");
        String emailAddress = email;

        auth.signInWithEmailAndPassword(emailAddress,"jemylanicole").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(ForgotPassStep2.this, "Incorrect username or password.", Toast.LENGTH_LONG).show();
                }
            }
        });

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
}
