package com.devdroid.edunomics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin,btnOTP;
    EditText edtEmail,edtPassword;
    TextView txtSignup,fgtPassword;
    ImageView img1,img2;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;

    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(LoginActivity.this,AfterLogin.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnOTP = findViewById(R.id.btnOTP);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        txtSignup = findViewById(R.id.txtSignup);
        fgtPassword = findViewById(R.id.fgtPassword);

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Coming Soon :)", Toast.LENGTH_SHORT).show();
            }
        });

        edtEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtEmail.setTextColor(getResources().getColor(R.color.colorPrimary));
                edtEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                edtPassword.setTextColor(getResources().getColor(R.color.Black));
                edtPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Black),PorterDuff.Mode.SRC_ATOP);
                img1.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.Black), PorterDuff.Mode.SRC_IN);
                return false;
            }
        });


        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtPassword.setTextColor(getResources().getColor(R.color.colorPrimary));
                edtPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                edtEmail.setTextColor(getResources().getColor(R.color.Black));
                edtEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Black),PorterDuff.Mode.SRC_ATOP);
                img2.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                img1.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.Black), PorterDuff.Mode.SRC_IN);
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                startSignin(edtEmail.getText().toString(),edtPassword.getText().toString());
            }
        });


    }

    public void startSignin(String email, String password) {


        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Logging in");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this,AfterLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{

                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid Credentials or Server Problem", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
