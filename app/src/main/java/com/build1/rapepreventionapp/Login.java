package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.editTextUserName);
        password = (EditText) findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        Log.v("message","onCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("message","onStart");
        mAuth.addAuthStateListener(authStateListener);
    }

    public void btnOnClickLogin(View v){

        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)){
            Toast.makeText(this, "Enter both values", Toast.LENGTH_LONG).show();
        } else{
            mAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful()){
                        Toast.makeText(Login.this, "Incorrect username or password.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void btnOnClickSignUp(View view) {
        Intent i = new Intent(getApplicationContext(), RegisterStep1.class);
        startActivity(i);
    }
    public void btnOnClickForgotPass(View view) {
        Intent i = new Intent(getApplicationContext(), ForgotPassStep1.class);
        startActivity(i);
    }
}
