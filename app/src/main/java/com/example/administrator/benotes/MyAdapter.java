package com.example.administrator.benotes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Raken on 5/17/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items=new ArrayList<>();
    ArrayList<String> urls=new ArrayList<>();
    public String university,faculty,semester,subject,timevalue,currentuserid,filename,key;
    private DatabaseReference mUserDatabase;


    public void update(String name, String url){

        items.add(name);
        urls.add(url);
        notifyDataSetChanged(); //refreshed the recycler view automatically..
    }

    public MyAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls=urls;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//to create views for recycler view items
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //initialize the elements of individual items..
        holder.nameofFile.setText(items.get(position));

    }

    @Override
    public int getItemCount() { //return the number of items
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameofFile;
        public ViewHolder(View itemView){ //represents individual list items
            super(itemView);
            nameofFile=itemView.findViewById(R.id.nameofFile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // to store clicked subject in database
                     timevalue = System.currentTimeMillis() + "";
                     currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                     filename=nameofFile.getText().toString();

                    mUserDatabase = FirebaseDatabase.getInstance().getReference();



                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserid);
                    current_user_db
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshota) {


                                    university = String.valueOf(dataSnapshota.child("University").getValue());
                                    faculty = String.valueOf(dataSnapshota.child("Faculty").getValue());
                                    semester = String.valueOf(dataSnapshota.child("Semester").getValue());




                                    mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(faculty).child(semester);

                                     mUserDatabase.orderByChild("note_name").equalTo(filename).addChildEventListener(new ChildEventListener() {
                                         @Override
                                         public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                             key=String.valueOf(dataSnapshot.getKey());
                                             mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Notes").child(university).child(faculty).child(semester).child(key);
                                             mUserDatabase.addValueEventListener(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                                     subject=String.valueOf(dataSnapshot.child("subject").getValue());
                                                     DatabaseReference searchref= FirebaseDatabase.getInstance().getReference(); //gives root node
                                                     Search_data search_data=new Search_data(currentuserid,subject,timevalue);
                                                     searchref.child("For_recommedation").child(currentuserid).child(timevalue).setValue(search_data);
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












                                }

                                @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

//




                    //upto here
                    int position=recyclerView.getChildLayoutPosition(view);

                    //browser ko work eta

                    Intent intent=new Intent();
                    intent.setType(Intent.ACTION_VIEW); // denotes that we are going to view something..
                    intent.setData(Uri.parse(urls.get(position)));
                    context.startActivity(intent);
                }
            });
        }
    }
}
