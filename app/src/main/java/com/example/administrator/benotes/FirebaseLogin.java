package com.example.administrator.benotes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Raken on 4/5/2018.
 */

public class FirebaseLogin extends AppCompatActivity {
    private EditText useremail;
    private EditText userpassword;
    private TextView Info;
    private Button Login;
    private int counter = 10;
    private TextView userRegistration;
    public FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword,verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);

        useremail= (EditText) findViewById(R.id.user_email);
        userpassword=(EditText) findViewById(R.id.etPassword);
        Info=(TextView) findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.btnLogin);
        userRegistration= (TextView) findViewById(R.id.tvRegister);
        forgotPassword =(TextView) findViewById(R.id.tvForgotPassword);
        verify =(TextView) findViewById(R.id.verificationemail);
        Info.setText("NO of attempts Remaining: 10");
        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user=firebaseAuth.getCurrentUser();

    //***feri login garna naparne process**
        if(user!=null){
            finish();
            startActivity(new Intent(FirebaseLogin.this,RecyclerViewActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(useremail.getText().toString().trim(), userpassword.getText().toString().trim());

            }
        });


        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( FirebaseLogin.this,FirebaseRegistrationActivity.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirebaseLogin.this,PasswordActivity.class);
                startActivity(intent);
            }
        });



    }

    private void validate(final String email, final String password){
        progressDialog.setMessage("Processing please wait");
//        Toast.makeText(FirebaseLogin.this, useremail.getText().toString()+ " "+ userpassword.getText().toString(), Toast.LENGTH_SHORT).show();

        progressDialog.show();
        if(email.isEmpty()) {
            useremail.setError("Email is required");
            useremail.requestFocus();
            progressDialog.dismiss();

//            return null;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            useremail.setError("Please enter a valid email.");
            useremail.requestFocus();
            progressDialog.dismiss();

//            return null;

        }

        if(password.isEmpty()){
            userpassword.setError("Password is required");
            userpassword.requestFocus();
            progressDialog.dismiss();

//            return null;

        }
        if(password.length()<6){
            userpassword.setError("Minimum length of password should be 6");
            userpassword.requestFocus();
            progressDialog.dismiss();

//            return null;

        }

//        if(userName.equals("")|| userPassword.equals("")){
//            Toast.makeText(FirebaseLogin.this,"Login credentials are not enough",Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
//
//            return;
//        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                 @Override
                                                                                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                     if(task.isSuccessful()){
                                                                                                         progressDialog.dismiss();

//                                                                                                         Toast.makeText(FirebaseLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                                                                                                         checkEmailVerification();
//                                                                                                         startActivity(new Intent(FirebaseLogin.this,RecyclerViewActivity.class));

                                                                                                     }
                                                                                                     else{
                                                                                                         Toast.makeText(FirebaseLogin.this,"Username or password mismatch",Toast.LENGTH_SHORT).show();
                                                                                                         counter--;
                                                                                                         Info.setText("No. of attempts remaining:" + counter);
                                                                                                         progressDialog.dismiss();
                                                                                                         if(counter==0){
                                                                                                             Login.setEnabled(false);
                                                                                                         }
                                                                                                     }
                                                                                                 }
                                                                                             }
        );



//        if((userName.equals("Admin"))&&(userPassword.equals("1234"))) {
//            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//            startActivity(intent);
//        }else{
//
//            counter--;
//            Info.setText("No of attempts remaining:" + String.valueOf(counter));
//
//            if(counter == 0){
//                Login.setEnabled(false);
//            }
//        }
    }



    private void checkEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();
        if(emailflag){
            finish();
            Intent intent=new Intent(FirebaseLogin.this,MainActivity.class);
            startActivity(intent);
        }else{
            firebaseAuth.signOut();
            Toast.makeText(this,"Please verify your email to continue",Toast.LENGTH_SHORT).show();

        }
    }


}

