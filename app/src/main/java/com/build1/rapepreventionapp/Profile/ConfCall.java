package com.build1.rapepreventionapp.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;

public class ConfCall extends Fragment implements View.OnClickListener{

    Switch switchAutomatedCall;
    EditText editPhoneNumber;
    Button btnSaveChanges, btnCancel, btnEditNumber;

    String automatedCall;
    String automatedCallState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
        automatedCall = preferences.getString("automatedCall", "");
        automatedCallState = preferences.getString("automatedCallState", "");

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_conf_call, container, false);

        switchAutomatedCall = (Switch) v.findViewById(R.id.switch1);
        editPhoneNumber = (EditText) v.findViewById(R.id.editTextConfCall);
        btnSaveChanges = (Button) v.findViewById(R.id.btnSaveChanges);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnEditNumber = (Button) v.findViewById(R.id.btnEditNumber);

        btnSaveChanges.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnEditNumber.setOnClickListener(this);
        switchAutomatedCall.setOnClickListener(this);

        if(automatedCallState.equals("on")){
            switchAutomatedCall.setChecked(true);
        } else {
            switchAutomatedCall.setChecked(false);
        }

        if(!automatedCall.isEmpty()){
            editPhoneNumber.setText(automatedCall);
        }

        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.btnEditNumber:

                editPhoneNumber.setEnabled(true);
                btnEditNumber.setVisibility(View.INVISIBLE);
                btnSaveChanges.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);

                break;

            case R.id.btnSaveChanges:

                String mobileNumPattern = "^(09|\\+639)\\d{9}$";

                if(!editPhoneNumber.getText().toString().equals("")){

                    if(editPhoneNumber.getText().toString().matches(mobileNumPattern)){
                        SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("automatedCall",  editPhoneNumber.getText().toString());
                        editor.commit();

                        Toast.makeText(getActivity(), "SUCCESSFULLY UPDATED.", Toast.LENGTH_SHORT).show();

                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null) {
                            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                            if (ft != null) {
                                ft.replace(R.id.rootLayout, new Profile());
                                ft.commit();
                            }
                        }
                    } else {
                        editPhoneNumber.setError("Invalid mobile number.");

                    }
                } else {
                    editPhoneNumber.setError("Phone number cannot be empty.");
                }

                break;
            case R.id.btnCancel:

                editPhoneNumber.setEnabled(false);
                btnEditNumber.setVisibility(View.VISIBLE);
                btnSaveChanges.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.INVISIBLE);

                break;

            case R.id.switch1:

                if(switchAutomatedCall.isChecked()){
                    SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                     editor.putString("automatedCallState", "on");
                    editor.commit();
                }
                else {
                    SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("automatedCallState", "off");
                    editor.commit();
                }

                break;
        }
    }
}
