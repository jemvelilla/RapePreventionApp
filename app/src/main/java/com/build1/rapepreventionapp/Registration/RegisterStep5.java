package com.build1.rapepreventionapp.Registration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by JEMYLA VELILLA on 07/02/2018.
 */

public class RegisterStep5 extends AppCompatActivity {

    private UserInformation info;
    FirebaseDatabase database;
    DatabaseReference user;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private FirebaseFirestore mFirestore;

    private CircleImageView mImageButton;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step5);

        imageUri = null;

        mImageButton = (CircleImageView) findViewById(R.id.profilePicture);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mFirestore = FirebaseFirestore.getInstance();

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

    }
    public void btnOnClickFinish(View v){

        if (imageUri != null){

            mProgressBar.setVisibility(View.VISIBLE);

            info = (UserInformation) getIntent().getSerializableExtra("info");

            final String firstName = info.getFirstName();
            final String lastName = info.getLastName();
            final String age = Integer.toString(info.getAge());
            final String birthdate = info.getBirthday();
            final String currentAddress = info.getCurrentAddress();
            final String contactPerson1 = info.getContactPerson1();
            final String contactPerson2 = info.getContactPerson2();
            final String contactPerson3 = info.getContactPerson3();
            final String contactNumber1 = info.getContactNumber1();
            final String contactNumber2 = info.getContactNumber2();
            final String contactNumber3 = info.getContactNumber3();
            final String mobileNumber = info.getMobileNumber();
            final String email = info.getEmail();
            String password = info.getPassword();

            if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(age)
                    && !TextUtils.isEmpty(birthdate) && !TextUtils.isEmpty(currentAddress) && !TextUtils.isEmpty(contactPerson1)
                    && !TextUtils.isEmpty(contactPerson2) && !TextUtils.isEmpty(contactPerson3)
                    && !TextUtils.isEmpty(contactNumber1) && !TextUtils.isEmpty(contactNumber2)
                    && !TextUtils.isEmpty(contactNumber3) && !TextUtils.isEmpty(mobileNumber)
                    && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            final String user_id = mAuth.getCurrentUser().getUid();

                            StorageReference user_profile = mStorage.child(user_id + ".jpg");

                            user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {
                                    if (uploadTask.isSuccessful()){
                                        String download_url = uploadTask.getResult().getDownloadUrl().toString();

                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("email", email);
                                        userMap.put("first_name", firstName);
                                        userMap.put("last_name", lastName);
                                        userMap.put("age", age);
                                        userMap.put("birthdate", birthdate);
                                        userMap.put("current_address", currentAddress);
                                        userMap.put("contact_person1", contactPerson1);
                                        userMap.put("contact_person2", contactPerson2);
                                        userMap.put("contact_person3", contactPerson3);
                                        userMap.put("contact_number1", contactNumber1);
                                        userMap.put("contact_number2", contactNumber2);
                                        userMap.put("contact_number3", contactNumber3);
                                        userMap.put("mobile_number", mobileNumber);
                                        userMap.put("image", download_url);

                                        mFirestore. collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mProgressBar.setVisibility(View.INVISIBLE);
                                                sendToMain();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterStep5.this, "Error" + uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterStep5.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }
    }

    public void btnOnClickLoginPage(View v){
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }

    private void sendToMain(){
        Intent intent = new Intent(RegisterStep5.this, AccountCreated.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){
            imageUri = data.getData();
            mImageButton.setImageURI(imageUri);
        }
    }
}
