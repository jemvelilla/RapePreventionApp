package com.build1.rapepreventionapp.Login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;
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

        auth.sendPasswordResetEmail(email.toString())
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassStep2.this, "Please check your email.", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(ForgotPassStep2.this, "Sending failed. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void btnOnClickOpenGmail(View view){
        Uri uri = Uri.parse("https://mail.google.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }




}
