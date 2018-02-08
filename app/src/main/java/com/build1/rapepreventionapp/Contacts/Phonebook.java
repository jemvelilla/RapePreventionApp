package com.build1.rapepreventionapp.Contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.Model.UserModel;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JEMYLA VELILLA on 05/02/2018.
 */

public class Phonebook extends Fragment implements View.OnClickListener{
    Button btnAddContacts, btnCancel;

    List<String> contactName = new ArrayList<>();
    List<String> contactNumber = new ArrayList<>();
    List<String> isChecked = new ArrayList<>();
    List<String> storedName = new ArrayList<>();
    List<String> storedNumber = new ArrayList<>();
    List<UserModel> users;
    String name, number, phoneName, phoneNumber;

    /**sending SMS**/
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private FirebaseFirestore mFirestore;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

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

    public voi
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_phonebook, container, false);
        btnAddContacts = (Button) v.findViewById(R.id.btnAddContacts);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);

        btnAddContacts.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        ListView listView  = (ListView) v.findViewById(R.id.listView);

        Cursor cursor = getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, null,null,null,null);
        getActivity().startManagingCursor(cursor);

        users = new ArrayList<>();

<<<<<<< HEAD
        final CustomAdapter adapter = new CustomAdapter(getContext(), users);
=======
        if(cursor.getCount() > 0){
            int i = 0;
            while (cursor.moveToNext()){
                phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if(Contacts.numbersOfAppUsers.contains(phoneNumber)){
                    users.add(new UserModel(false, true, phoneName, phoneNumber));
                } else {
                    users.add(new UserModel(false, false, phoneName, phoneNumber));
                }
            }
        }

        final CustomAdapter adapter = new CustomAdapter(getActivity(), users);
>>>>>>> parent of 73e5702... Get Contact Id
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserModel model = users.get(i);

                name = model.getName();
                number = model.getNumber();
                boolean isAppUser = model.isAppUser();

                if(isAppUser){

                    model.setAppUser(true);
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
                                    model.setSelected(true);
                                } else {
                                    contactName.remove(name);
                                    contactNumber.remove(number);
                                    model.setSelected(false);
                                }
                            } else{
                                if(model.isSelected()){
                                    contactName.remove(name);
                                    contactNumber.remove(number);
                                    model.setSelected(false);
                                } else {
                                    Toast.makeText(getActivity(), "You have reached the maximum of 10 contacts.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), name+" is already added.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "You have reached the maximum of 10 contacts.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sendInvitation(model.getNumber().toString());
                }

                btnAddContacts.setText("ADD ("+contactName.size()+")");

                users.set(i, model);

                //now update adapter
                adapter.updateRecords(users);

            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddContacts:
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
                break;
            case R.id.btnCancel:
                fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (ft != null) {
                        ft.replace(R.id.rootLayout, new Contacts());
                        ft.commit();
                    }
                }
                break;
        }
    }

    public void sendInvitation(String sendTo){
        String message = "It can happen to ANYONE and ANYWHERE. Download TULONG: Rape Prevention App now.";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + sendTo));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }
}
