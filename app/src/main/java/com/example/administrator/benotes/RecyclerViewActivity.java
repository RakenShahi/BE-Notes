package com.example.administrator.benotes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 5/17/2018.
 */

public class RecyclerViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    String university, faculty,semester,subject,key,fileName,url;
    Button search_bt;

    //uta ko variblees
    String currentuserid;
    public String urls;
    public RecyclerView mResultList;
    private DatabaseReference mUserDatabase;
    public String[] subjects = new String[100];
    //    public String[] datas=new String[100];
    public String[] value = new String[100];

    String count;
    int unique_value[] = new int[100];


    public int i = 0, c = 0;
    int loop = 1;
     //up to here
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user =firebaseAuth.getCurrentUser();

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
                                Toast.makeText(RecyclerViewActivity.this, "Fields value error", Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(RecyclerViewActivity.this, university + " " + faculty + " " + semester, Toast.LENGTH_SHORT).show();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(faculty).child(semester);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshotb, String s) {
                    //actually called for individual items at the database reference
                     key = String.valueOf(dataSnapshotb.getKey()); // returns the filename

                    DatabaseReference bhitra=FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(faculty).child(semester).child(key);
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
                        }//else close


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
        recyclerView=findViewById(R.id.recyclerView);
        //custom adapters always
        //they are used to populate the recycler view with items..

        recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerViewActivity.this));

        MyAdapter myAdapter=new MyAdapter(recyclerView,RecyclerViewActivity.this,new ArrayList<String>(), new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);

        search_bt=findViewById(R.id.search_btn);
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecyclerViewActivity.this,SearchActivity.class));
            }
        });


        //recommendation ko lagi yaha dekhi
        if (user != null) {

            currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference Recomendation_sub = FirebaseDatabase.getInstance().getReference().child("For_recommedation").child(currentuserid);


            Recomendation_sub.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String key = String.valueOf(dataSnapshot.getKey());

//                        Toast.makeText(Recommendation.this, key, Toast.LENGTH_SHORT).show();
                    DatabaseReference bhitra = FirebaseDatabase.getInstance().getReference().child("For_recommedation").child(currentuserid).child(key);
                    DatabaseReference countkolagi = FirebaseDatabase.getInstance().getReference().child("For_recommedation").child(currentuserid);

                    countkolagi.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count = String.valueOf(dataSnapshot.getChildrenCount());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    bhitra.addValueEventListener(new ValueEventListener() {

                        @Override

                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                Toast.makeText(Recommendation.this, count, Toast.LENGTH_SHORT).show();


//                                String sub=String.valueOf(dataSnapshot.child("search_subject").getValue());
                            subjects[i] = String.valueOf(dataSnapshot.child("search_subject").getValue());
                            setvalue(subjects[i], count);
                            i = i + 1;

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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


        } //main if user close


        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(RecyclerViewActivity.this));



        //yaha samma

    }
//recommendation ko classes
public void setvalue(String d, String cvalue) {
    String uniqueid[] = new String[100];
    this.value[c] = d;
    c = c + 1;

    int counter = Integer.valueOf(cvalue);
    loop = loop + 1;
    Arrays.fill(unique_value, 0);
    if (loop == counter) {

        //Hashset values
        List<String> list = Arrays.asList(value);
        Set<String> set = new HashSet<String>(list);
        String datas[] = set.toArray(new String[set.size()]);
        int datas_length = 0;

        for (int k = 0; k < datas.length; k++) {
            if (datas[k] != null) {
                datas_length++;
            }

        }
//            Toast.makeText(this, "Total unique datas = "+ datas_length, Toast.LENGTH_SHORT).show();
        for (int k = 1; k < datas_length; k++) {
//                Toast.makeText(this, "data d["+k+ "] = " + datas[k], Toast.LENGTH_SHORT).show();
//                unique_value[k]=k;
//                uniqueid[k]=String.valueOf(unique_value[k]);
//                Toast.makeText(this, datas[k] + "is assigned a value = " +uniqueid, Toast.LENGTH_SHORT).show();

        }
//            Toast.makeText(this, value[1]+" why no equal"+datas[2], Toast.LENGTH_SHORT).show();
        for (int j = 1; j < (counter - 1); j++) {
//                Toast.makeText(this, "Value v["+j+"] = " + value[j] , Toast.LENGTH_SHORT).show();
            for (int l = 1; l < (datas_length); l++) {
                if (value[j].equals(datas[l])) {
//                            Toast.makeText(this, "Value v["+j+"] = " + value[j] + " & data d["+l+ "] = "+ datas[l] + " is equal", Toast.LENGTH_SHORT).show();
                    unique_value[l] = unique_value[l] + 1;

//                            for(int m=1;m<(datas_length);m++)
//                                                {
//                                                        if (datas[m].equals(value[j])) {
////                                                                unique_value[m]=increase[m];
//                                                                unique_value[m]=unique_value[m]+1;
//                                                        }
//                                                }
                } else {
//                            Toast.makeText(this, "Value v["+j+"] = " + value[j] + " & data d["+l+ "] = "+ datas[l] + " is not equal", Toast.LENGTH_SHORT).show();
                }


            }
        }

        for (int l = 1; l < (datas_length); l++) {
//                Toast.makeText(this, datas[l] + " repeated " + unique_value[l] + " times. ", Toast.LENGTH_SHORT).show();
        }
        //Maximum kun  ko cha find gareko
        int maxindex = 0;
        for (int s = 0; s < unique_value.length; s++) {
            if (unique_value[s] > unique_value[maxindex]) {
                maxindex = s;
            }
        }
//        Toast.makeText(this, "Max viewed is of subject = " + datas[maxindex], Toast.LENGTH_SHORT).show();

        //To print the data on recycler view
        Recommend(datas[maxindex]);

    } //if close


}

    private void Recommend(final String searchText) {


//        Toast.makeText(RecyclerViewActivity.this, "Started recommendation of " + searchText, Toast.LENGTH_LONG).show();

        // for path reference mUserDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        if (user != null) {
            currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // To Delete old datas older than 30days
            DatabaseReference Delete = FirebaseDatabase.getInstance().getReference().child("For_recommedation").child(currentuserid);
            long cutoff = new java.util.Date().getTime() - TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
            Query oldItems = Delete.orderByChild("timestamp").endAt(cutoff);
            oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                        itemSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
            //Data deletion upto here


            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserid);
            current_user_db
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshota) {

                            university = String.valueOf(dataSnapshota.child("University").getValue());
                            faculty = String.valueOf(dataSnapshota.child("Faculty").getValue());
                            semester = String.valueOf(dataSnapshota.child("Semester").getValue());


                            if (university.isEmpty() || faculty.isEmpty() || semester.isEmpty()) {
                                Toast.makeText(RecyclerViewActivity.this, "Fields value error", Toast.LENGTH_SHORT).show();
                                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(faculty).child(semester);

                            } else {
//                                Toast.makeText(Recommendation.this, university + " " + faculty + " " + semester, Toast.LENGTH_SHORT).show();

                                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(faculty).child(semester);

                                Query firebaseSearchQuery = mUserDatabase.orderByChild("subject").equalTo(searchText).limitToFirst(10);
                                FirebaseRecyclerAdapter<for_note, Recommendation.NotesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<for_note, Recommendation.NotesViewHolder>(

                                        for_note.class,
                                        R.layout.list_layout,
                                        Recommendation.NotesViewHolder.class,
                                        firebaseSearchQuery

                                ) {
                                    @Override
                                    public void populateViewHolder(Recommendation.NotesViewHolder viewHolder, for_note model, int position) {

                                        viewHolder.setDetails(model.getNote_name(), model.getUrl());
                                        final String u = model.getUrl();
                                        subject = model.getSubject();

                                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//                        Toast.makeText(SearchActivity.this, "Cliked" + u, Toast.LENGTH_SHORT).show();


                                                int position = mResultList.getChildLayoutPosition(view);
                                                // database ma store garna lai (subject of that searched note)

                                                String timevalue = System.currentTimeMillis() + "";
                                                currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                Search_data search_data = new Search_data(currentuserid, subject, timevalue);
                                                DatabaseReference searchref = FirebaseDatabase.getInstance().getReference(); //gives root node
                                                searchref.child("For_recommedation").child(currentuserid).child(timevalue).setValue(search_data);

                                                //browser ko work eta

                                                Intent intent = new Intent();
                                                intent.setType(Intent.ACTION_VIEW); // denotes that we are going to view something..
                                                intent.setData(Uri.parse(u));
                                                startActivity(intent);

                                            }
                                        });
                                    }


                                };// yeta samma adapter ko lagi
                                mResultList.setAdapter(firebaseRecyclerAdapter); // last line for adapter
                            }
                        }//else close


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            // yeta samma



//        Toast.makeText(this, firebaseSearchQuery.toString(), Toast.LENGTH_SHORT).show();






        }
    }


    // View Holder Class

    public static class NotesViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public NotesViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }


        public void setDetails(String notename, String url) {

            TextView note_name = (TextView) mView.findViewById(R.id.note_name_text);
//            TextView url_text = (TextView) mView.findViewById(R.id.url_text);
//            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);

            note_name.setText(notename);
//           url_text.setText(url);


//            url_text.setText(userImage);

//            Glide.with(ctx).load(userImage).into(user_image);


        }

    }



// yaha samma recomendaiton ko classes

    private void recommendation() {
        Recommendation recc=new Recommendation();

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
        Intent intent= new Intent(RecyclerViewActivity.this,FirebaseLogin.class);
        startActivity(intent);
    }
}
