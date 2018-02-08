package com.build1.rapepreventionapp.Contacts;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;

import java.util.ArrayList;
import java.util.List;


public class Contacts extends Fragment implements View.OnClickListener{

    String contactName;
    String contactNumber;

    List<String> nameList = new ArrayList<>();
    List<String> numList = new ArrayList<>();

    String[] names;
    String[] numbers;
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

            names = contactName.split(",");
            numbers = contactNumber.split(",");

            for (int i=0; i < names.length; i++){
                nameList.add(names[i]);
                numList.add(numbers[i]);
            }


            final ArrayAdapter<String> arrayAdapter =
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

            //delete from sharedpreferences
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), nameList.get(position) + " is removed from your emergency contacts.", Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("contactNames");
                    editor.remove("contactNumbers");
                    editor.commit();

                    nameList.remove(nameList.get(position));
                    numList.remove(numList.get(position));
                    arrayAdapter.notifyDataSetChanged();

                    StringBuilder nameBuilder = new StringBuilder();
                    for (String numbers : nameList){
                        nameBuilder.append(numbers);
                        nameBuilder.append(',');

                        preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putString("contactNames", nameBuilder.toString());
                        editor.commit();
                    }

                    StringBuilder numberBuilder = new StringBuilder();
                    for (String numbers : numList){
                        numberBuilder.append(numbers);
                        numberBuilder.append(',');

                        preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putString("contactNumbers", numberBuilder.toString());
                        editor.commit();
                    }

                    return true;
                }
            });

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
                ft.replace(R.id.rootLayout, new Phonebook());
                ft.commit();
            }
        }
    }
}