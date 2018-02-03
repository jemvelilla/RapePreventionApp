package com.build1.rapepreventionapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;

    TextView tvAge, tvBirthdate, tvContact1, tvContactNumber1 ,tvContact2, tvContactNumber2, tvContact3, tvContactNumber3;
    TextView tvAddress, tvName, tvPhoneNum;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        ImageButton btnLogout = (ImageButton) v.findViewById(R.id.btnSettings);
        tvName = (TextView) v.findViewById(R.id.name);
        tvAge = (TextView)v.findViewById(R.id.age);
        tvAddress = (TextView) v.findViewById(R.id.address);
        tvBirthdate = (TextView) v.findViewById(R.id.birthday);
        tvPhoneNum = (TextView) v.findViewById(R.id.phoneNum);
        tvContact1 = (TextView) v.findViewById(R.id.eContacts1);
        tvContactNumber1 = (TextView) v.findViewById(R.id.eContactsNum1);
        tvContact2 = (TextView) v.findViewById(R.id.eContacts2);
        tvContactNumber2 = (TextView) v.findViewById(R.id.eContactsNum2);
        tvContact3 = (TextView) v.findViewById(R.id.eContacts3);
        tvContactNumber3 = (TextView) v.findViewById(R.id.eContactsNum3);
        btnLogout.setOnClickListener(this);

        tvAge.setText(UserInformation.details.get(0) + " " + "years old");
        tvBirthdate.setText(UserInformation.details.get(1));
        tvContactNumber1.setText(UserInformation.details.get(2));
        tvContactNumber2.setText(UserInformation.details.get(3));
        tvContactNumber3.setText(UserInformation.details.get(4));
        tvContact1.setText(UserInformation.details.get(5));
        tvContact2.setText(UserInformation.details.get(6));
        tvContact3.setText(UserInformation.details.get(7));
        tvAddress.setText(UserInformation.details.get(8));
        tvName.setText(UserInformation.details.get(10) + " " + UserInformation.details.get(11));
        tvPhoneNum.setText(UserInformation.details.get(12));


        return v;
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(getActivity().getApplicationContext(), Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        getActivity().finish();
        mAuth.signOut();
    }
}
