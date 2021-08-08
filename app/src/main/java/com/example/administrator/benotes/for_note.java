package com.example.administrator.benotes;

/**
 * Created by Raken.
 */

public class for_note {

    private String Url;
    private String author_name;
    private String note_id;
    private String note_name;
    private String subject;

    public for_note(String url,String auth,String noteid,String notename,String sub) {
        this.Url = url;
        this.author_name = auth;
        this.note_id=noteid;
        this.note_name=notename;
        this.subject=sub;


    }


    public for_note(){

    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getNote_name() {
        return note_name;
    }

    public void setNote_name(String note_name) {
        this.note_name = note_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
