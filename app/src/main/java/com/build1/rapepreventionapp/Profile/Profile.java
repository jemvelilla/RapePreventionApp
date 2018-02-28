package com.build1.rapepreventionapp.Profile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.build1.rapepreventionapp.Model.EditInformation;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment implements View.OnClickListener{

    String currentUserId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    TextView tvAge, tvBirthdate, tvContact1, tvContactNumber1 ,tvContact2, tvContactNumber2, tvContact3, tvContactNumber3;
    TextView tvAddress, tvName, tvPhoneNum;
    private CircleImageView mProfilePicture;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        currentUserId =  mAuth.getCurrentUser().getUid();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        ImageButton btnLogout = (ImageButton) v.findViewById(R.id.btnSettings);
        tvName = (TextView) v.findViewById(R.id.name);
        tvAge = (TextView)v.findViewById(R.id.age);
        tvAddress = (TextView) v.findViewById(R.id.address);
        tvBirthdate = (TextView) v.findViewById(R.id.birthday);
        tvPhoneNum = (TextView) v.findViewById(R.id.phoneNum);
        tvContact1 = (TextView) v.findViewById(R.id.eContacts1);
        tvContactNumber1 = (TextView) v.findViewById(R.id.eContactsNum1);
        tvContact2 = (TextView) v.findViewById(R.id.eContacts2);
        tvContactNumber2 = (TextView) v.findViewById(R.id.eContactsNum2);
        tvContact3 = (TextView) v.findViewById(R.id.eContacts3);
        tvContactNumber3 = (TextView) v.findViewById(R.id.eContactsNum3);
        mProfilePicture = (CircleImageView) v.findViewById(R.id.profilePicture);
        btnLogout.setOnClickListener(this);

        mFirestore.collection("Users").document(currentUserId).get().addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentSnapshot>() {
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

                Glide.with(container.getContext()).setDefaultRequestOptions(placeHolderOptions).load(documentSnapshot.getString("image")).into(mProfilePicture);

                EditInformation editInformation = new EditInformation();

                editInformation.firstName = documentSnapshot.getString("first_name");
                editInformation.lastName = documentSnapshot.getString("last_name");
                editInformation.age = Integer.parseInt(documentSnapshot.getString("age"));
                editInformation.birthday = documentSnapshot.getString("birthdate");
                editInformation.contactNumber1 = documentSnapshot.getString("contact_number1");
                editInformation.contactNumber2 = documentSnapshot.getString("contact_number2");
                editInformation.contactNumber3 = documentSnapshot.getString("contact_number3");
                editInformation.contactPerson1 = documentSnapshot.getString("contact_person1");
                editInformation.contactPerson2 = documentSnapshot.getString("contact_person2");
                editInformation.contactPerson3 = documentSnapshot.getString("contact_person3");
                editInformation.currentAddress = documentSnapshot.getString("current_address");
                editInformation.mobileNumber = documentSnapshot.getString("mobile_number");
                editInformation.email = documentSnapshot.getString("email");
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, new Settings());
                ft.commit();
            }
        }
    }
}
