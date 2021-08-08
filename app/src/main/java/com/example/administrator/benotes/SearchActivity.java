package com.example.administrator.benotes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Raken.
 */

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchField;
    private Button mSearchBtn;
    private FirebaseAuth firebaseAuth;
    String university,faculty,semester,currentuserid,subject;
    public String urls;
    public RecyclerView mResultList;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mUserDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user =firebaseAuth.getCurrentUser();




        if(user!=null) {
            currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserid);
            current_user_db
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshota) {

                            university = String.valueOf(dataSnapshota.child("University").getValue());
                            faculty = String.valueOf(dataSnapshota.child("Faculty").getValue());
                            semester = String.valueOf(dataSnapshota.child("Semester").getValue());


                            if (university.isEmpty() || faculty.isEmpty() || semester.isEmpty()) {

                                mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(faculty).child(semester);

                            } else {
          mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(faculty).child(semester);


                            }
                        }//else close


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        } //main if user close


        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String searchText = mSearchField.getText().toString();
//                Toast.makeText(SearchActivity.this, searchText, Toast.LENGTH_SHORT).show();
                firebaseUserSearch(searchText);

                //to store the keyword in firebase
//                String timevalue = System.currentTimeMillis() + "";
//                currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                Search_data search_data=new Search_data(currentuserid,searchText,timevalue);
//                DatabaseReference searchref=FirebaseDatabase.getInstance().getReference(); //gives root node
//                searchref.child("For_recommedation").child(currentuserid).child(timevalue).setValue(search_data);


            }
        });

    }

    private void firebaseUserSearch(String searchText) {


        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("subject").startAt(searchText).endAt(searchText + "\uf8ff");
//        Toast.makeText(this, firebaseSearchQuery.toString(), Toast.LENGTH_SHORT).show();

        FirebaseRecyclerAdapter<for_note, NotesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<for_note, NotesViewHolder>(

                for_note.class,
                R.layout.list_layout,
                NotesViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            public void populateViewHolder(NotesViewHolder viewHolder, for_note model, int position) {

                viewHolder.setDetails(model.getNote_name(),model.getUrl());
                final String u=model.getUrl();
                subject=model.getSubject();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(SearchActivity.this, "Cliked" + u, Toast.LENGTH_SHORT).show();


                        int position=mResultList.getChildLayoutPosition(view);
                        // database ma store garna lai (subject of that searched note)

                String timevalue = System.currentTimeMillis() + "";
                currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Search_data search_data=new Search_data(currentuserid,subject,timevalue);
                DatabaseReference searchref=FirebaseDatabase.getInstance().getReference(); //gives root node
                searchref.child("For_recommedation").child(currentuserid).child(timevalue).setValue(search_data);

                        //browser ko work eta

                        Intent intent=new Intent();
                        intent.setType(Intent.ACTION_VIEW); // denotes that we are going to view something..
                        intent.setData(Uri.parse(u));
                        startActivity(intent);

                    }
                });
            }


        };


        mResultList.setAdapter(firebaseRecyclerAdapter);

//        Toast.makeText(this, "After m", Toast.LENGTH_SHORT).show();



    }


    // View Holder Class

    public static class NotesViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public NotesViewHolder(View itemView) {
            super(itemView);

            mView = itemView;



        }


        public void setDetails( String notename,String url){

            TextView note_name = (TextView) mView.findViewById(R.id.note_name_text);
//            TextView url_text = (TextView) mView.findViewById(R.id.url_text);


           note_name.setText(notename);





        }




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
        Intent intent= new Intent(SearchActivity.this,FirebaseLogin.class);
        startActivity(intent);
    }

}
