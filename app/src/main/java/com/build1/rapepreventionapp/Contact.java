package com.build1.rapepreventionapp;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JEMYLA VELILLA on 05/02/2018.
 */

public class Contact extends Fragment implements View.OnClickListener{
    Button btnAddContacts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_contact, container, false);
        btnAddContacts = (Button) v.findViewById(R.id.btnAddContacts);
        btnAddContacts.setOnClickListener(this);

        ListView listView  = (ListView) v.findViewById(R.id.listView);

        Cursor cursor = getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, null,null,null,null);
        getActivity().startManagingCursor(cursor);

        //instantiate list contact name and contact number from UserInformation class
        UserInformation.contactName = new ArrayList<>();
        UserInformation.contactNumber = new ArrayList<>();
        final List<UserModel> users = new ArrayList<>();

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //store all contact values to userinformation list
                UserInformation.contactName.add(phoneName);
                UserInformation.contactNumber.add(phoneNumber);
                users.add(new UserModel(false, phoneName));
            }
        }

        final CustomAdapter adapter = new CustomAdapter(getActivity(), users);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserModel model = users.get(i);

                if(model.isSelected()){
                    model.setSelected(false);
                } else {
                    model.setSelected(true);
                }

                users.set(i, model);

                //now update adapter
                adapter.updateRecords(users);

            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        
    }
}
