package com.example.administrator.benotes;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
/**
 * Created by Raken .
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper myDb;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    int count=0;
    TextView admin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDb = new DatabaseHelper(this);
        firebaseAuth= FirebaseAuth.getInstance();
         user =firebaseAuth.getCurrentUser();

        try {
            myDb.createDatabase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDb.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }
//        Toast.makeText(MainActivity.this, "Successfully Imported", Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview=navigationView.getHeaderView(0);
        admin_btn=(TextView) headerview.findViewById(R.id.secret);
        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                count++;
                if(count>=10){
                    Toast.makeText(MainActivity.this, "Admin mode", Toast.LENGTH_SHORT).show();
                    Fragment fragment=new Backup();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main,fragment,"A");
                    ft.commit();
                }
            }
        });


        Cursor res = myDb.getfieldinformation();
        if (res.getCount() == 0) {
            displaySelectedScreen(R.id.nav_field);
            toolbar.setNavigationIcon(null);
        }
        else {

            res.moveToFirst();

                    String uni = res.getString(res.getColumnIndex("University"));
                    String sem=res.getString(res.getColumnIndex("Semester"));
                    String fac=res.getString(res.getColumnIndex("Faculty"));
//                    Toast.makeText(this,uni+sem+fac, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString ("University",uni);
                    bundle.putString ("Semester",sem);
                    bundle.putString ("Faculty",fac);

                    Fragment fragment =new AllNotes();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main,fragment);
                    ft.commit();


        }

//


    }

    @Override
    public void onBackPressed() {
        RelativeLayout fl = (RelativeLayout) findViewById(R.id.content_main);
        if (fl.getChildCount() == 1) {
            super.onBackPressed();
            if (fl.getChildCount() == 0) {
                Cursor res = myDb.getfieldinformation();

                if (res.getCount() == 0) {
                    displaySelectedScreen(R.id.nav_field);
                }
                else {
                    displaySelectedScreen(R.id.nav_Notes);

                }
                new AlertDialog.Builder(this)
                        .setTitle("Close App?")
                        .setMessage("Do you really want to exit this app?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Cursor res = myDb.getfieldinformation();

                                        if (res.getCount() == 0) {
                                            displaySelectedScreen(R.id.nav_field);
                                        }
                                        else {
                                            displaySelectedScreen(R.id.nav_Notes);

                                        }

                                    }
                                }).show();
                // load your first Fragment here
            }
        } else if (fl.getChildCount() == 0) {
            // load your first Fragment here
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id){
        Fragment fragment=null;

        switch (id){
            case R.id.nav_Admin:
                fragment =new Admin();
                break;
            case R.id.nav_Notes:
                fragment =new AllNotes();
                Cursor res = myDb.getfieldinformation();

                    res.moveToFirst();

                    String uni = res.getString(res.getColumnIndex("University"));
                    String sem=res.getString(res.getColumnIndex("Semester"));
                    String fac=res.getString(res.getColumnIndex("Faculty"));
//                    Toast.makeText(this,uni+sem+fac, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString ("University",uni);
                    bundle.putString ("Semester",sem);
                    bundle.putString ("Faculty",fac);
                    fragment.setArguments(bundle);

                break;
            case R.id.nav_field:
                user =firebaseAuth.getCurrentUser();
                if(user==null) {
                    fragment =new Field();
                }
                else{
                    startActivity(new Intent(MainActivity.this,Field_on_net.class));
                }

                break;
            case R.id.nav_profile:
//                fragment =new LogIn();
                Intent intent=new Intent("com.example.administrator.benotes.FirebaseLogin");
                startActivity(intent);
                break;
            case R.id.nav_questions:
                user =firebaseAuth.getCurrentUser();
                if(user==null) {
                    fragment =new Questions();
                }
                else{
                    startActivity(new Intent(MainActivity.this,Questions_RecyclerViewActivity.class));
                }
                break;
            case R.id.nav_share:

                    startActivity(new Intent(MainActivity.this,FirebaseLogin.class));


                break;
            case R.id.nav_syllabus:
                user =firebaseAuth.getCurrentUser();
                if(user==null) {
                    fragment =new Syllabus();
                    }
                else{
                    startActivity(new Intent(MainActivity.this,Syllabus_RecyclerViewActivity.class));
                }

                break;
            case R.id.secret:

                break;
        }
        if (fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main,fragment,"A");
            ft.addToBackStack("MainActivity");
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
