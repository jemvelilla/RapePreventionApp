package com.build1.rapepreventionapp.Login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private FirebaseAuth.AuthStateListener authStateListener;

    String mUserId;
    DatabaseReference databaseUsers;
    private FirebaseFirestore mFirestore;

    private WebView webView;
    TextView tvUser;

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
        Map<String, Object> tokenMapRemove = new HashMap<>();
        tokenMapRemove.put("token_id", FieldValue.delete());

        mFirestore.collection("Users").document(mUserId).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuth.signOut();

                Intent i = new Intent(AccountVerification.this, LogIn2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(i);
            }
        });
    }

}
