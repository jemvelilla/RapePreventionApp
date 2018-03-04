package com.build1.rapepreventionapp.Login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by JEMYLA VELILLA on 30/01/2018.
 */

public class AccountVerification extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);

        mAuth = FirebaseAuth.getInstance();

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
        mAuth.signOut();
        Intent i = new Intent(getApplicationContext(), Login.class);
        finish();
        startActivity(i);
    }

}
