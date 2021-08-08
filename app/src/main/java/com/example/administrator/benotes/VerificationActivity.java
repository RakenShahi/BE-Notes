package com.example.administrator.benotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Raken.
 */
public class VerificationActivity extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        passwordEmail=(EditText) findViewById(R.id.etPasswordEmail);
        resetPassword=(Button) findViewById(R.id.btnPasswordReset);
        firebaseAuth=FirebaseAuth.getInstance();
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail=passwordEmail.getText().toString().trim();

                if(useremail.equals("")){
                    Toast.makeText(VerificationActivity.this,"please Enter your registered email id",Toast.LENGTH_SHORT).show();

                }else{
//                    firebaseAuth.s (useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
                                Toast.makeText(VerificationActivity.this,"Internet problem occured. Please try again!",Toast.LENGTH_SHORT).show();
//                                finish();
//                                startActivity(new Intent(VerificationActivity.this,FirebaseLogin.class));
//                            } else{
//                                Toast.makeText(VerificationActivity.this,"Error in sending password reset",Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    });
                }
            }
        });
    }
}

