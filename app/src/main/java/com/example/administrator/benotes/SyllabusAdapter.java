package com.example.administrator.benotes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Raken on 5/17/2018.
 */

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.ViewHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items=new ArrayList<>();
    ArrayList<String> urls=new ArrayList<>();
    public String university,faculty,semester,subject;
    private DatabaseReference mUserDatabase;


    public void update(String name, String url){

        items.add(name);
        urls.add(url);
        notifyDataSetChanged(); //refreshed the recycler view automatically..
    }

    public SyllabusAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls=urls;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//to create views for recycler view items
        View view= LayoutInflater.from(context).inflate(R.layout.syllabus_item,parent,false);

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
