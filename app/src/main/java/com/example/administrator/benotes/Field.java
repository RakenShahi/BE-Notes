package com.example.administrator.benotes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Raken on 4/5/2018.
 */

public class Field extends Fragment{
    Spinner faculty,semester;
    ArrayAdapter<CharSequence> adapter;
    private static Button button_sbm;
    String facultyvalue,semestervalue;
    RadioGroup uni;
    RadioButton univalue;
    DatabaseHelper myDb;

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Field");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View Ffrag= inflater.inflate(R.layout.field_layout ,container,false);
        myDb = new DatabaseHelper(getActivity());

        /*Spinner ko work*/
        semester = (Spinner) Ffrag.findViewById(R.id.spinnersemester);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.select_semester, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter);

        faculty = (Spinner) Ffrag.findViewById(R.id.spinnerfaculty);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.select_faculty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty.setAdapter(adapter);
        //spinner ko eta samma

        //Submit Button ko lagi work
        uni=(RadioGroup)Ffrag.findViewById(R.id.University);

        button_sbm=(Button)Ffrag.findViewById(R.id.buttonsub);
        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Cursor res = myDb.getfieldinformation();
                        if (res.getCount() == 0) {
                            Intent i = getActivity().getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }


//                        Toast.makeText(getActivity(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
                        int uniid=uni.getCheckedRadioButtonId(); //radio ko id leko
                        univalue=(RadioButton)Ffrag.findViewById(uniid); // radio ko value leko
//                        Toast.makeText(getActivity(), semester.getSelectedItem().toString() +" " + faculty.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(), univalue.getText().toString(), Toast.LENGTH_SHORT).show(); // University radio button check gareko

                        // for spinner value
                        if (semester.getSelectedItem() == null && faculty.getSelectedItem()==null) {
                            return;
                        }
                        else {
//                            Toast.makeText(getActivity(), semester.getSelectedItem().toString() +" " + faculty.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            semestervalue = semester.getSelectedItem().toString();
                            facultyvalue=faculty.getSelectedItem().toString();
                        }
                        //spinner value yeta samma


                        if (res.getCount() == 0) {
                            boolean isInserted = myDb.insertUniversityData(univalue.getText().toString(), facultyvalue, semestervalue);
                            if (isInserted = true) {
                                Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putString ("University",univalue.getText().toString());
                                bundle.putString ("Semester",semester.getSelectedItem().toString());
                                bundle.putString ("Faculty",faculty.getSelectedItem().toString());

                                //Fragment work
                                Fragment fragment = new AllNotes();
                                fragment.setArguments(bundle);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main, fragment);
                                ft.addToBackStack(null);
                                ft.commit();
                            }

                        }
                            else
                        {
                            myDb.updateUniversityData(univalue.getText().toString(), facultyvalue, semestervalue);
                            Toast.makeText(getActivity(), "Data Updated", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getActivity(), univalue.getText().toString()+ " "+facultyvalue+" "+semestervalue, Toast.LENGTH_SHORT).show(); // University radio button check gareko

                            Bundle bundle = new Bundle();
                            bundle.putString ("University",univalue.getText().toString());
                            bundle.putString ("Semester",semester.getSelectedItem().toString());
                            bundle.putString ("Faculty",faculty.getSelectedItem().toString());

                            //Fragment work
                            Fragment fragment = new Notes();
                            fragment.setArguments(bundle);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_main, fragment);
                            ft.addToBackStack(null);
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.popBackStack ("Field", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            ft.commit();

                        }


                            //Pass the values



                    }
                }

        );
    //Submit button ko work yaha samma

        return Ffrag;

    }


}
