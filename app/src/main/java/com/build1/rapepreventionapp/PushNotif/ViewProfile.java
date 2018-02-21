package com.build1.rapepreventionapp.PushNotif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.build1.rapepreventionapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by JEMYLA VELILLA on 16/02/2018.
 */

public class ViewProfile extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    TextView tvAge, tvBirthdate, tvContact1, tvContactNumber1 ,tvContact2, tvContactNumber2, tvContact3, tvContactNumber3;
    TextView tvAddress, tvName, tvPhoneNum;
    private CircleImageView mProfilePicture;

    String dataFrom;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        mFirestore = FirebaseFirestore.getInstance();

        dataFrom = getIntent().getStringExtra("user_id");

        tvName = (TextView) findViewById(R.id.name);
        tvAge = (TextView) findViewById(R.id.age);
        tvAddress = (TextView) findViewById(R.id.address);
        tvBirthdate = (TextView) findViewById(R.id.birthday);
        tvPhoneNum = (TextView) findViewById(R.id.phoneNum);
        tvContact1 = (TextView) findViewById(R.id.eContacts1);
        tvContactNumber1 = (TextView) findViewById(R.id.eContactsNum1);
        tvContact2 = (TextView) findViewById(R.id.eContacts2);
        tvContactNumber2 = (TextView) findViewById(R.id.eContactsNum2);
        tvContact3 = (TextView) findViewById(R.id.eContacts3);
        tvContactNumber3 = (TextView) findViewById(R.id.eContactsNum3);
        mProfilePicture = (CircleImageView) findViewById(R.id.profilePicture);
    }

    public void onStart(){
        super.onStart();

        mFirestore.collection("Users").document(dataFrom).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                tvName.setText(documentSnapshot.getString("first_name") + " " + documentSnapshot.getString("last_name"));
                tvAge.setText(documentSnapshot.getString("age") + "years old");
                tvBirthdate.setText(documentSnapshot.getString("birthdate"));
                tvContactNumber1.setText(documentSnapshot.getString("contact_number1"));
                tvContactNumber2.setText(documentSnapshot.getString("contact_number2"));
                tvContactNumber3.setText(documentSnapshot.getString("contact_number3"));
                tvContact1.setText(documentSnapshot.getString("contact_person1"));
                tvContact2.setText(documentSnapshot.getString("contact_person2"));
                tvContact3.setText(documentSnapshot.getString("contact_person3"));
                tvAddress.setText(documentSnapshot.getString("current_address"));
                tvPhoneNum.setText(documentSnapshot.getString("mobile_number"));

                RequestOptions placeHolderOptions = new RequestOptions();
                placeHolderOptions.placeholder(R.drawable.default_profile);

                Glide.with(getApplicationContext()).setDefaultRequestOptions(placeHolderOptions).load(documentSnapshot.getString("image")).into(mProfilePicture);

            }
        });

    }
}
