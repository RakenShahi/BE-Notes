package com.example.administrator.benotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by Raken on 3/12/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    public static final String DATABASE_NAME = "BENotes.db";
    public final static String DATABASE_PATH ="/data/data/com.example.administrator.benotes/databases/";
    public static final int DATABASE_VERSION = 1;
//    public static final int DATABASE_VERSION_old = 1;


    public static final String TABLE_NAME = "User_uni_info";
    public static final String TABLE_NAME2 = "Notes";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "University";
    public static final String COL_3 = "Faculty";
    public static final String COL_4 = "Semester";
    public static final String COL_5 = "Name";
    public static final String COL_6 = "Subject";
    public static final String COL_7 = "Author";


    //Constructor
    public DatabaseHelper(Context myContext)
    {
        super(myContext, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = myContext;
    }


//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, 1);
//
//    }

    public void createDatabase() throws IOException
    {
        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
//            onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if(!dbExist1)
        {
            this.getWritableDatabase();
            try
            {
                copyDataBase();
                this.close();

            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    //delete database
    public void db_delete()
    {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException
    {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,University TEXT,Faculty TEXT,Semester TEXT)");
//        db.execSQL("create table Notes (ID INTEGER PRIMARY KEY AUTOINCREMENT,University TEXT,Semester TEXT,Faculty TEXT,Name TEXT,Subject TEXT,Author TEXT)");
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
//        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
//        onCreate(db);
        if (newversion > oldversion)
        {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // yaha samma open db ko kaam

    public boolean insertUniversityData(String University,String Faculty,String Semester){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,University);
        contentValues.put(COL_3,Faculty);
        contentValues.put(COL_4,Semester);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public void updateUniversityData(String University,String Faculty,String Semester){
        SQLiteDatabase db=this.getWritableDatabase();
//        String Query="UPDATE User_uni_info SET ID='1' AND University='"+University+"' AND Faculty='"+Faculty+"' AND Semester='"+Semester+"' WHERE ID='1'" ;
//         db.execSQL(Query);

        ContentValues args = new ContentValues();
        args.put(COL_1, 1);
        args.put(COL_2, University);
        args.put(COL_3, Faculty);
        args.put(COL_4, Semester);

        db.update(TABLE_NAME, args, "ID=1", null);


    }

    public boolean insertNotes(String University,String Semester,String Faculty,String Name, String Subject, String Author){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,University);
        contentValues.put(COL_3,Faculty);
        contentValues.put(COL_4,Semester);
        contentValues.put(COL_5,Name);
        contentValues.put(COL_6,Subject);
        contentValues.put(COL_7,Author);


        long result=db.insert(TABLE_NAME2,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }




    public Cursor getfieldinformation(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME,null);
        return res;

    }

    public Cursor getAllSubjects(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME2 ,null);
        return res;
    }

    public Cursor getNoteId(String sub){
        SQLiteDatabase db=this.getReadableDatabase();
String query="SELECT ID FROM Notes WHERE Subject='"+sub+"'";
Cursor data=db.rawQuery(query,null);
return data;
    }

    public Cursor getRequiredNote(String uni,String sem,String faculty,String sub){
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT Name FROM Notes WHERE University='" + uni + "' AND Subject='" + sub + "' AND Semester='" + sem + "' AND Faculty='" + faculty + "'";
        Cursor data=db.rawQuery(query,null);
        return data;
    }

    public Cursor getRequiredSubject(String uni,String sem,String faculty){
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT DISTINCT(Subject) FROM Notes WHERE University='" + uni + "' AND Semester='" + sem + "' AND Faculty='" + faculty + "'";
        Cursor data=db.rawQuery(query,null);
        return data;
    }

    public Cursor viewRelatedNotes(String uni,String sem,String faculty){
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT Name FROM Notes WHERE University='" + uni + "' AND Semester='" + sem + "' AND Faculty='" + faculty + "'";
        Cursor data=db.rawQuery(query,null);
        return data;
    }




    public void deleteselectednote(String uni,String sem,String faculty,String sub) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE From Notes WHERE University='" + uni + "' AND Subject='" + sub + "' AND Semester='" + sem + "' AND Faculty='" + faculty + "'";
        Log.d(TAG, "DeleteName: query " + query);
        Log.d(TAG, "DeleteName: Deleting" + sub + "From notes");

            db.execSQL(query);


    }



//    public boolean updateData(String id,String University,String Semester,String Faculty,String Name, String Subject, String Author) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_2,University);
//        contentValues.put(COL_3,Semester);
//        contentValues.put(COL_4,Faculty);
//        contentValues.put(COL_5,Name);
//        contentValues.put(COL_6,Subject);
//        contentValues.put(COL_7,Author);
//        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
//        return true;
//    }
    public boolean updateField(Field p)
    {
        boolean result=false;
        String q="SELECT * FROM "+ TABLE_NAME +"WHERE " + COL_1 +"="+ p.getId();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery(q,null);
        if(c.moveToFirst()){
//            String q2="UPDATE"+TABLE_NAME+"SET"+COL_2 + "= '" p.getUniversity()+"'","+COL_3+"="+p.getSemester()+"WHERE"+COL_1+"="p.getId();
//            db.execSQL(q2);
            result=true;
        }
        db.close();
        return result;


    }
    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
