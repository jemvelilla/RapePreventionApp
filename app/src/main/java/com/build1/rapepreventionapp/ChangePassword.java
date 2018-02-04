package com.build1.rapepreventionapp;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

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
        tvUsername.setText(mAuth.getCurrentUser().getEmail());

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
                //save changes for change password

                //
                break;
            case R.id.btnCancel:
                break;
        }

    }
}
