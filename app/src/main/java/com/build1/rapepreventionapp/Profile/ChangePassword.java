package com.build1.rapepreventionapp.Profile;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Model.EditInformation;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChangePassword extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;

    EditText currentPassword, newPassword, confirmNewPassword;
    Button btnSave, btnCancel;
    TextView tvUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_change_password, container, false);

        tvUsername = (TextView) v.findViewById(R.id.username);
        tvUsername.setText(EditInformation.firstName + " " + EditInformation.lastName);

        currentPassword = (EditText) v.findViewById(R.id.editCurrentPassword);
        newPassword = (EditText) v.findViewById(R.id.editNewPassword);
        confirmNewPassword = (EditText) v.findViewById(R.id.editConfirmNewPassword);

        btnSave = (Button) v.findViewById(R.id.btnSaveChanges);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveChanges:
                if (currentPassword.getText().toString().equals("")){
                    currentPassword.setError("Password cannot be empty.");
                }  else if(newPassword.getText().toString().equals("")){
                    newPassword.setError("Password cannot be empty.");
                } else if(confirmNewPassword.getText().toString().equals("")){
                    confirmNewPassword.setError("Password cannot be empty.");
                } else {
                    //save changes for change password
                    AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), currentPassword.getText().toString());
                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        if(newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){
                                            mAuth.getCurrentUser().updatePassword(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(getActivity(), " Password updated successfully.", Toast.LENGTH_SHORT).show();
                                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                                if (fragmentManager != null) {
                                                                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                                                                    if (ft != null) {
                                                                        ft.replace(R.id.rootLayout, new Profile());
                                                                        ft.commit();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });

                                        } else {
                                            newPassword.setError("Passwords do not match.");
                                            confirmNewPassword.setError("Passwords do not match.");
                                        }
                                    } else{
                                        currentPassword.setError("Incorrect password.");
                                    }
                                }
                            });
                }

                //
                break;
            case R.id.btnCancel:
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (ft != null) {
                        ft.replace(R.id.rootLayout, new Profile());
                        ft.commit();
                    }
                }
                break;
        }

    }
}
