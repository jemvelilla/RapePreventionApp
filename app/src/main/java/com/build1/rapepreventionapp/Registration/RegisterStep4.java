package com.build1.rapepreventionapp.Registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.build1.rapepreventionapp.Login.LogIn2;
import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterStep4 extends AppCompatActivity {

    private UserInformation info;
    FirebaseDatabase database;
    DatabaseReference user;

    private FirebaseAuth firebaseAuth;

    private StorageReference mStorage;

    String savedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step4);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("images");

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        savedAccount = preferences.getString("savedAccount", "");


    }
    public void btnOnClickNextPassword(View v){

        EditText password = (EditText) findViewById(R.id.editTextPassword);
        EditText confPassword = (EditText) findViewById(R.id.editTextConfPassword);

        info = (UserInformation) getIntent().getSerializableExtra("info");

        String email = info.getEmail();
        String pass = password.getText().toString().trim();
        String confpass = confPassword.getText().toString().trim();
        String pattern = "((?=.*\\d)(?=.*[A-Z]).{6,20})";

        if (!pass.equals("") && !confpass.equals("")){
            if (pass.matches(pattern) && confpass.matches(pattern)){
                if (password.getText().toString().equals(confPassword.getText().toString())){

                    info.setPassword(pass);
                    Intent i = new Intent(getApplicationContext(), RegisterStep5.class);

                    i.putExtra("info", info);
                    startActivity(i);
                }else{

                    confPassword.setError("Passwords do not match.");

                }
            }else{
                Toast.makeText(getApplicationContext(), "Password must have 1 number, 1 uppercase letter and minimum of 6 characters", Toast.LENGTH_LONG).show();
            }
        } else {
            password.setError("Please enter a password");
        }

    }

    public void btnOnClickLoginPage(View v){

        if (!savedAccount.isEmpty()){
            Intent i = new Intent(RegisterStep4.this, LogIn2.class);
            startActivity(i);
        } else {
            Intent i = new Intent(RegisterStep4.this, Login.class);
            startActivity(i);
        }
    }


}
