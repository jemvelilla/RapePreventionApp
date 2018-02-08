package com.build1.rapepreventionapp.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
    private UserInformation info;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);
        mFirestore = FirebaseFirestore.getInstance();
    }
    public void btnOnClickMobNum(View v){
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
                        Log.v("result", "Error getting documents: ", task.getException());
                    }
                }
            });

        }else{
            mobNum.setError("Invalid Mobile Number");
        }
    }
    public void btnOnClickLoginPage(View v){
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }


}
