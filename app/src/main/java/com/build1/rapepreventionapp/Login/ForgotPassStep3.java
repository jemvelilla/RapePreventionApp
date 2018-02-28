package com.build1.rapepreventionapp.Login;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassStep3 extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private ImageView loading;
    AnimationDrawable animation;

    FirebaseUser currentUser;
    EditText newPassword, confirmPassword;

    Button btnSubmit;

    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_step3);
        TextView user = (TextView) findViewById(R.id.tvName);

        mAuth = FirebaseAuth.getInstance();

        //
//        user.setText(currentUser.getEmail());

        loading = (ImageView) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        loading.bringToFront();
        animation = (AnimationDrawable) loading.getDrawable();

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        newPassword = (EditText) findViewById(R.id.editTextNewPassword);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);


        //for link clicked
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            String segments = intent.getData().toString();
            String subUrl = segments.replace(segments.substring(segments.lastIndexOf("&")), "");
            code = subUrl.substring(subUrl.lastIndexOf("=")+1);
        }
    }


    public void btnOnClickUpdatePassword (View view){

        loading.setVisibility(View.VISIBLE);
        animation.start();
        btnSubmit.setVisibility(View.INVISIBLE);

        if(newPassword.getText().toString().equals(confirmPassword.getText().toString())){
            mAuth.confirmPasswordReset(code, newPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Intent i = new Intent(ForgotPassStep3.this, PasswordUpdated.class);
                                startActivity(i);
                            } else {
                                btnSubmit.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.INVISIBLE);
                                animation.stop();
                                Toast.makeText(ForgotPassStep3.this, "Failed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {
            btnSubmit.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
            animation.stop();
            Toast.makeText(ForgotPassStep3.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
        }

    }


}
