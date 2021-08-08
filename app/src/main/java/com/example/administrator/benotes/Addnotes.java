package com.example.administrator.benotes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Raken on 4/5/2018.
 */

public class Addnotes extends Fragment{
    @Nullable

    Spinner faculty,semester;
    ArrayAdapter<CharSequence> adapter;
    private static Button button_add;
    String facultyvalue,semestervalue;
    RadioGroup uni;
    RadioButton univalue;
    DatabaseHelper myDb;
    EditText subject;
    EditText author;
    EditText name;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Notes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View af= inflater.inflate(R.layout.add_notes,container,false);
        myDb = new DatabaseHelper(getActivity());

        /*Spinner ko work*/
        semester = (Spinner) af.findViewById(R.id.spinnersemester);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.select_semester, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter);

        faculty = (Spinner) af.findViewById(R.id.spinnerfaculty);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.select_faculty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty.setAdapter(adapter);
        //spinner ko eta samma

        //Submit Button ko lagi work
        uni=(RadioGroup)af.findViewById(R.id.University);
        button_add=(Button)af.findViewById(R.id.buttonadd);
        subject=(EditText)af.findViewById(R.id.subject_txt);
        author=(EditText)af.findViewById(R.id.author_txt);
        name=(EditText)af.findViewById(R.id.note_name_txt);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int uniid=uni.getCheckedRadioButtonId(); //radio ko id leko
                univalue=(RadioButton)af.findViewById(uniid); // radio ko value leko
                semestervalue = semester.getSelectedItem().toString();
                facultyvalue=faculty.getSelectedItem().toString();
                boolean isInserted=myDb.insertNotes(univalue.getText().toString(),semestervalue,facultyvalue,name.getText().toString(),subject.getText().toString(),author.getText().toString());
                if (isInserted=true) {
                    Toast.makeText(getActivity(), "Data Intersted", Toast.LENGTH_SHORT).show();
//                    Fragment fragment = new Notes();
//                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_main, fragment);
//                    ft.addToBackStack(null);
//                    ft.commit();
                }
                else
                    Toast.makeText(getActivity(), "Data not Inserted", Toast.LENGTH_SHORT).show();

            }


        });

        return af;

    }
}
