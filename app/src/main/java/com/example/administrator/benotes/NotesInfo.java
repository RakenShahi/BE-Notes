package com.example.administrator.benotes;


import android.widget.Toast;
/**
 * Created by Raken on 4/5/2018.
 */

public class NotesInfo {
    public String Url;
    public String author_name;
    public String note_id;
    public String note_name;
    public String subject;

//    public NotesInfo(){
//
//    }
//
//    public String getUrl() {
//        return Url;
//    }
//
//    public void setUrl(String url) {
//        Url = url;
//    }
//
//    public String getAuthor_name() {
//        return author_name;
//    }
//
//    public void setAuthor_name(String author_name) {
//        this.author_name = author_name;
//    }
//
//    public String getNote_id() {
//        return note_id;
//    }
//
//    public void setNote_id(String note_id) {
//        this.note_id = note_id;
//    }
//
//    public String getNote_name() {
//        return note_name;
//    }
//
//    public void setNote_name(String note_name) {
//        this.note_name = note_name;
//    }
//
//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }

    public NotesInfo(String userAge, String sub, String author, String urls, String name) {
        this.note_id="1";
        this.subject = sub;
        this.author_name = author;
        this.Url=urls;
        this.note_name=name;

    }
}
