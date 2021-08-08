package com.example.administrator.benotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Field_on_net extends AppCompatActivity {
    Spinner faculty,semester;
    ArrayAdapter<CharSequence> adapter;
    private static Button button_sbm;
    String university,facultyvalue,semestervalue,key,fileName,url;
    RadioGroup uni;
    RadioButton univalue;
    RecyclerView recyclerView;
    LinearLayout lini;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_on_net);



        /*Spinner ko work*/
        semester = (Spinner) findViewById(R.id.spinnersemester);
        adapter = ArrayAdapter.createFromResource(Field_on_net.this, R.array.select_semester, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter);

        faculty = (Spinner) findViewById(R.id.spinnerfaculty);
        adapter = ArrayAdapter.createFromResource(Field_on_net.this, R.array.select_faculty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty.setAdapter(adapter);
        //spinner ko eta samma

        //Submit Button ko lagi work
        uni=(RadioGroup)findViewById(R.id.University);
        int uniid=uni.getCheckedRadioButtonId(); //radio ko id leko
        univalue=(RadioButton)findViewById(uniid); // radio ko value leko

        button_sbm=(Button)findViewById(R.id.buttonsub);
        lini=(LinearLayout)findViewById(R.id.linearid);

        button_sbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        lini.setVisibility(View.INVISIBLE);
                semestervalue = semester.getSelectedItem().toString();
                facultyvalue=faculty.getSelectedItem().toString();
                university=univalue.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(facultyvalue).child(semestervalue);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshotb, String s) {
                //actually called for individual items at the database reference
                key = String.valueOf(dataSnapshotb.getKey()); // returns the filename


                DatabaseReference bhitra=FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(facultyvalue).child(semestervalue).child(key);
                bhitra.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fileName=String.valueOf(dataSnapshot.child("note_name").getValue());
                        url = String.valueOf(dataSnapshot.child("Url").getValue());
                        ((MyAdapter) recyclerView.getAdapter()).update(fileName, url);

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
        });

        recyclerView=findViewById(R.id.recyclerView);
        //custom adapters always
        //they are used to populate the recycler view with items..

        recyclerView.setLayoutManager(new LinearLayoutManager(Field_on_net.this));

        MyAdapter myAdapter=new MyAdapter(recyclerView,Field_on_net.this,new ArrayList<String>(), new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);


    }
}
