package com.example.administrator.benotes;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Raken on 4/5/2018.
 */

public class NotesView extends Fragment{
    @Nullable
    DatabaseHelper myDb;
    SimpleCursorAdapter simpleCursorAdapter;
    ListView listviewnotes;
    View n=null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("View Notes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       n= inflater.inflate(R.layout.notes_view ,container,false);
        myDb = new DatabaseHelper(getActivity());
        listviewnotes=(ListView)n.findViewById(R.id.notes_list);


        displaynotes();
        return n;
    }

    public void displaynotes() {
        // Value lini
        String University=null;
        String Semester=null;
        String Faculty=null;
        String Subject=null;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            University = bundle.getString("University", University);
            Semester = bundle.getString("Semester", Semester);
            Faculty = bundle.getString("Faculty", Faculty);
            Subject= bundle.getString("subject", Subject);

        }

//        Toast.makeText(getActivity(), "Notes view ma xiryo "+University+" " +Semester+ " "+Faculty+" "+Subject, Toast.LENGTH_SHORT).show();


        // yaha samma

//        final String finalUniversity = University;
//        final String finalSemester = Semester;
//        final String finalFaculty = Faculty;
//
        Cursor data =myDb.getRequiredNote(University, Semester, Faculty,Subject);

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
