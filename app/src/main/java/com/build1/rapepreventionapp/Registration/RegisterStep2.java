package com.build1.rapepreventionapp.Registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.build1.rapepreventionapp.Login.LogIn2;
import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RegisterStep2 extends AppCompatActivity {
    private Button btnNext;
    private ImageView loading;
    AnimationDrawable animation;

    String savedAccount;

    private UserInformation info;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        btnNext = (Button) findViewById(R.id.buttonNexttwo);

        loading = (ImageView) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        mFirestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        savedAccount = preferences.getString("savedAccount", "");

    }
    public void btnOnClickMobNum(View v){

        animation.start();
        loading.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.INVISIBLE);

        final EditText mobNum = (EditText) findViewById(R.id.editTextMobNum);
        final String mobileNum = mobNum.getText().toString().trim();
        String mobileNumPattern = "^(09|\\+639)\\d{9}$";

        if (mobileNum.matches(mobileNumPattern)){

            CollectionReference usersRef = mFirestore.collection("Users");
            com.google.firebase.firestore.Query query = usersRef.whereEqualTo("mobile_number", mobileNum);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        loading.setVisibility(View.INVISIBLE);
                        animation.stop();
                        btnNext.setVisibility(View.VISIBLE);

                        if(task.getResult().size() == 0){
                            info = (UserInformation) getIntent().getSerializableExtra("info");
                            info.setMobileNumber(mobileNum);

                            Intent i = new Intent(getApplicationContext(), RegisterStep3.class);
                            i.putExtra("info", info);
                            startActivity(i);
                        } else {
                            for (DocumentSnapshot document : task.getResult()) {
                                mobNum.setError("Mobile number already in use.");
                            }
                        }
                    } else {
                        loading.setVisibility(View.INVISIBLE);
                        animation.stop();
                        btnNext.setVisibility(View.VISIBLE);
                        Log.v("result", "Error getting documents: ", task.getException());
                    }
                }
            });

        }else{
            loading.setVisibility(View.INVISIBLE);
            animation.stop();
            btnNext.setVisibility(View.VISIBLE);
            mobNum.setError("Invalid Mobile Number");
        }
    }
    public void btnOnClickLoginPage(View v){

        if (!savedAccount.isEmpty()){
            Intent i = new Intent(RegisterStep2.this, LogIn2.class);
            startActivity(i);
        } else {
            Intent i = new Intent(RegisterStep2.this, Login.class);
            startActivity(i);
        }
    }


}
