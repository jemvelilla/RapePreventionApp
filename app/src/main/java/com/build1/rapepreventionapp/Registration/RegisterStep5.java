package com.build1.rapepreventionapp.Registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Login.LogIn2;
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
import com.google.firebase.iid.FirebaseInstanceId;
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
    private Button btnFinish;
    private ImageView loading;
    AnimationDrawable animation;

    private UserInformation info;
    FirebaseDatabase database;
    DatabaseReference user;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private FirebaseFirestore mFirestore;

    private CircleImageView mImageButton;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    String savedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step5);

        btnFinish = (Button) findViewById(R.id.btnFinish);

        loading = (ImageView) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        imageUri = null;

        mImageButton = (CircleImageView) findViewById(R.id.profilePicture);

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

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        savedAccount = preferences.getString("savedAccount", "");

    }
    public void btnOnClickFinish(View v){

        if (imageUri != null){

            animation.start();
            loading.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.INVISIBLE);

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

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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

                                        String token_id = FirebaseInstanceId.getInstance().getToken();

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
                                        userMap.put("token_id", token_id);

                                        mFirestore. collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                sendToAccountCreated();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterStep5.this, "Error" + uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        loading.setVisibility(View.INVISIBLE);
                                        animation.stop();
                                        btnFinish.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterStep5.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            loading.setVisibility(View.INVISIBLE);
                            animation.stop();
                            btnFinish.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }

    public void btnOnClickLoginPage(View v){

        if (!savedAccount.isEmpty()){
            Intent i = new Intent(RegisterStep5.this, LogIn2.class);
            startActivity(i);
        } else {
            Intent i = new Intent(RegisterStep5.this, Login.class);
            startActivity(i);
        }
    }

    private void sendToAccountCreated(){
        Intent intent = new Intent(getApplicationContext(), Slides.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            mImageButton.setImageURI(imageUri);
        }
    }
}
