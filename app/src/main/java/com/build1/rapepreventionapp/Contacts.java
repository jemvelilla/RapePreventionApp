package com.build1.rapepreventionapp;

import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Contacts extends Fragment implements View.OnClickListener{

    String contactName;
    String contactNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
        contactName = preferences.getString("contactNames", "");
        contactNumber = preferences.getString("contactNumbers", "");
        Log.v("view", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_contacts, container, false);
        Button btnContacts = (Button) v.findViewById(R.id.connBtn);
        btnContacts.setOnClickListener(this);



        if(!contactName.isEmpty() && !contactNumber.isEmpty()){
            v = inflater.inflate(R.layout.activity_contactlist, container, false);
            Button btnAdd = (Button) v.findViewById(R.id.addBtn);
            btnAdd.setOnClickListener(this);

            String[] names = contactName.split(",");
            String[] numbers = contactNumber.split(",");

            final List<String> nameList = new ArrayList<>();
            final List<String> numList = new ArrayList<>();

            for (int i=0; i < names.length; i++){
                nameList.add(names[i]);
                numList.add(numbers[i]);
            }


            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, nameList){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                            TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                            text1.setText(nameList.get(position));
                            text2.setText(numList.get(position));
                            return view;
                        }
                    };

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

        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, new Contacts2());
                ft.commit();
            }
        }
    }
}