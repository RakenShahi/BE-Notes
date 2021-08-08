package com.example.administrator.benotes;

import android.database.Cursor;
import android.nfc.Tag;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Raken on 4/5/2018.
 */

public class Notes extends Fragment{

    DatabaseHelper myDb;
    SimpleCursorAdapter simpleCursorAdapter;
    ListView lvSubject;


    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Subject");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View n= inflater.inflate(R.layout.notes_layout ,container,false);
        myDb = new DatabaseHelper(getActivity());
        lvSubject=(ListView)n.findViewById(R.id.subjectlistview);
        displaysubjectlist();

    return n;
    }


    public void displaysubjectlist(){

        // Value lini
        String University=null;
        String Semester=null;
        String Faculty=null;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            University = bundle.getString("University", University);
            Semester = bundle.getString("Semester", Semester);
            Faculty = bundle.getString("Faculty", Faculty);
        }

//        Toast.makeText(getActivity(), "Notes ma xiryo "+University+" " +Semester+ " "+Faculty, Toast.LENGTH_SHORT).show();

        final Cursor data=myDb.getRequiredSubject(University,Semester,Faculty);
        final ArrayList<String>listData=new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the arraylist
            listData.add(data.getString(0));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,listData);
        lvSubject.setAdapter(adapter);
        //setting item click listener in listview subjects
        final String finalUniversity = University;
        final String finalSemester = Semester;
        final String finalFaculty = Faculty;
        lvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String subject=adapterView.getItemAtPosition(i).toString();
                Log.d(TAG,"onItemClick: You clicked on " + subject);

                Fragment fragment = new NotesView();
                //Value pass

                Bundle bundle = new Bundle();
                bundle.putString ("University", finalUniversity);
                bundle.putString ("Semester", finalSemester);
                bundle.putString ("Faculty", finalFaculty);
                bundle.putString ("subject", subject);

                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

//                Toast.makeText(getActivity(), "Clicked "+subject, Toast.LENGTH_SHORT).show();
////
////
////                Cursor d=myDb.getRequiredNote(finalUniversity, finalSemester, finalFaculty,subject); //get the id associated with that subject
////                while (d.moveToNext()){
////                    listData.add(data.getString(4));
//////                }
//////
//////                if(noteID > -1){
//////                    Log.d(TAG,"onItemClick: The ID is" + noteID);
//////                    Toast.makeText(getActivity(), noteID + " " +subject, Toast.LENGTH_SHORT).show();

////
////
////                }
////                else{
////                    Toast.makeText(getActivity(), "No associated with this subject", Toast.LENGTH_SHORT).show();
////                }
//

//    try
//    {
//        Cursor cursor= myDb.getAllSubjects();
//        if(cursor==null)
//        {
//            Toast.makeText(getActivity(), "Unable to generate Cursor", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(cursor.getCount()==0)
//        {
//            Toast.makeText(getActivity(), "No Subjects in the Database", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String[] columns=new String[]{
//                myDb.COL_1,
//                myDb.COL_6,
//
//
//        };
//
//        int[] boundto=new int[]{
////                R.id.cid,
//                R.id.subject_txt,
//        };
//        simpleCursorAdapter=new SimpleCursorAdapter(getActivity(),R.layout.notes_layout,cursor,columns,boundto,0);
//        lvSubject.setAdapter(simpleCursorAdapter);
//
//    }
//    catch (Exception ex)
//    {
//        Toast.makeText(getActivity(), "There was an error", Toast.LENGTH_SHORT).show();
//    }
}
}