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
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Raken on 4/5/2018.
 */

public class Backup extends Fragment {
    private EditText uname;
    private EditText pwd;
    Button login;
    int c = 0;
    public String username, password;

    @Nullable


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Admin Menu");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.backup_layout, container, false);
        uname = (EditText) v.findViewById(R.id.admin_username);
        pwd = (EditText) v.findViewById(R.id.admin_password);
        login = (Button) v.findViewById(R.id.admin_btn);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=null;
                username = uname.getText().toString().trim();
                password = pwd.getText().toString().trim();
//                Toast.makeText(getActivity(), username + " " + password, Toast.LENGTH_SHORT).show();
                if (c == 0) {
                    if (username.equals("admin") && password.equals("admin")) {
                        c++;
                         fragment = new Admin();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, fragment);
//                        ft.addToBackStack("MainActivity");
                        ft.commit();
                    } else {
                        Toast.makeText(getActivity(), "Incorrect Credentials.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    fragment = new Admin();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment, "A");
//                    ft.addToBackStack("MainActivity");
                    ft.commit();

                }

            }
        });
            return v;
    }
}

