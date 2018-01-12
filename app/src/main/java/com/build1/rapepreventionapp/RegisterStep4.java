package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterStep4 extends AppCompatActivity {

    private UserInformation info;
    FirebaseDatabase database;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step4);

        database = FirebaseDatabase.getInstance();

    }
    public void btnOnClickNextPassword(View v){
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        EditText confPassword = (EditText) findViewById(R.id.editTextConfPassword);

        info = (UserInformation) getIntent().getSerializableExtra("info");

        if (password.getText().toString().equals(confPassword.getText().toString())){

            Intent i = new Intent(getApplicationContext(), Slides.class);
            startActivity(i);

            user = database.getReference("Users").child(info.getFirstName() + " " + info.getLastName());

            user.child("email").setValue(info.getEmail());
            user.child("mobile_number").setValue(info.getMobileNumber().toString());
            user.child("first_name").setValue(info.getFirstName().toString());
            user.child("last_name").setValue(info.getLastName().toString());
            user.child("age").setValue(Integer.toString(info.getAge()));
            user.child("current_address").setValue(info.getCurrentAddress().toString());
            user.child("contact_person1").setValue(info.getContactPerson1().toString());
            user.child("contact_number_person1").setValue(info.getContactNumber1().toString());
            user.child("contact_person2").setValue(info.getContactPerson2().toString());
            user.child("contact_number_person2").setValue(info.getContactNumber2().toString());

            if(info.getContactPerson3() != null && info.getContactNumber3()!= null) {
                user.child("contact_person3").setValue(info.getContactPerson3().toString());
                user.child("contact_number_person3").setValue(info.getContactNumber3().toString());
            }

        }else{

            confPassword.setError("Passwords do not match.");

        }
    }

    public void btnOnClickLoginPage(View v){
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }
}
