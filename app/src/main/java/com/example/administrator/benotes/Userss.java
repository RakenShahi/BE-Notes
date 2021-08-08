package com.example.administrator.benotes;

import android.widget.Toast;

/**
 * Created by Raken.
 */

public class Userss {

    public long name, image;

    public Userss(long name, long image) {
        this.name = name;
        this.image = image;


    }


    public Userss(){

    }




    public long getName() {
        return name;
    }

    public void setName(long name) {
        this.name = name;
    }

    public long getImage() {
        return image;
    }

    public void setImage(long image) {
        this.image = image;
    }


}
