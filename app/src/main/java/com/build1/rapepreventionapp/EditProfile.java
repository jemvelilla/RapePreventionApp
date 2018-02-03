package com.build1.rapepreventionapp;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditProfile extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_profile, container, false);
    }

    public void btnOnClickChangePassword(View view){
//        Intent i = new Intent(getActivity().getApplicationContext(), ChangePassword.class);
//        startActivity(i);
    }

    //SAVE CHANGES BTN
    //CANCEL BTN

}
