package com.build1.rapepreventionapp.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Base.Welcome;
import com.build1.rapepreventionapp.Home.BottomNavigation;
import com.build1.rapepreventionapp.R;
import com.build1.rapepreventionapp.Registration.RegisterStep1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private ImageView loading;
    AnimationDrawable animation;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private Button btnLogin;

    private EditText email;
    private EditText password;

    String username;
    String passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.buttonLogin);

        loading = (ImageView) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        email = (EditText) findViewById(R.id.editTextUserName);
        password = (EditText) findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        Log.v("message","onCreate");

    }

    public void btnOnClickLogin(View v){

        animation.start();
        loading.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);

        username = email.getText().toString();
        passwordText = password.getText().toString();
        String pattern = "^(09|\\+639)\\d{9}$";
        if(isNetworkAvailable()){
            if(TextUtils.isEmpty(username) || TextUtils.isEmpty(passwordText)){
                Toast.makeText(this, "Enter both values", Toast.LENGTH_LONG).show();
            } else if (username.matches(pattern)){

                CollectionReference usersRef = mFirestore.collection("Users");
                Query query = usersRef.whereEqualTo("mobile_number", username);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().size() == 0){
                                btnLogin.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.INVISIBLE);
                                animation.stop();

                                Toast.makeText(Login.this, "Incorrect username or password.", Toast.LENGTH_LONG).show();
                            } else {
                                for (DocumentSnapshot document : task.getResult()) {
                                    username = document.getData().get("email").toString();
                                    login();
                                }

                            }
                        } else {
                            btnLogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                            animation.stop();
                            Log.v("result", "Error getting documents: ", task.getException());
                            Toast.makeText(Login.this, "Slow or no internet connection.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                login();
            }
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
            animation.stop();
            Toast.makeText(Login.this, "Slow or no internet connection.", Toast.LENGTH_LONG).show();
        }

    }

    public void btnOnClickSignUp(View view) {
        Intent i = new Intent(getApplicationContext(), RegisterStep1.class);
        startActivity(i);
    }
    public void btnOnClickForgotPass(View view) {
        Intent i = new Intent(getApplicationContext(), ForgotPassStep1.class);
        startActivity(i);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void login(){
        mAuth.signInWithEmailAndPassword(username,passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String token_id = FirebaseInstanceId.getInstance().getToken();
                    String current_id = mAuth.getCurrentUser().getUid();

                    Map<String, Object> tokenMap = new HashMap<>();
                    tokenMap.put("token_id", token_id);

                    mFirestore.collection("Users").document(current_id).update(tokenMap).addOnSuccessListener(Login.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            btnLogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                            animation.stop();

                            sendToMain();
                        }
                    });
                } else {
                    btnLogin.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    animation.stop();
                    Toast.makeText(Login.this, "Incorrect username or password.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendToMain(){
        if(!mAuth.getCurrentUser().isEmailVerified()){
            Intent intent = new Intent(this, AccountVerification.class);
            finish();
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, BottomNavigation.class);
            finish();
            startActivity(intent);
        }
    }
}
