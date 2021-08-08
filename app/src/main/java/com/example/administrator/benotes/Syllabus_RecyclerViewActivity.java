package com.example.administrator.benotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by raken on 5/17/2018.
 */

public class Syllabus_RecyclerViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    String university, faculty,semester,subject,key,fileName,url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syllabus_recycler_view);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user =firebaseAuth.getCurrentUser();
//        Toast.makeText(this, "Page open vayo", Toast.LENGTH_SHORT).show();
        if(user!=null) {
            String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserid);
            current_user_db
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshota) {
                            university = String.valueOf(dataSnapshota.child("University").getValue());
                            faculty = String.valueOf(dataSnapshota.child("Faculty").getValue());
                            semester = String.valueOf(dataSnapshota.child("Semester").getValue());
                            if (university.isEmpty() || faculty.isEmpty() || semester.isEmpty()) {
                                Toast.makeText(Syllabus_RecyclerViewActivity.this, "Fields value error", Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(Syllabus_RecyclerViewActivity.this, university + " " + faculty + " " + semester, Toast.LENGTH_SHORT).show();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Syllabus").child(university).child(faculty).child(semester);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshotb, String s) {
                    //actually called for individual items at the database reference
                     key = String.valueOf(dataSnapshotb.getKey()); // returns the filename

                    DatabaseReference bhitra=FirebaseDatabase.getInstance().getReference().child("Syllabus").child(university).child(faculty).child(semester).child(key);
                        bhitra.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                              fileName=String.valueOf(dataSnapshot.child("syllabus_name").getValue());
                              url = String.valueOf(dataSnapshot.child("Url").getValue());
                                ((SyllabusAdapter) recyclerView.getAdapter()).update(fileName, url);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


//                String url=dataSnapshot.`getValue(String.class); //return url for filename
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }
                        }//else close


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
        recyclerView=findViewById(R.id.recyclerView);
        //custom adapters always
        //they are used to populate the recycler view with items..

        recyclerView.setLayoutManager(new LinearLayoutManager(Syllabus_RecyclerViewActivity.this));
        SyllabusAdapter myAdapter=new SyllabusAdapter(recyclerView,Syllabus_RecyclerViewActivity.this,new ArrayList<String>(), new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        Intent intent= new Intent(Syllabus_RecyclerViewActivity.this,FirebaseLogin.class);
        startActivity(intent);
    }
}
