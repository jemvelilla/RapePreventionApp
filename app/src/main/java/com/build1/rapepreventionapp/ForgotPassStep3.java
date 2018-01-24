package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassStep3 extends AppCompatActivity {
    FirebaseUser currentUser;
    EditText newPassword, confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step3);
        TextView user = (TextView) findViewById(R.id.tvName);
        user.setText("");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        newPassword = (EditText) findViewById(R.id.editTextNewPassword);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);


    }


    public void btnOnClickSearchEmail (View view){

        currentUser.updatePassword(newPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(ForgotPassStep3.this, "Password successfully updated.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgotPassStep3.this, "Failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }


}
