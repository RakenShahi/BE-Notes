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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Raken on 4/5/2018.
 */

public class DeleteNotes extends Fragment{

    private static Button button_show_notes;
    String facultyvalue,semestervalue;
    RadioGroup uni;
    RadioButton univalue;
    DatabaseHelper myDb;
    Spinner faculty,semester;
    ArrayAdapter<CharSequence> adapter;
//    SimpleCursorAdapter simpleCursorAdapter;
//    ListView lvSubject;

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Delete Notes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View Ffrag=inflater.inflate(R.layout.delete_layout,container,false);
//        myDb = new DatabaseHelper(getActivity());
//        lvSubject=(ListView)Ffrag.findViewById(R.id.subjectlistview);
//
        /*Spinner ko work*/
        semester = (Spinner) Ffrag.findViewById(R.id.spinnersemester);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.select_semester, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter);

        faculty = (Spinner) Ffrag.findViewById(R.id.spinnerfaculty);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.select_faculty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty.setAdapter(adapter);
       // spinner ko eta samma

        //Submit Button ko lagi work
        uni=(RadioGroup)Ffrag.findViewById(R.id.University);
        button_show_notes=(Button)Ffrag.findViewById(R.id.btn_show_sub);
        button_show_notes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
                        int uniid=uni.getCheckedRadioButtonId(); //radio ko id leko
                        univalue=(RadioButton)Ffrag.findViewById(uniid); // radio ko value leko
//                        Toast.makeText(getActivity(), semester.getSelectedItem().toString() +" " + faculty.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(), univalue.getText().toString(), Toast.LENGTH_SHORT).show(); // University radio button check gareko



                        Fragment fragment = new ShowSubsForDelete();

                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, fragment);
                        ft.addToBackStack(null);
                        ft.commit();

                        Bundle bundle = new Bundle();
                        bundle.putString ("University",univalue.getText().toString());
                        bundle.putString ("Semester",semester.getSelectedItem().toString());
                        bundle.putString ("Faculty",faculty.getSelectedItem().toString());
                        fragment.setArguments(bundle);



//
//                        // for spinner value
//                        if (semester.getSelectedItem() == null && faculty.getSelectedItem()==null) {
//                            return;
//                        }
//                        else {
////                            Toast.makeText(getActivity(), semester.getSelectedItem().toString() +" " + faculty.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                            semestervalue = semester.getSelectedItem().toString();
//                            facultyvalue=faculty.getSelectedItem().toString();
//                        }
//                        //spinner value yeta samma
//
//                        Toast.makeText(getActivity(), univalue.getText().toString(), Toast.LENGTH_SHORT).show(); // University radio button check gareko
//                        boolean isInserted=myDb.insertUniversityData(univalue.getText().toString(),facultyvalue,semestervalue);
//                        if (isInserted=true) {
//                            Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();
//                            Fragment fragment = new Notes();
//                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_main, fragment);
//                            ft.addToBackStack(null);
//                            ft.commit();
//                        }
//                        else
//                            Toast.makeText(getActivity(), "Data not Inserted", Toast.LENGTH_SHORT).show();

                    }
                }
//
        );
        //Submit button ko work yaha samma

        return Ffrag;

    }

}
