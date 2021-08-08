package com.example.administrator.benotes;

/**
 * Created by Raken.
 */

public class SyllabusInfo {
    public String Url;
    public String syllabus_id;
    public  String syllabus_name;

//    public SyllabusInfo(){
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
//    public String getSyllabus_id() {
//        return syllabus_id;
//    }
//
//    public void setSyllabus_id(String syllabus_id) {
//        this.syllabus_id = syllabus_id;
//    }
//
//    public String getSyllabus_name() {
//        return syllabus_name;
//    }
//
//    public void setSyllabus_name(String syllabus_name) {
//        this.syllabus_name = syllabus_name;
//    }

    public SyllabusInfo(String id, String syllabus_name, String urls) {
        this.syllabus_id=id;
        this.syllabus_name = syllabus_name;
        this.Url=urls;

    }
}
