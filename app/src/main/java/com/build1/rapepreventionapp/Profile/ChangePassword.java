package com.build1.rapepreventionapp.Profile;

import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Model.EditInformation;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePassword extends Fragment implements View.OnClickListener{
    private ImageView loading;
    AnimationDrawable animation;

    private FirebaseAuth mAuth;
    String currentUserId;
    private FirebaseFirestore mFirestore;

    EditText currentPassword, newPassword, confirmNewPassword;
    Button btnSave, btnCancel;
    TextView tvUsername;
    private CircleImageView mProfilePicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        currentUserId =  mAuth.getCurrentUser().getUid();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_change_password, container, false);

        mProfilePicture = (CircleImageView) v.findViewById(R.id.profilePicture);

        loading = (ImageView) v.findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();
        animation.start();
        loading.setVisibility(View.VISIBLE);

        tvUsername = (TextView) v.findViewById(R.id.username);
        tvUsername.setText(EditInformation.firstName + " " + EditInformation.lastName);

        currentPassword = (EditText) v.findViewById(R.id.editCurrentPassword);
        newPassword = (EditText) v.findViewById(R.id.editNewPassword);
        confirmNewPassword = (EditText) v.findViewById(R.id.editConfirmNewPassword);

        btnSave = (Button) v.findViewById(R.id.btnSaveChanges);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnSave.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        mFirestore.collection("Users").document(currentUserId).get().addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                loading.setVisibility(View.INVISIBLE);
                animation.stop();
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);

                RequestOptions placeHolderOptions = new RequestOptions();
                placeHolderOptions.placeholder(R.drawable.default_profile);

                Glide.with(container.getContext()).setDefaultRequestOptions(placeHolderOptions).load(documentSnapshot.getString("image")).into(mProfilePicture);

            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveChanges:

                animation.start();
                loading.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.INVISIBLE);

                if (currentPassword.getText().toString().equals("")){
                    animation.stop();
                    loading.setVisibility(View.INVISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    currentPassword.setError("Password cannot be empty.");
                }  else if(newPassword.getText().toString().equals("")){
                    animation.stop();
                    loading.setVisibility(View.INVISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    newPassword.setError("Password cannot be empty.");
                } else if(confirmNewPassword.getText().toString().equals("")){
                    animation.stop();
                    loading.setVisibility(View.INVISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
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
                                            animation.stop();
                                            loading.setVisibility(View.INVISIBLE);
                                            btnSave.setVisibility(View.VISIBLE);
                                            btnCancel.setVisibility(View.VISIBLE);
                                            newPassword.setError("Passwords do not match.");
                                            confirmNewPassword.setError("Passwords do not match.");
                                        }
                                    } else{
                                        animation.stop();
                                        loading.setVisibility(View.INVISIBLE);
                                        btnSave.setVisibility(View.VISIBLE);
                                        btnCancel.setVisibility(View.VISIBLE);
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
