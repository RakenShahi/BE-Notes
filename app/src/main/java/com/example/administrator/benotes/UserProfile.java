package com.example.administrator.benotes;
/**
 * Created by Raken.
 */

public class UserProfile {
    public String Age;
    public String Email;
    public String Name;
    public String User_id;
    public String University;
    public String Semester;
    public String Faculty;

    public UserProfile(String userAge, String userEmail, String userName,String userId,String university,String Semester,String Faculty) {
        this.User_id=userId;
        this.Email = userEmail;
        this.Name = userName;
        this.Age = userAge;
        this.University=university;
        this.Semester=Semester;
        this.Faculty=Faculty;



    }
}
