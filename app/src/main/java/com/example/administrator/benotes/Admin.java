package com.example.administrator.benotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Raken on 4/5/2018.
 */

public class Admin extends Fragment{
    @Nullable
    private static Button button_add;
    private static Button button_del;
    private static Button firebase_upload;
    private static Button button_update;
    private static Button button_manage_user;
    private static Button button_update_question;
    private static Button button_update_syllabus;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Admin");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View af= inflater.inflate(R.layout.admin_layout ,container,false);
        button_add=(Button)af.findViewById(R.id.add_note);
        button_del=(Button)af.findViewById(R.id.delete_note);
        firebase_upload=(Button)af.findViewById(R.id.Firebase_upload_btn);
        button_update_question=(Button)af.findViewById(R.id.update_question);
        button_update_syllabus=(Button)af.findViewById(R.id.update_syllabus);



        //Button listeners
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Addnotes();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new DeleteNotes();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                Toast.makeText(getActivity(), "Delete Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        firebase_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("com.example.administrator.benotes.FileUpload");
                startActivity(intent);
            }
        });

        button_update_syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("com.example.administrator.benotes.SyllabusUpload");
                startActivity(intent);

            }
        });

        button_update_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("com.example.administrator.benotes.QuestionUpload");
                startActivity(intent);
            }
        });
        return af;
    }
}
