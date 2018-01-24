package com.build1.rapepreventionapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Contacts extends Fragment implements View.OnClickListener{

    String contact;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
        contact = preferences.getString("contacts", "");

        Log.v("view", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_contacts, container, false);
        Button btnContacts = (Button) v.findViewById(R.id.connBtn);
        btnContacts.setOnClickListener(this);



        if(!contact.isEmpty()){
            v = inflater.inflate(R.layout.activity_contactlist, container, false);

            String[] contacts = contact.split(",");
            List<String> contactList = new ArrayList<>();

            for (int i=0; i < contacts.length; i++){
                contactList.add(contacts[i]);
            }


            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, contactList);

            ListView listView = (ListView) v.findViewById(R.id.listView);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

            Log.v("view", "not null");
        }

        Log.v("view", "onCreateView");



        return v;
    }


    @Override
    public void onClick(View view) {

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, new Contacts2());
                ft.commit();
            }
        }
    }
}