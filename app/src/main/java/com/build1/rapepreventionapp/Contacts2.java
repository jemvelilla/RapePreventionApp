package com.build1.rapepreventionapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Contacts2 extends Fragment implements View.OnClickListener{
    List<String> contacts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contacts = new ArrayList<>();
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
        contacts.add("09359175250");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_contacts2, container, false);

        Button btnAddContacts = (Button) v.findViewById(R.id.addBtn);
        btnAddContacts.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {

        StringBuilder stringBuilder = new StringBuilder();
        for (String s: contacts){
            stringBuilder.append(s);
            stringBuilder.append(',');

            SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("contacts", stringBuilder.toString());
            editor.commit();
        }
    }
}
