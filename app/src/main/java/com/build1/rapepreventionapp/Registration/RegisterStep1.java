package com.build1.rapepreventionapp.Registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.build1.rapepreventionapp.Login.LogIn2;
import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterStep1 extends AppCompatActivity {
    private Button btnNext;
    private ImageView loading;
    AnimationDrawable animation;

    String savedAccount;

    FirebaseAuth auth;
    EditText emailAdd;

    UserInformation info = new UserInformation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step1);

        btnNext = (Button) findViewById(R.id.buttonNextone);

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        savedAccount = preferences.getString("savedAccount", "");

        loading = (ImageView) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        auth = FirebaseAuth.getInstance();

    }
    public void btnOnClickOne(View v){

        dismissKeyboard();

        animation.start();
        loading.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.INVISIBLE);

        final EditText emailAdd = (EditText) findViewById(R.id.editTextEmailAdd);

        final String email = emailAdd.getText().toString().trim();
        final String emailPatternGbiz = "[a-zA-Z0-9._-]+@[a-z]+[._-]+[a-z]+[._-]+[a-z]+[._-]+[a-z]+";
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+[._-]+[a-z]+";

        if (email.matches(emailPattern) || email.matches(emailPatternGbiz)){

            if(isNetworkAvailable()){

                auth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                        loading.setVisibility(View.INVISIBLE);
                        animation.stop();
                        btnNext.setVisibility(View.VISIBLE);

                        boolean check = !task.getResult().getProviders().isEmpty();

                        if (!check){
                            info.setEmail(emailAdd.getText().toString());

                            Intent i = new Intent(getApplicationContext(), RegisterStep2.class);
                            i.putExtra("info", info);
                            startActivity(i);
                        } else {
                            loading.setVisibility(View.INVISIBLE);
                            animation.stop();
                            btnNext.setVisibility(View.VISIBLE);

                            emailAdd.setError("Email address already exists.");
                        } //end check
                    } //end on complete
                }); //end fetch
            } else{
                loading.setVisibility(View.INVISIBLE);
                animation.stop();
                btnNext.setVisibility(View.VISIBLE);

                Toast.makeText(RegisterStep1.this, "Slow or no internet connection.", Toast.LENGTH_LONG).show();
            }//end network available
        }else {
            loading.setVisibility(View.INVISIBLE);
            animation.stop();
            btnNext.setVisibility(View.VISIBLE);

            emailAdd.setError("Invalid Email Address");
        } //end check email address format
    }

    public void btnOnClickLoginPage(View v){

        if (!savedAccount.isEmpty()){
            Intent i = new Intent(RegisterStep1.this, LogIn2.class);
            startActivity(i);
        } else {
            Intent i = new Intent(RegisterStep1.this, Login.class);
            startActivity(i);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void dismissKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
