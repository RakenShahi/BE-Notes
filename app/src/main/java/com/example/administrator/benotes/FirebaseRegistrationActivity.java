package com.example.administrator.benotes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRegistrationActivity extends AppCompatActivity {

    Spinner faculty,semester;
    ArrayAdapter<CharSequence> adapter;
    String facultyvalue,semestervalue;
    RadioGroup uni;
    RadioButton univalue;
    EditText subject;
    EditText author;

    private EditText userName,userPassword,userEmail,userAge;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    String email,name,age,password,university;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_registration);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        /*Spinner ko work*/
        semester = (Spinner)findViewById(R.id.spinnersemester);
        adapter = ArrayAdapter.createFromResource(this, R.array.select_semester, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter);

        faculty = (Spinner)findViewById(R.id.spinnerfaculty);
        adapter = ArrayAdapter.createFromResource(this, R.array.select_faculty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty.setAdapter(adapter);
        //spinner ko eta samma




        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){

                    //upload data to the database
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

//                    Toast.makeText(FirebaseRegistrationActivity.this, user_email+ " " + user_password, Toast.LENGTH_SHORT).show();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
//                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                          Intent intent =new Intent(RegistrationActivity.this,MainActivity.class);
//                                startActivity(intent);

                                sendEmailVerification();
//
                                sendUserData();
                                Toast.makeText(FirebaseRegistrationActivity.this,"Sucessfully Registered,Please verify your email to continue",Toast.LENGTH_SHORT).show();
                                finish();

//                                startActivity(new Intent(FirebaseRegistrationActivity.this,RecyclerViewActivity.class));

                            }
                            else{
                                Toast.makeText(FirebaseRegistrationActivity.this, "Registration Failed / Email is already used", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirebaseRegistrationActivity.this,MainActivity.class));
            }
        });
    }

    private void setupUIViews(){

        userName=(EditText) findViewById(R.id.etUserName);
        userPassword=(EditText) findViewById(R.id.etUserPassword);
        userEmail=(EditText) findViewById(R.id.etUserEmail);
        regButton=(Button) findViewById(R.id.btnRegister);
        userLogin=(TextView) findViewById(R.id.tvUserLogin);
        userAge=(EditText) findViewById(R.id.etAge);


    }

    private Boolean validate(){
        Boolean result=false;

        //Submit Button ko lagi work
        uni=(RadioGroup)findViewById(R.id.University);
        int uniid=uni.getCheckedRadioButtonId(); //radio ko id leko
        univalue=(RadioButton)findViewById(uniid); // radio ko value leko
        university=univalue.getText().toString();
        name=userName.getText().toString();
        password=userPassword.getText().toString();
        email=userEmail.getText().toString();
        age=userAge.getText().toString();
        semestervalue = semester.getSelectedItem().toString();
        facultyvalue=faculty.getSelectedItem().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty() || semester.getSelectedItem() == null ||faculty.getSelectedItem()==null || university.isEmpty() ){
            Toast.makeText(this,"please Enter all the details",Toast.LENGTH_SHORT).show();

        }else{
            result=true;

        }
        return result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(FirebaseRegistrationActivity.this,"Sucessfully Registered,Verification mail sent",Toast.LENGTH_SHORT).show();;
//                        firebaseAuth.signOut();
//                        finish();
                        startActivity(new Intent(FirebaseRegistrationActivity.this,RecyclerViewActivity.class));
                    }else{
                        Toast.makeText(FirebaseRegistrationActivity.this,"Verification mail hasn't been sent!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserData(){
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference();
        String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserProfile userProfile = new UserProfile(age,email,name,currentuserid,university,semestervalue,facultyvalue);
        myRef.child("Users").child(currentuserid).setValue(userProfile);
    }
}
