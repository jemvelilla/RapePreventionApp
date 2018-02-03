package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountCreated extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    private UserInformation info;
    FirebaseDatabase database;
    DatabaseReference user;

    private String userKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);

        database = FirebaseDatabase.getInstance();
        info = (UserInformation) getIntent().getSerializableExtra("info");

        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = database.getReference("Users").child(userKey);

        user.child("email").setValue(info.getEmail());
        user.child("first_name").setValue(info.getFirstName().toString());
        user.child("last_name").setValue(info.getLastName().toString());
        user.child("age").setValue(Integer.toString(info.getAge()));
        user.child("birthdate").setValue(info.getBirthday().toString());
        user.child("current_address").setValue(info.getCurrentAddress().toString());
        user.child("contact_person1").setValue(info.getContactPerson1().toString());
        user.child("contact_number_person1").setValue(info.getContactNumber1().toString());
        user.child("contact_person2").setValue(info.getContactPerson2().toString());
        user.child("contact_number_person2").setValue(info.getContactNumber2().toString());
        user.child("contact_person3").setValue(info.getContactPerson3().toString());
        user.child("contact_number_person3").setValue(info.getContactNumber3().toString());
        user.child("mobile_number").setValue(info.getMobileNumber().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent homeIntent = new Intent(AccountCreated.this, Slides.class);
                    startActivity(homeIntent);
                    finish();
                } else {
                    // Task failed with an exception
                    Exception exception = task.getException();
                }
            }
        });
    }
}
