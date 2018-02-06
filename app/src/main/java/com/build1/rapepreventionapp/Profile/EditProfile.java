package com.build1.rapepreventionapp.Profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.build1.rapepreventionapp.Home.BottomNavigation;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditProfile extends Fragment implements View.OnClickListener{

    EditText editAge, editBirthdate, editContact1, editContactNumber1 ,editContact2, editContactNumber2, editContact3, editContactNumber3;
    EditText editAddress, editFirstName, editLastName;
    Button btnSave, btnCancel, btnChangePassword;

    String userKey;
    FirebaseDatabase database;
    DatabaseReference user;
    FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
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


        editAge.setText(UserInformation.details.get(0));
        editBirthdate.setText(UserInformation.details.get(1));
        editContactNumber1.setText(UserInformation.details.get(2));
        editContactNumber2.setText(UserInformation.details.get(3));
        editContactNumber3.setText(UserInformation.details.get(4));
        editContact1.setText(UserInformation.details.get(5));
        editContact2.setText(UserInformation.details.get(6));
        editContact3.setText(UserInformation.details.get(7));
        editAddress.setText(UserInformation.details.get(8));
        editFirstName.setText(UserInformation.details.get(10));
        editLastName.setText(UserInformation.details.get(11));

        btnSave = (Button) v.findViewById(R.id.btnSaveChanges);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);

        editBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = month +"/"+ day + "/" + year;
                editBirthdate.setText(date);
            }
        };

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSaveChanges:
                String mobileNumPattern = "^(09|\\+639)\\d{9}$";

                if (editFirstName.getText().toString().trim().equals("")){
                    editFirstName.setError("Field Required");
                }else if (editLastName.getText().toString().trim().equals("")){
                    editLastName.setError("Field Required");
                }else if (editAge.getText().toString().trim().equals("")){
                    editAge.setError("Field Required");
                }else if (editBirthdate.getText().toString().trim().equals("")){
                    editBirthdate.setError("Field Required");
                }else if (editAddress.getText().toString().trim().equals("")){
                    editAddress.setError("Field Required");
                }else if (editContact1.getText().toString().trim().equals("")){
                    editContact1.setError("Field Required");
                }else if (!editContactNumber1.getText().toString().matches(mobileNumPattern)){
                    editContactNumber1.setError("Invalid mobile number");
                }else if (editContact2.getText().toString().trim().equals("")){
                    editContact2.setError("Field Required");
                }else if (!editContactNumber2.getText().toString().matches(mobileNumPattern)){
                    editContactNumber2.setError("Invalid mobile number");
                } else if (editContact3.getText().toString().trim().equals("")){
                    editContact3.setError("Field Required");
                }else if (!editContactNumber3.getText().toString().matches(mobileNumPattern)){
                    editContactNumber3.setError("Invalid mobile number");
                } else {

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
                    user.child("contact_number_person3").setValue(editContactNumber3.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), " Profile updated successfully.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity().getApplicationContext(), BottomNavigation.class);
                                startActivity(i);
                            } else {
                                // Task failed with an exception
                                Exception exception = task.getException();
                            }
                        }
                    });

                }
                break;
            case R.id.btnCancel:
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (ft != null) {
                        ft.replace(R.id.rootLayout, new Profile());
                        ft.commit();
                    }
                }
                break;
        }

    }

}
