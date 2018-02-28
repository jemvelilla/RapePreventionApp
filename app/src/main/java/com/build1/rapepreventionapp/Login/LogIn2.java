package com.build1.rapepreventionapp.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Home.BottomNavigation;
import com.build1.rapepreventionapp.R;
import com.build1.rapepreventionapp.Registration.RegisterStep1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LogIn2 extends AppCompatActivity {

    private ImageView loading;
    AnimationDrawable animation;

    String savedAccount;
    String email;

    FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    Button btnName, btnLogIn, btnCreateAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in2);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        loading = (ImageView) findViewById(R.id.loading);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        btnName = (Button) findViewById(R.id.btnName);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);

        btnName.setVisibility(View.INVISIBLE);
        btnLogIn.setVisibility(View.INVISIBLE);
        btnCreateAccount.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        animation.start();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        savedAccount = preferences.getString("savedAccount", "");

        Log.v("saved", savedAccount);
        if(!savedAccount.isEmpty()){
            mFirestore.collection("Users").document(savedAccount).get().addOnSuccessListener(LogIn2.this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    loading.setVisibility(View.INVISIBLE);
                    animation.stop();
                    btnName.setVisibility(View.VISIBLE);
                    btnLogIn.setVisibility(View.VISIBLE);
                    btnCreateAccount.setVisibility(View.VISIBLE);

                    btnName.setText(documentSnapshot.getString("first_name") + " " + documentSnapshot.getString("last_name"));
                    email = documentSnapshot.getString("email");
                }
            });
        } else {
            return;
        }
    }

    public void btnOnClickLogIn (View v) {
        Intent i = new Intent(this, EnterPassword.class);
        finish();
        i.putExtra("email", email);
        startActivity(i);
    }

    public void btnOnClickLogInAnotherAccnt (View v){
        Intent i = new Intent(this, Login.class); // Login
        startActivity(i);
    }

    public void btnOnClickCreateAccnt (View v){
        Intent i = new Intent(this, RegisterStep1.class); // RegisterStep1
        startActivity(i);
    }
}
