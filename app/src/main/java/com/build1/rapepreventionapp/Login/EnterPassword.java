package com.build1.rapepreventionapp.Login;

import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class EnterPassword extends AppCompatActivity {

    private ImageView loading;
    AnimationDrawable animation;

    FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    EditText password;
    String email;

    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        password = (EditText)findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        loading = (ImageView) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        btnLogin = (Button) findViewById(R.id.btnLogin);
        email = getIntent().getStringExtra("email").toString();

    }

    public void btnOnClickLogin (View v) {

        animation.start();
        loading.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);

        mAuth.signInWithEmailAndPassword(email,password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String token_id = FirebaseInstanceId.getInstance().getToken();
                    String current_id = mAuth.getCurrentUser().getUid();

                    Map<String, Object> tokenMap = new HashMap<>();
                    tokenMap.put("token_id", token_id);

                    mFirestore.collection("Users").document(current_id).update(tokenMap).addOnSuccessListener(EnterPassword.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            btnLogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                            animation.stop();

                            finish();
                        }
                    });
                } else {
                    btnLogin.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    animation.stop();
                    Toast.makeText(EnterPassword.this, "Incorrect username or password.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
