package com.build1.rapepreventionapp.Contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Contacts extends Fragment implements View.OnClickListener{
    int i; //counter
    String contactName; //contact name preferences
    String contactNumber; //contact number preferences

    public static List<String> contactNumberId; //array of saved number id
    public static List<String> numbersOfAppUsers; //array of app users

    List<String> nameList = new ArrayList<>(); //array of saved names
    List<String> numList = new ArrayList<>(); //array of saved numbers
    String[] names; //array where separated name preferences are stored
    String[] numbers; //array where separated number preferences are stored
    String phoneName, phoneNumber; //name and number in the phonebook

    private FirebaseFirestore mFirestore;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
        contactName = preferences.getString("contactNames", "");
        contactNumber = preferences.getString("contactNumbers", "");

        Log.v("view", "onCreate");

        //load phonebook and app users
        //loadContacts();
    }

    @Override
    public void onStart() {
        super.onStart();

        //loadContactsToPreferences();
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
<<<<<<< HEAD
            ids = contactId.split(",");

            for(String id: ids){
                idList.add(id);
                Log.v("message", id);
            }
=======
>>>>>>> parent of 73e5702... Get Contact Id

            for (int i=0; i < names.length; i++){
                nameList.add(names[i]);
                Log.v("message", names[i]);
                numList.add(numbers[i]);
<<<<<<< HEAD
                Log.v("message", numbers[i]);
=======
>>>>>>> parent of 73e5702... Get Contact Id
            }

            //get id of selected contact numbers
            //getContactId();

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

<<<<<<< HEAD
//    public void loadContacts(){
//        Cursor cursor = getActivity().getContentResolver()
//                .query(ContactsContract.CommonDataKinds.Phone
//                        .CONTENT_URI, null,null,null,null);
//        getActivity().startManagingCursor(cursor);
//
//        if(cursor.getCount() > 0){
//            int i = 0;
//            while (cursor.moveToNext()){
//                phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                numbersOfAppUsers = new ArrayList<>();
//
//                /** check if phone number exists in the app firebase**/
//                CollectionReference usersRef = mFirestore.collection("Users");
//                com.google.firebase.firestore.Query query = usersRef.whereEqualTo("mobile_number", phoneNumber);
//                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if(task.getResult().size() == 0){
//                                Log.v("message", "from contacts: 0");
//                                numbersOfAppUsers.add("");
//                            } else {
//                                for (DocumentSnapshot document : task.getResult()) {
//                                    String user_id = document.getId();
//
//                                    UserModel userModel = document.toObject(UserModel.class).withId(user_id);
//
//                                    numbersOfAppUsers.add(document.getData().get("mobile_number").toString());
//                                }
//                            }
//                        } else {
//                            Log.v("result", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//                /**end**/
//
//            }
//        }
//    }
=======
    public void loadContacts(){
        Cursor cursor = getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, null,null,null,null);
        getActivity().startManagingCursor(cursor);

        if(cursor.getCount() > 0){
            int i = 0;
            while (cursor.moveToNext()){
                phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                numbersOfAppUsers = new ArrayList<>();
                /** check if phone number exists in the app firebase**/
                CollectionReference usersRef = mFirestore.collection("Users");
                com.google.firebase.firestore.Query query = usersRef.whereEqualTo("mobile_number", phoneNumber);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() == 0){
                                numbersOfAppUsers.add("");
                            } else {
                                for (DocumentSnapshot document : task.getResult()) {
                                    numbersOfAppUsers.add(document.getData().get("mobile_number").toString());
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

    public void loadContactsToPreferences(){

        StringBuilder numberBuilder = new StringBuilder();
        for (String numberId : contactNumberId){
            numberBuilder.append(numberId);
            numberBuilder.append(',');

            SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Emergency Contacts ID", numberBuilder.toString());
            editor.commit();
        }
    }

    public void getContactId(){
        i = 0;
        contactNumberId = new ArrayList<>();
        while (i < numList.size()){
            CollectionReference usersRef = mFirestore.collection("Users");
            com.google.firebase.firestore.Query query = usersRef.whereEqualTo("mobile_number", phoneNumber);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            contactNumberId.add(document.getId());
                            i++;
                        }
                    } else {
                        Log.v("result", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }
>>>>>>> parent of 73e5702... Get Contact Id
}