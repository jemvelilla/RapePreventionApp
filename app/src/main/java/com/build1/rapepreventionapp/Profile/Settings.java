package com.build1.rapepreventionapp.Profile;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.build1.rapepreventionapp.Bluno.BlunoMain;
import com.build1.rapepreventionapp.Login.LogIn2;
import com.build1.rapepreventionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Settings extends Fragment {

    private FirebaseAuth mAuth;
    DatabaseReference user;
    String userKey;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_settings, container, false);

        TextView txtEditProfile = (TextView) v.findViewById(R.id.editProfileTxt);
        TextView txtConfCall = (TextView) v.findViewById(R.id.confCallTxt);
        TextView txtChangePassword = (TextView) v.findViewById(R.id.changePassword);
        TextView txtConnectToDevice = (TextView) v.findViewById(R.id.connectToDevice);

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

        txtConnectToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), BlunoMain.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(getActivity(), LogIn2.class);
                getActivity().finish();
                startActivity(i);

            }
        });

        return v;


    }


}
