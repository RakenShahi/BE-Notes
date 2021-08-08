package com.example.administrator.benotes;

/**
 * Created by Raken on 5/22/2018.
 */

public class Search_data {
    private String User_id;
    private String Search_subject;

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getSearch_subject() {
        return Search_subject;
    }

    public void setSearch_subject(String search_key) {
        Search_subject = search_key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;

    public Search_data(){

    }

    public Search_data(String uid,String sk,String ts){

        this.User_id=uid;
        this.Search_subject=sk;
        this.timestamp=ts;
    }


}
