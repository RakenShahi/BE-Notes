package com.example.administrator.benotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Raken on 4/5/2018.
 */

public class Syllabus extends Fragment{
    Button signin_btn;
    @Nullable


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Syllabus");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v= inflater.inflate(R.layout.syllabus_layout ,container,false);
        signin_btn=v.findViewById(R.id.btn_signin);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("com.example.administrator.benotes.FirebaseLogin");
                startActivity(intent);
            }
        });

                return v;

    }
}
