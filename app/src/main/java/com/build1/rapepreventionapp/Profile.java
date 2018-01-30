package com.build1.rapepreventionapp;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Profile extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    DatabaseReference databaseUsers;
    String currentUser, age, birthdate, contact_number_person1, contact_number_person2, contact_number_person3;
    String contact_person1, contact_person2, contact_person3, current_address, email, first_name, last_name, mobile_number;

    List<String> userValues = new ArrayList<>();

    TextView tvAge, tvBirthdate, tvContactNumber1;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser =  TextUtils.substring(mAuth.getCurrentUser().getEmail(), 0, mAuth.getCurrentUser().getEmail().indexOf("@"));

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        Button btnLogout = (Button) v.findViewById(R.id.btnLogout);
        tvAge = (TextView)v.findViewById(R.id.age);
        tvBirthdate = (TextView) v.findViewById(R.id.birthdate);
        tvContactNumber1 = (TextView) v.findViewById(R.id.contact_number_1);

        btnLogout.setOnClickListener(this);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    String details = userSnapshot.getValue(String.class);
                    userValues.add(details);
                    Log.v("user", Integer.toString(userValues.size()));
                    Log.v("user", details);
                }

                tvAge.setText(userValues.get(0));
                tvBirthdate.setText(userValues.get(1));
                tvContactNumber1.setText(userValues.get(2));
                contact_number_person2 = userValues.get(3);
                contact_number_person3 = userValues.get(4);
                contact_person1 = userValues.get(5);
                contact_person2 = userValues.get(6);
                contact_person3 = userValues.get(7);
                current_address = userValues.get(8);
                email = userValues.get(9);
                first_name = userValues.get(10);
                last_name = userValues.get(11);
                mobile_number = userValues.get(12);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(getActivity(), Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        getActivity().finish();
        mAuth.signOut();
    }
}
