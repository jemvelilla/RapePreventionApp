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

public class Contacts2 extends Fragment implements View.OnClickListener{
    List<String> contactName = new ArrayList<>();
    List<String> contactNumber = new ArrayList<>();

    ListView contactList;
    String name, number;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String tempName = preferences.getString("contactNames", "");
        String tempNumber = preferences.getString("contactNumbers", "");

        if(!tempName.isEmpty() && !tempNumber.isEmpty()){
            String[] tempContactName = tempName.split(",");
            String[] tempContactNumber = tempNumber.split(",");

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
        Button btnAddContacts = (Button) v.findViewById(R.id.btnAddContacts);
        btnAddContacts.setOnClickListener(this);

        contactList = (ListView) v.findViewById(R.id.listView);

        Cursor cursor = getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, null,null,null,null);
        getActivity().startManagingCursor(cursor);

        UserInformation.contactName = new ArrayList<>();
        UserInformation.contactNumber = new ArrayList<>();

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                UserInformation.contactName.add(phoneName);
                UserInformation.contactNumber.add(phoneNumber);
            }
        }

        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID};

        final int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_checked,cursor,from,to);
        contactList.setAdapter(simpleCursorAdapter);
        contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                // TODO Auto-generated method stub

                name = ((TextView)v).getText().toString();
                number = UserInformation.contactNumber.get(position);

                if(contactName.size() < 10 && contactNumber.size() < 10){

                    if(!contactName.contains(name) && !contactNumber.contains(number)){
                        contactName.add(name);
                        contactNumber.add(number);
                    } else {
                        contactName.remove(name);
                        contactNumber.remove(number);
                    }

                } else {
                    Toast.makeText(getActivity(), "You can only select maximum of 10 contacts.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
            StringBuilder nameBuilder = new StringBuilder();
            for (String names : contactName) {
                if(!contactName.contains(names)){
                    nameBuilder.append(names);
                    nameBuilder.append(',');

                    SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("contactNames", nameBuilder.toString());
                    editor.commit();
                }
            }

            StringBuilder numberBuilder = new StringBuilder();
            for (String numbers : contactNumber){
                if(!contactNumber.contains(number)){
                    numberBuilder.append(numbers);
                    numberBuilder.append(',');

                    SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("contactNumbers", numberBuilder.toString());
                    editor.commit();
                }
            }

            Toast.makeText(getActivity(), contactName.size() + " contacts added.", Toast.LENGTH_SHORT).show();
    }
}
