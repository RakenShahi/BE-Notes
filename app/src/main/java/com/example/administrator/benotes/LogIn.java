package com.example.administrator.benotes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

/**
 * Created by Raken on 4/5/2018.
 */

public class LogIn extends Fragment{
    DatabaseHelper myDb;
    public Button login_in_btn;
    public Button sign_up_btn;
    EditText useremail,userpassword;
    ProgressBar login_pg;


    FirebaseAuth mAuth;
    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Log In");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.login_layout ,container,false);
        login_in_btn=(Button)v.findViewById(R.id.btn_login);
        sign_up_btn=(Button)v.findViewById(R.id.btn_signUp);
        useremail=(EditText)v.findViewById(R.id.login_user_email);
        userpassword=(EditText)v.findViewById(R.id.login_user_password);
        mAuth=FirebaseAuth.getInstance();
        login_pg=(ProgressBar)v.findViewById(R.id.login_pg);
        login_pg.setVisibility(View.GONE);



        //login button work
        login_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=useremail.getText().toString().trim();
                String password=userpassword.getText().toString().trim();

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

                login_pg.setVisibility(View.VISIBLE);


            //Work from here
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Fragment fragment =new SetUpProfileImage();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_main,fragment,"Log");
//                            ft.addToBackStack("Allnotes");
                            ft.addToBackStack("Login");
                            ft.commit();


                        }
                        else
                        {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            login_pg.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        //sign up button work
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment =new SignUp();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


        return v;

    }
}
