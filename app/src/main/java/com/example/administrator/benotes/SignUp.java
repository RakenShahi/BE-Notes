package com.example.administrator.benotes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

/**
 * Created by Raken on 4/5/2018.
 */

public class SignUp extends Fragment{
    public Button sign_up_btn;
    public Button  log_in_btn;
    EditText useremail,userpassword,usermobilenum;
    private FirebaseAuth mAuth;
    ProgressBar mypg;
    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Sign Up");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.signup_layout,container,false);
        useremail=(EditText)v.findViewById(R.id.emailtxt);
        userpassword=(EditText)v.findViewById(R.id.passtxt);
        sign_up_btn=(Button)v.findViewById(R.id.btn_signup_sign);
        log_in_btn=(Button)v.findViewById(R.id.btn_signin);

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment =new LogIn();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

//        usermobilenum=(EditText)v.findViewById(R.id.mob_sign);

        mypg=(ProgressBar)v.findViewById(R.id.myprogressbar);
        mAuth = FirebaseAuth.getInstance();

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mypg.setVisibility(View.VISIBLE);
        String email=useremail.getText().toString().trim();
        String password=userpassword.getText().toString().trim();
//        String mobile=usermobilenum.getText().toString().trim();

        if(email.isEmpty()) {
            useremail.setError("Email is required");
            useremail.requestFocus();
//            return null;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            useremail.setError("Please enter a valid email.");
            useremail.requestFocus();
//            return null;

        }

        if(password.isEmpty()){
            userpassword.setError("Password is required");
            userpassword.requestFocus();
//            return null;

        }
        if(password.length()<6){
            userpassword.setError("Minimum length of password should be 6");
            userpassword.requestFocus();
//            return null;

        }



        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mypg.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "User Registerd Successful", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), "Some Problem occured! Please try again !!!", Toast.LENGTH_SHORT).show();
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getActivity(), "You are already Registered", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }
            }
            });

            }
        });
        return v;

    }
}
