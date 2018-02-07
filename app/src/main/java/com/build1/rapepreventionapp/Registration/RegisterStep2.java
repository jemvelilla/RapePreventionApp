package com.build1.rapepreventionapp.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;

public class RegisterStep2 extends AppCompatActivity {
    private UserInformation info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);
    }
    public void btnOnClickMobNum(View v){
        EditText mobNum = (EditText) findViewById(R.id.editTextMobNum);
        String mobileNum = mobNum.getText().toString().trim();
        String mobileNumPattern = "^(09|\\+639)\\d{9}$";

        if (mobileNum.matches(mobileNumPattern)){
            info = (UserInformation) getIntent().getSerializableExtra("info");
            info.setMobileNumber(mobNum.getText().toString());

            Intent i = new Intent(getApplicationContext(), RegisterStep3.class);
            i.putExtra("info", info);
            startActivity(i);

        }else{
            mobNum.setError("Invalid Mobile Number");
        }
    }
    public void btnOnClickLoginPage(View v){
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }


}