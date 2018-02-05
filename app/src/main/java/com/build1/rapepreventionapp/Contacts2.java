package com.build1.rapepreventionapp;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Model.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class Contacts2 extends Fragment implements View.OnClickListener{
    List<String> contactName = new ArrayList<>();
    List<String> contactNumber = new ArrayList<>();
    List<String> isChecked = new ArrayList<>();
    List<String> storedName = new ArrayList<>();
    List<String> storedNumber = new ArrayList<>();

    ListView contactList;
    String name, number;
    Button btnAddContacts;
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
                storedName.add(tempContactName[i]);
                storedNumber.add(tempContactNumber[i]);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_contacts2, container, false);
        btnAddContacts = (Button) v.findViewById(R.id.btnAddContacts);
        btnAddContacts.setOnClickListener(this);

        contactList = (ListView) v.findViewById(R.id.listView);

        Cursor cursor = getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, null,null,null,null);
        getActivity().startManagingCursor(cursor);

        //instantiate list contact name and contact number from UserInformation class
        UserInformation.contactName = new ArrayList<>();
        UserInformation.contactNumber = new ArrayList<>();

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //store all contact values to userinformation list
                UserInformation.contactName.add(phoneName);
                UserInformation.contactNumber.add(phoneNumber);
            }
        }

        final String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID};

        final int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice,cursor,from,to){
            public View getView(final int position, View convertView, ViewGroup parent) {
                if(convertView == null)
                {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    convertView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);

                    final CheckedTextView ctv = (CheckedTextView)convertView.findViewById(android.R.id.text1);
                    ctv.setText(UserInformation.contactName.get(position));

                    ctv.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //get the value of selected item

                            name = ((TextView)v).getText().toString();
                            number = UserInformation.contactNumber.get(position);

                            //check if the stored named and number are less than 10
                            if(storedName.size() < 10 && storedNumber.size() < 10){
                                //check if the stored name and number already contain the selected item
                                if(!storedName.contains(name) && !storedNumber.contains(number)){
                                    //
                                    if(contactName.size() < (10 - storedName.size()) && contactNumber.size() < (10 - storedNumber.size())){
                                        //for checking and unchecking on the current view
                                        if(!contactName.contains(name) && !contactNumber.contains(number)){
                                            contactName.add(name);
                                            contactNumber.add(number);
                                            ctv.setChecked(true);
                                        } else {
                                            contactName.remove(name);
                                            contactNumber.remove(number);
                                            ctv.setChecked(false);
                                        }
                                    } else{
                                        if (ctv.isChecked()){
                                            contactName.remove(ctv.getText().toString());
                                            contactNumber.remove(UserInformation.contactNumber.get(position));
                                            ctv.setChecked(false);
                                        } else{
                                            ctv.setActivated(false);
                                            Toast.makeText(getActivity(), "You have reached the maximum of 10 contacts.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), name+" is already added.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                ctv.setActivated(false);
                                Toast.makeText(getActivity(), "You have reached the maximum of 10 contacts.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    ctv.setChecked(false);
                }
                return convertView;
            };
        };
        contactList.setAdapter(simpleCursorAdapter);
        contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        return v;
    }

    @Override
    public void onClick(View view) {

        for (int i=0; i < contactName.size(); i++){
            storedName.add(contactName.get(i));
            storedNumber.add(contactNumber.get(i));
        }

        StringBuilder nameBuilder = new StringBuilder();
            for (String names : storedName) {
                nameBuilder.append(names);
                nameBuilder.append(',');

                SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("contactNames", nameBuilder.toString());
                editor.commit();
            }

            StringBuilder numberBuilder = new StringBuilder();
            for (String numbers : storedNumber){
                numberBuilder.append(numbers);
                numberBuilder.append(',');

                SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("contactNumbers", numberBuilder.toString());
                editor.commit();
            }

            Toast.makeText(getActivity(), contactName.size() + " contact(s) added.", Toast.LENGTH_SHORT).show();

            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                if (ft != null) {
                    ft.replace(R.id.rootLayout, new Contacts());
                    ft.commit();
                }
            }
    }
}
