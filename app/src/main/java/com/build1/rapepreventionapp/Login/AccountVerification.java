package com.build1.rapepreventionapp.Login;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Home.BottomNavigation;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JEMYLA VELILLA on 30/01/2018.
 */

public class AccountVerification extends AppCompatActivity {
    private FirebaseAuth mAuth;

    String mUserId;
    private FirebaseFirestore mFirestore;

    TextView tvUser;

    private ImageView loading;
    AnimationDrawable animation;

    Button btnOpenEmail;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();

        if (mAuth == null){
            return;
        }

        tvUser = (TextView) findViewById(R.id.email);
        tvUser.setText(mAuth.getCurrentUser().getEmail());
        btnOpenEmail = (Button) findViewById(R.id.btnVerify);
        btnLogout = (Button) findViewById(R.id.btnLogOut);

        loading = (ImageView) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mAuth.getCurrentUser().reload();
        if (mAuth.getCurrentUser().isEmailVerified()){
            Intent i = new Intent(this, BottomNavigation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(i);
        }
    }
    public void btnOnClickVerify (View view){
        Log.v("user", mAuth.getCurrentUser().getEmail());
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(AccountVerification.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Uri uri = Uri.parse("https://mail.google.com");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } else {
                    Toast.makeText(AccountVerification.this, "Failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void btnOnClickLogout (View view){
        animation.start();
        loading.setVisibility(View.VISIBLE);
        btnOpenEmail.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);

        Map<String, Object> tokenMapRemove = new HashMap<>();
        tokenMapRemove.put("token_id", FieldValue.delete());

        mFirestore.collection("Users").document(mUserId).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuth.signOut();

                animation.stop();
                loading.setVisibility(View.INVISIBLE);

                Intent i = new Intent(AccountVerification.this, LogIn2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(i);
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                animation.stop();
                loading.setVisibility(View.INVISIBLE);
                btnOpenEmail.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.VISIBLE);

                Toast.makeText(AccountVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
