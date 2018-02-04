package com.build1.rapepreventionapp;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends Fragment implements View.OnClickListener{

    EditText editAge, editBirthdate, editContact1, editContactNumber1 ,editContact2, editContactNumber2, editContact3, editContactNumber3;
    EditText editAddress, editFirstName, editLastName;
    Button btnSave, btnCancel, btnChangePassword;

    String userKey;
    FirebaseDatabase database;
    DatabaseReference user;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edit_profile, container, false);

        editAge = (EditText) v.findViewById(R.id.editTextAge);
        editBirthdate = (EditText) v.findViewById(R.id.editTextDOB);
        editContact1 = (EditText) v.findViewById(R.id.editTextCP1);
        editContactNumber1 = (EditText) v.findViewById(R.id.editTextCP1Num);
        editContact2 = (EditText) v.findViewById(R.id.editTextCP2);
        editContactNumber2 = (EditText) v.findViewById(R.id.editTextCP2Num);
        editContact3 = (EditText) v.findViewById(R.id.editTextCP3);
        editContactNumber3 = (EditText) v.findViewById(R.id.editTextCP3Num);
        editAddress = (EditText) v.findViewById(R.id.editTextCAdd);
        editFirstName = (EditText) v.findViewById(R.id.editTextFN);
        editLastName = (EditText) v.findViewById(R.id.editTextLN);

        btnSave = (Button) v.findViewById(R.id.btnSaveChanges);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnChangePassword = (Button) v.findViewById(R.id.btnChangePassword);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSaveChanges:
                user = database.getReference("Users").child(userKey);

                user.child("first_name").setValue(editFirstName.getText().toString());
                user.child("last_name").setValue(editLastName.getText().toString());
                user.child("age").setValue(editAge.getText().toString());
                user.child("birthdate").setValue(editBirthdate.getText().toString());
                user.child("current_address").setValue(editAddress.getText().toString());
                user.child("contact_person1").setValue(editContact1.getText().toString());
                user.child("contact_number_person1").setValue(editContactNumber1.getText().toString());
                user.child("contact_person2").setValue(editContact2.getText().toString());
                user.child("contact_number_person2").setValue(editContactNumber2.getText().toString());
                user.child("contact_person3").setValue(editContact3.getText().toString());
                user.child("contact_number_person3").setValue(editContactNumber3.getText().toString());

                break;
            case R.id.btnCancel:
                break;

            case R.id.btnChangePassword:
                break;
        }

    }

}
