package com.example.administrator.benotes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Raken on 4/5/2018.
 */

public class AllNotes extends Fragment{
    @Nullable
    DatabaseHelper myDb;
    public Button all_sub,more_btn;
    private FirebaseAuth firebaseAuth;
    SimpleCursorAdapter simpleCursorAdapter;
    ListView listviewnotes,recommendednotes;
    View n=null;
    String University=null;
    String Semester=null;
    String Faculty=null;
    String Subject=null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("View Notes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       n= inflater.inflate(R.layout.all_notes_view ,container,false);
        myDb = new DatabaseHelper(getActivity());
        //value leko

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            University = bundle.getString("University", University);
            Semester = bundle.getString("Semester", Semester);
            Faculty = bundle.getString("Faculty", Faculty);
            Subject= bundle.getString("subject", Subject);

        }

        listviewnotes=(ListView)n.findViewById(R.id.notes_list);
        recommendednotes=(ListView)n.findViewById(R.id.recommended_list);
        all_sub=(Button)n.findViewById(R.id.sub);
        more_btn=(Button)n.findViewById(R.id.more_btn);

//        final String finalFaculty = Faculty;
//        final String finalUniversity = University;
//        final String finalSemester = Semester;
        all_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString ("University", University);
                bundle.putString ("Semester", Semester);
                bundle.putString ("Faculty", Faculty);

                Fragment fragment =new Notes();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();

                }



        });


        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment fragment =new LogIn();
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_main,fragment);
//                ft.addToBackStack("Allnotes");
//                ft.commit();
                firebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser user =firebaseAuth.getCurrentUser();
                if(user==null) {
                Intent intent = new Intent(getActivity(), FirebaseLogin.class);
                startActivity(intent);}
                else{
                    startActivity(new Intent(getActivity(),RecyclerViewActivity.class));
                }

            }
        });

        displaynotes();
        return n;
    }

    public void displaynotes() {
        // Value lini


//
        Cursor data =myDb.viewRelatedNotes(University, Semester, Faculty);

        ArrayList<String> listData = new ArrayList<>();
            while (data.moveToNext()) {
////            //get the value from the database in column 1
////            //then add it to the arraylist
                listData.add(data.getString(0));
            }



//
////        //create the list adapter and set the adapter
            ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listData);
            listviewnotes.setAdapter(adapter);

        ArrayList<String> recommendData = new ArrayList<>();
        recommendData.add(new String(" "));

        recommendData.add(new String("Recommended Notes"));
        ListAdapter recommend = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, recommendData);
        recommendednotes.setAdapter(recommend);


        //setting item click listener in listview subjects
        listviewnotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), "Clicked ", Toast.LENGTH_SHORT).show();
                String notename=adapterView.getItemAtPosition(i).toString();
                Log.d(TAG,"onItemClick: You clicked on " + notename);
                    Fragment fragment = new pdf();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                Bundle bundle = new Bundle();
                bundle.putString ("Notename",   notename);
                fragment.setArguments(bundle);

            }
        });
        }




}
