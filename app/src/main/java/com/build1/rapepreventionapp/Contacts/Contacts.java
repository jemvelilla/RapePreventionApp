package com.build1.rapepreventionapp.Contacts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Login.ForgotPassStep2;
import com.build1.rapepreventionapp.Model.UserModel;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Contacts extends Fragment implements View.OnClickListener{
    public static HashMap<String, String> usersMap;

    String contactName; //contact name preferences
    String contactNumber; //contact number preferences
    String contactId;
    private static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mContactsPermissionGranted = false;

    List<String> nameList = new ArrayList<>(); //array of saved names
    List<String> numList = new ArrayList<>(); //array of saved numbers
    List<String> idList = new ArrayList<>(); //array of saved numbers

    String[] names; //array where separated name preferences are stored
    String[] numbers; //array where separated number preferences are stored
    String[] ids; //array where separated number preferences are stored

    String phoneName, phoneNumber; //name and number in the phonebook

    private Button btnContacts;
    private Button btnAdd;

    private FirebaseFirestore mFirestore;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContactsPermission();

        mFirestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
        contactName = preferences.getString("contactNames", "");
        contactNumber = preferences.getString("contactNumbers", "");
        contactId = preferences.getString("contactIds", "");

        Log.v("view", "onCreate");
        //load phonebook and app users
        if (mContactsPermissionGranted == true){
            loadContacts();
        }else{

        }
<<<<<<< HEAD


=======
>>>>>>> 2b7e2c6fa10b63fe85149abf740bd9b33a289569

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_contacts, container, false);
        btnContacts = (Button) v.findViewById(R.id.connBtn);
        btnContacts.setOnClickListener(this);

        if(!contactName.isEmpty() && !contactNumber.isEmpty()){
            v = inflater.inflate(R.layout.activity_contactlist, container, false);
            btnAdd = (Button) v.findViewById(R.id.addBtn);
            btnAdd.setOnClickListener(this);

            names = contactName.split(",");
            numbers = contactNumber.split(",");
            ids = contactId.split(",");

            for (int i=0; i < names.length; i++){
                nameList.add(names[i]);
                Log.v("message", names[i]);
                numList.add(numbers[i]);
                Log.v("message", numbers[i]);
                idList.add(ids[i]);
                Log.v("message", ids[i]);
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
                    editor.remove("contactIds");
                    editor.commit();

                    nameList.remove(nameList.get(position));
                    numList.remove(numList.get(position));
                    idList.remove(idList.get(position));
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

                    StringBuilder idBuilder = new StringBuilder();
                    for (String ids : idList){
                        idBuilder.append(ids);
                        idBuilder.append(',');

                        preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putString("contactIds", idBuilder.toString());
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
<<<<<<< HEAD
            Log.d("pandegbug", "onClick: true");
            if (fragmentManager != null) {
                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                if (ft != null) {
                    ft.replace(R.id.rootLayout, new Phonebook());
                    ft.commit();
                }
            }
=======
        Log.d("pandegbug", "onClick: true");
        if (fragmentManager != null) {
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, new Phonebook());
                ft.commit();
            }
        }
>>>>>>> 2b7e2c6fa10b63fe85149abf740bd9b33a289569

    }

    public void loadContacts(){
        Cursor cursor = getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, null,null,null,null);
        getActivity().startManagingCursor(cursor);

        usersMap = new HashMap<String, String>();

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){

                phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                phoneNumber = phoneNumber.replace("+63", "0");
                Log.v("phone", phoneNumber);

                /** check if phone number exists in the app firebase**/
                CollectionReference usersRef = mFirestore.collection("Users");
                com.google.firebase.firestore.Query query = usersRef.whereEqualTo("mobile_number", phoneNumber);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(btnAdd != null) btnAdd.setEnabled(true);
                        else btnContacts.setEnabled(true);

                        if (task.isSuccessful()) {

                            if(task.getResult().size() == 0){
                                usersMap.put("number", "no id");
                            } else {
                                for (DocumentSnapshot document : task.getResult()) {
                                    String phone_number = document.getData().get("mobile_number").toString();
                                    String user_id = document.getId();
                                    usersMap.put(phone_number, user_id);
                                }
                            }
                        } else {
                            Log.v("result", "Error getting documents: ", task.getException());
                        }
                    }
                });
                /**end**/
            }
        }
    }

    public void getContactsPermission(){
        //Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {
                Manifest.permission.READ_CONTACTS,
                //Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){


            mContactsPermissionGranted = true;

        }else{
            ActivityCompat.requestPermissions(getActivity(),permissions, CONTACTS_PERMISSION_REQUEST_CODE);
        }
    }
}