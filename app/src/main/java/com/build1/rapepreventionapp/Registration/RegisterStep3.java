package com.build1.rapepreventionapp.Registration;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.build1.rapepreventionapp.Login.LogIn2;
import com.build1.rapepreventionapp.Login.Login;
import com.build1.rapepreventionapp.Model.UserInformation;
import com.build1.rapepreventionapp.R;

import java.util.Calendar;

public class RegisterStep3 extends AppCompatActivity {
    private UserInformation info;
    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String savedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step3);
        mDisplayDate = (EditText) findViewById(R.id.editTextDOB);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterStep3.this,
                                                                        android.R.style.Theme_Holo_Dialog_MinWidth,
                                                                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = month +"/"+ day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        savedAccount = preferences.getString("savedAccount", "");

    }
    public void btnOnClickRegInfo(View v){
        EditText fName = (EditText) findViewById(R.id.editTextFN);
        EditText lName = (EditText) findViewById(R.id.editTextLN);
        EditText age = (EditText) findViewById(R.id.editTextAge);
        EditText dob = (EditText) findViewById(R.id.editTextDOB);
        EditText currAdd = (EditText) findViewById(R.id.editTextCAdd);
        EditText cP1 = (EditText) findViewById(R.id.editTextCP1);
        EditText cP1Num = (EditText) findViewById(R.id.editTextCP1Num);
        EditText cP2 = (EditText) findViewById(R.id.editTextCP2);
        EditText cP2Num = (EditText) findViewById(R.id.editTextCP2Num);
        EditText cP3 = (EditText) findViewById(R.id.editTextCP3);
        EditText cP3Num = (EditText) findViewById(R.id.editTextCP3Num);

        String cp1MobNum = cP1Num.getText().toString().trim();
        String cp2MobNum = cP2Num.getText().toString().trim();
        String cp3MobNum = cP3Num.getText().toString().trim();
        String mobileNumPattern = "^(09|\\+639)\\d{9}$";


        if (fName.getText().toString().trim().equals("")){
            fName.setError("Field Required");
        }else if (lName.getText().toString().trim().equals("")){
            lName.setError("Field Required");
        }else if (age.getText().toString().trim().equals("")){
            age.setError("Field Required");
        }else if (dob.getText().toString().trim().equals("")){
            dob.setError("Field Required");
        }else if (currAdd.getText().toString().trim().equals("")){
            currAdd.setError("Field Required");
        }else if (cP1.getText().toString().trim().equals("")){
            cP1.setError("Field Required");
        }else if (!cp1MobNum.matches(mobileNumPattern)){
            cP1Num.setError("Invalid mobile number");
        }else if (cP2.getText().toString().trim().equals("")){
            cP2.setError("Field Required");
        }else if (!cp2MobNum.matches(mobileNumPattern)){
            cP2Num.setError("Invalid mobile number");
        } else if (cP3.getText().toString().trim().equals("")){
            cP3.setError("Field Required");
        }else if (!cp3MobNum.matches(mobileNumPattern)){
            cP3Num.setError("Invalid mobile number");
        }
        else{
            info = (UserInformation) getIntent().getSerializableExtra("info");
            info.setFirstName(fName.getText().toString());
            info.setLastName(lName.getText().toString());
            info.setAge(Integer.parseInt(age.getText().toString()));
            info.setBirthday(dob.getText().toString());
            info.setCurrentAddress(currAdd.getText().toString());
            info.setContactPerson1(cP1.getText().toString());
            info.setContactNumber1(cP1Num.getText().toString());
            info.setContactPerson2(cP2.getText().toString());
            info.setContactNumber2(cP2Num.getText().toString());
            info.setContactPerson3(cP3.getText().toString());
            info.setContactNumber3(cP3Num.getText().toString());


            Intent i = new Intent(getApplicationContext(), RegisterStep4.class);
            i.putExtra("info", info);
            startActivity(i);

        }

    }

    public void btnOnClickLoginPage(View v){

        if (!savedAccount.isEmpty()){
            Intent i = new Intent(RegisterStep3.this, LogIn2.class);
            startActivity(i);
        } else {
            Intent i = new Intent(RegisterStep3.this, Login.class);
            startActivity(i);
        }
    }


}
