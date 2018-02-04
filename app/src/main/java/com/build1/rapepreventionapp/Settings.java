package com.build1.rapepreventionapp;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.build1.rapepreventionapp.Model.EditInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Settings extends Fragment {

    private FirebaseAuth mAuth;
    DatabaseReference user;
    String userKey;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
//
//        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        user = FirebaseDatabase.getInstance().getReference("Users").child(userKey);
//
//        EditInformation.details = new ArrayList();
//
//        user.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
//
//                    String details = userSnapshot.getValue(String.class);
//                    EditInformation.details.add(details);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_settings, container, false);

        TextView txtEditProfile = (TextView) v.findViewById(R.id.editProfileTxt);
        TextView txtConfCall = (TextView) v.findViewById(R.id.confCallTxt);
        TextView txtChangePassword = (TextView) v.findViewById(R.id.changePassword);
        Button btnLogOut = (Button) v.findViewById(R.id.btnLogOut);

        txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (ft != null) {
                        ft.replace(R.id.rootLayout, new EditProfile());
                        ft.commit();
                    }
                }
            }
        });

        txtConfCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (ft != null) {
                        ft.replace(R.id.rootLayout, new ConfCall());
                        ft.commit();
                    }
                }
            }
        });

        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (ft != null) {
                        ft.replace(R.id.rootLayout, new ChangePassword());
                        ft.commit();
                    }
                }
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Log out func
                getActivity().finish();
                Intent i = new Intent(getActivity().getApplicationContext(), Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                mAuth.signOut();
            }
        });

        return v;


    }


}
