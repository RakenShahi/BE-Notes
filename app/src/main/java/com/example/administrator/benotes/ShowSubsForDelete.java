package com.example.administrator.benotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Raken on 4/5/2018.
 */

public class ShowSubsForDelete extends Fragment{
    @Nullable
    DatabaseHelper myDb;
    SimpleCursorAdapter simpleCursorAdapter;
    ListView lvSubject;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Choose Subject");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.show_subs_for_delete_notes ,container,false);
        myDb = new DatabaseHelper(getActivity());
        lvSubject=(ListView)v.findViewById(R.id.sub_list_for_delete);
        displaysubjectlist();

        return v;

    }
    public void displaysubjectlist() {
        String University=null;
        String Semester=null;
        String Faculty=null;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            University = bundle.getString("University", University);
            Semester = bundle.getString("Semester", Semester);
            Faculty = bundle.getString("Faculty", Faculty);
//            Toast.makeText(getActivity(), University + " "+ Semester + " "+Faculty, Toast.LENGTH_SHORT).show();
        }

        Cursor data = myDb.getAllSubjects();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the arraylist
            listData.add(data.getString(5));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listData);
        lvSubject.setAdapter(adapter);
        //setting item click listener in listview subjects
        final String finalUniversity = University;
        final String finalSemester = Semester;
        final String finalFaculty = Faculty;
        lvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String subject = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You clicked on " + subject);
                Toast.makeText(getActivity(), "Clicked " + subject, Toast.LENGTH_SHORT).show();


                //Alert box
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to delete the whole Subject or its notes?");
                builder.setCancelable(true);
                builder.setNegativeButton("Delete Whole subject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDb.deleteselectednote(finalUniversity, finalSemester, finalFaculty,subject);

                            Toast.makeText(getActivity(), subject+" Subject Delted", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setPositiveButton("View its notes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDb.getRequiredNote(finalUniversity, finalSemester, finalFaculty,subject);
                        Toast.makeText(getActivity(), "Action note", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();



            // Yaha samma alert box



//                Cursor d = myDb.getNoteId(subject); //get the id associated with that subject
//                int noteID = -1;
//                while (d.moveToNext()) {
//                    noteID = d.getInt(0);
//                }
//
//                if (noteID > -1) {
//                    Log.d(TAG, "onItemClick: The ID is" + noteID);
//                    Toast.makeText(getActivity(), noteID + " " + subject, Toast.LENGTH_SHORT).show();
//                    Fragment fragment = new NotesView();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Subject", subject);
//                    fragment.setArguments(bundle);
//                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_main, fragment);
//                    ft.addToBackStack(null);
//                    ft.commit();
//
//
//                } else {
//                    Toast.makeText(getActivity(), "No associated with this subject", Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }
}
