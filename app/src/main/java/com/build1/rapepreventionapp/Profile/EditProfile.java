package com.build1.rapepreventionapp.Profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.build1.rapepreventionapp.Home.BottomNavigation;
import com.build1.rapepreventionapp.Model.EditInformation;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends Fragment implements View.OnClickListener{

    EditText editAge, editBirthdate, editContact1, editContactNumber1 ,editContact2, editContactNumber2, editContact3, editContactNumber3;
    EditText editAddress, editFirstName, editLastName, editMobileNumber;
    Button btnSave, btnCancel;
    private ImageView loading;
    AnimationDrawable animation;

    String currentUserId, user_id;

    private StorageReference mStorage;
    private FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private CircleImageView mProfilePicture;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("images");

        imageUri = null;

        currentUserId =  mAuth.getCurrentUser().getUid();
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edit_profile, container, false);

        UserInformation userInformation = new UserInformation();

        loading = (ImageView) v.findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        mProfilePicture = (CircleImageView) v.findViewById(R.id.profilePicture);
        editAge = (EditText) v.findViewById(R.id.editTextAge);
        editBirthdate = (EditText) v.findViewById(R.id.editTextDOB);
        editContact1 = (EditText) v.findViewById(R.id.editTextCP1);
        editContactNumber1 = (EditText) v.findViewById(R.id.editTextCP1Num);
        editContact2 = (EditText) v.findViewById(R.id.editTextCP2);
        editContactNumber2 = (EditText) v.findViewById(R.id.editTextCP2Num);
        editContact3 = (EditText) v.findViewById(R.id.editTextCP3);
        editContactNumber3 = (EditText) v.findViewById(R.id.editTextCP3Num);
        editAddress = (EditText) v.findViewById(R.id.editTextCAdd);
        editFirstName = (EditText) v.findViewById(R.id.editTextFN);
        editLastName = (EditText) v.findViewById(R.id.editTextLN);
        editMobileNumber = (EditText) v.findViewById(R.id.editTextMobileNumber);

        editAge.setText(Integer.toString(EditInformation.age));
        editBirthdate.setText(EditInformation.birthday);
        editContactNumber1.setText(EditInformation.contactNumber1);
        editContactNumber2.setText(EditInformation.contactNumber2);
        editContactNumber3.setText(EditInformation.contactNumber3);
        editContact1.setText(EditInformation.contactPerson1);
        editContact2.setText(EditInformation.contactPerson2);
        editContact3.setText(EditInformation.contactPerson3);
        editAddress.setText(EditInformation.currentAddress);
        editFirstName.setText(EditInformation.firstName);
        editLastName.setText(EditInformation.lastName);
        editMobileNumber.setText(EditInformation.mobileNumber);


        mProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        btnSave = (Button) v.findViewById(R.id.btnSaveChanges);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);


        animation.start();
        loading.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);

        mFirestore.collection("Users").document(currentUserId).get().addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                animation.stop();
                loading.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);

                RequestOptions placeHolderOptions = new RequestOptions();
                placeHolderOptions.placeholder(R.drawable.default_profile);

                Glide.with(container.getContext()).setDefaultRequestOptions(placeHolderOptions).load(documentSnapshot.getString("image")).into(mProfilePicture);

            }
        });

        editBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = month +"/"+ day + "/" + year;
                editBirthdate.setText(date);
            }
        };

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

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

                String mobileNumPattern = "^(09|\\+639)\\d{9}$";

                if (editFirstName.getText().toString().trim().equals("")){
                    editFirstName.setError("Field Required");
                }else if (editLastName.getText().toString().trim().equals("")){
                    editLastName.setError("Field Required");
                }else if (editAge.getText().toString().trim().equals("")){
                    editAge.setError("Field Required");
                }else if (editBirthdate.getText().toString().trim().equals("")){
                    editBirthdate.setError("Field Required");
                }else if (editMobileNumber.getText().toString().trim().equals("")){
                    editAge.setError("Field Required");
                }else if (editAddress.getText().toString().trim().equals("")){
                    editAddress.setError("Field Required");
                }else if (editContact1.getText().toString().trim().equals("")){
                    editContact1.setError("Field Required");
                }else if (!editContactNumber1.getText().toString().matches(mobileNumPattern)){
                    editContactNumber1.setError("Invalid mobile number");
                }else if (editContact2.getText().toString().trim().equals("")){
                    editContact2.setError("Field Required");
                }else if (!editContactNumber2.getText().toString().matches(mobileNumPattern)){
                    editContactNumber2.setError("Invalid mobile number");
                } else if (editContact3.getText().toString().trim().equals("")){
                    editContact3.setError("Field Required");
                }else if (!editContactNumber3.getText().toString().matches(mobileNumPattern)){
                    editContactNumber3.setError("Invalid mobile number");
                } else {
                    user_id = mAuth.getCurrentUser().getUid();

                    if (imageUri != null){

                        StorageReference user_profile = mStorage.child(user_id + ".jpg");

                        user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {
                                if (uploadTask.isSuccessful()){
                                    String download_url = uploadTask.getResult().getDownloadUrl().toString();

                                    //String token_id = FirebaseInstanceId.getInstance().getToken();

                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("first_name", editFirstName.getText().toString());
                                    userMap.put("last_name", editLastName.getText().toString());
                                    userMap.put("age", editAge.getText().toString());
                                    userMap.put("birthdate", editBirthdate.getText().toString());
                                    userMap.put("current_address", editAddress.getText().toString());
                                    userMap.put("contact_person1", editContact1.getText().toString());
                                    userMap.put("contact_person2", editContact2.getText().toString());
                                    userMap.put("contact_person3", editContact3.getText().toString());
                                    userMap.put("contact_number1", editContactNumber1.getText().toString());
                                    userMap.put("contact_number2", editContactNumber2.getText().toString());
                                    userMap.put("contact_number3", editContactNumber3.getText().toString());
                                    userMap.put("mobile_number", editMobileNumber.getText().toString());
                                    userMap.put("image", download_url);
                                    //userMap.put("token_id", token_id);

                                    mFirestore. collection("Users").document(user_id).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Updated Successfully",
                                                    Toast.LENGTH_SHORT).show();

                                            animation.stop();
                                            loading.setVisibility(View.INVISIBLE);
                                            btnSave.setVisibility(View.VISIBLE);
                                            btnCancel.setVisibility(View.VISIBLE);

                                            redirectToProfile();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Error" + uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    animation.stop();
                                    loading.setVisibility(View.INVISIBLE);
                                    btnSave.setVisibility(View.VISIBLE);
                                    btnCancel.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                   } else { //else walang image update
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("first_name", editFirstName.getText().toString());
                        userMap.put("last_name", editLastName.getText().toString());
                        userMap.put("age", editAge.getText().toString());
                        userMap.put("birthdate", editBirthdate.getText().toString());
                        userMap.put("current_address", editAddress.getText().toString());
                        userMap.put("contact_person1", editContact1.getText().toString());
                        userMap.put("contact_person2", editContact2.getText().toString());
                        userMap.put("contact_person3", editContact3.getText().toString());
                        userMap.put("contact_number1", editContactNumber1.getText().toString());
                        userMap.put("contact_number2", editContactNumber2.getText().toString());
                        userMap.put("contact_number3", editContactNumber3.getText().toString());
                        userMap.put("mobile_number", editMobileNumber.getText().toString());
                        //userMap.put("token_id", token_id);

                        mFirestore. collection("Users").document(user_id).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Updated Successfully",
                                        Toast.LENGTH_SHORT).show();

                                animation.stop();
                                loading.setVisibility(View.INVISIBLE);
                                btnSave.setVisibility(View.VISIBLE);
                                btnCancel.setVisibility(View.VISIBLE);

                                redirectToProfile();
                            }
                        });
                    }
                }
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
    public void redirectToProfile(){
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, new Profile());
                ft.commit();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){
            if(data.getData() != null){
                imageUri = data.getData();
                mProfilePicture.setImageURI(imageUri);
            }
        }
    }

}
