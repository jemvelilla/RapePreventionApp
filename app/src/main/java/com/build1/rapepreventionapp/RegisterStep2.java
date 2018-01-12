package com.build1.rapepreventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class RegisterStep2 extends AppCompatActivity {
    private UserInformation info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);
    }
    public void btnOnClickMobNum(View v){
        EditText mobNum = (EditText) findViewById(R.id.editTextMobNum);

        if (mobNum.getText().toString().trim().equals("")){
            mobNum.setError("Input your Mobile Number");
        }else{
            info = (UserInformation) getIntent().getSerializableExtra("info");
            info.setMobileNumber(mobNum.getText().toString());

            Intent i = new Intent(getApplicationContext(), RegisterStep3.class);
            i.putExtra("info", info);
            startActivity(i);
        }
    }
    public void btnOnClickLoginPage(View v){
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }
}
