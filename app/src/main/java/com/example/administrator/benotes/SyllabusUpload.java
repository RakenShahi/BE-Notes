package com.example.administrator.benotes;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
/**
 * Created by Raken.
 */
public class SyllabusUpload extends AppCompatActivity {

    Spinner faculty,semester;
    ArrayAdapter<CharSequence> adapter;
    String facultyvalue,semestervalue,university,sub,note_author,filename;
    RadioGroup uni;
    RadioButton univalue;


    Button selectFile,upload;
    TextView notification;
    Uri pdfUri; // uri are actually URLS that are meant for local storage

    FirebaseStorage storage; // used for uploading files
    FirebaseDatabase database; //used to store URLs of uploaded files
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus_upload);

        storage=FirebaseStorage.getInstance(); //returns an object of firebase storage
        database=FirebaseDatabase.getInstance(); // returns an object of firebase database

        /*Spinner ko work*/
        semester = (Spinner)findViewById(R.id.spinnersemester);
        adapter = ArrayAdapter.createFromResource(this, R.array.select_semester, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter);

        faculty = (Spinner)findViewById(R.id.spinnerfaculty);
        adapter = ArrayAdapter.createFromResource(this, R.array.select_faculty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty.setAdapter(adapter);
        //spinner ko eta samma

        selectFile=findViewById(R.id.selectFile);
        upload=findViewById(R.id.upload);
        uni=(RadioGroup)findViewById(R.id.University);

        notification=findViewById(R.id.notification);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(SyllabusUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED)
                {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(SyllabusUpload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfUri  !=null)//  the user has selected the file
                {

                    int uniid=uni.getCheckedRadioButtonId(); //radio ko id leko
                    univalue=(RadioButton)findViewById(uniid); // radio ko value leko

                    semestervalue = semester.getSelectedItem().toString();
                    facultyvalue=faculty.getSelectedItem().toString();
                    university=univalue.getText().toString();


                    if (university.isEmpty() || semestervalue.isEmpty()||facultyvalue.isEmpty()) {


                    } else {
                        uploadFile(pdfUri);
                    }
                }

                else
                    Toast.makeText(SyllabusUpload.this, "Select a File", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();


        final String Filename = System.currentTimeMillis() + ".pdf";
        final String filename1 = System.currentTimeMillis() + "";
        final String file=filename.substring(0, filename.lastIndexOf('.'));
        StorageReference storageReference = storage.getReference(); // returns root path

            storageReference.child("Syllabus").child(university).child(facultyvalue).child(semestervalue).child(filename).putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String url = taskSnapshot.getDownloadUrl().toString();// return the url of you uploaded file
//                            Toast.makeText(FileUpload.this, url, Toast.LENGTH_SHORT).show();
                            //store the url in realtime database.

                            DatabaseReference reference = database.getReference();// return the path to root
                            SyllabusInfo syllabusInfo = new SyllabusInfo(filename1,filename,url);
                            reference.child("Syllabus").child(university).child(facultyvalue).child(semestervalue).child(filename1).setValue(syllabusInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SyllabusUpload.this, "File is successfuly uploaded", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SyllabusUpload.this, "File realtime ma upload vayena", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SyllabusUpload.this, "File is not successfuly uploaded..vayena", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                         @Override
                                         public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                             //track the progress of our upload.
                                             int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                             progressDialog.setProgress(currentProgress);
                                         }
                                     }
            );
        }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            selectPdf();
        }
        else
            Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show();
    }

    private void selectPdf() {
        // to offer user to select  a file using file manager
        // we will be using intent

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether has selected the file or not.
        if(requestCode==86 && resultCode==RESULT_OK && data!=null){
            pdfUri=data.getData(); // return the uri of selected file.
            //to get filename
            String uriString = pdfUri.toString();
            File myFile = new File(uriString);
            filename = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(pdfUri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                filename = myFile.getName();
            }

            //file name ko lagi yaha samma

            notification.setText("File is selected :"+data.getData().getLastPathSegment());


        }
        else {
            Toast.makeText(this, "Please select a File", Toast.LENGTH_SHORT).show();
        }
    }
}
