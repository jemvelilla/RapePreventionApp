package com.build1.rapepreventionapp;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Contacts2 extends Fragment{
    Set<String> contactName = new HashSet<>();
    Set<String> contactNumber = new HashSet<>();
    ListView contactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
        String tempName = preferences.getString("contactNames", "");
        String tempNumber = preferences.getString("contactNumbers", "");

        if(!tempName.isEmpty() && !tempNumber.isEmpty()){
            String[] tempContactName = tempName.split(",");
            String[] tempContactNumber = tempName.split(",");

            for (int i=0; i < tempContactName.length; i++){
                contactName.add(tempContactName[i]);
                contactNumber.add(tempContactNumber[i]);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_contacts2, container, false);


        contactList = (ListView) v.findViewById(R.id.listView);

        Cursor cursor = getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, null,null,null,null);
        getActivity().startManagingCursor(cursor);

        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID};

        final int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_activated_2,cursor,from,to);
        contactList.setAdapter(simpleCursorAdapter);
        contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                // TODO Auto-generated method stub

                String name = ((TextView)(v.findViewById(android.R.id.text1))).getText().toString();
                String number = ((TextView)(v.findViewById(android.R.id.text2))).getText().toString();

                if(contactName.size() < 10){

                    contactName.add(name);
                    contactNumber.add(number);

                    StringBuilder nameBuilder = new StringBuilder();
                    for (String names : contactName){
                        nameBuilder.append(names);
                        nameBuilder.append(',');

                        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("contactNames", nameBuilder.toString());
                        editor.commit();
                    }



                    StringBuilder numberBuilder = new StringBuilder();
                    for (String numbers : contactNumber){
                        numberBuilder.append(numbers);
                        numberBuilder.append(',');

                        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("contactNumbers", numberBuilder.toString());
                        editor.commit();
                    }
                } else {
                    Toast.makeText(getActivity(), "You can only select maximum of 10 contacts.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        StringBuilder stringBuilder = new StringBuilder();
//        for (String s: contacts){
//            stringBuilder.append(s);
//            stringBuilder.append(',');
//
//            SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("contacts", stringBuilder.toString());
//            editor.commit();
//        }
        return v;
    }

}
