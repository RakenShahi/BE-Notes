package com.example.administrator.benotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Raken on 4/5/2018.
 */

public class SetUpProfileImage extends Fragment{
    private static final int IMAGE_PICKER_SELECT = 999;
    public Button btn_save;
    EditText uname;
    ImageView mSelectedImage;
    Uri uriProfileImage;
    ProgressBar imgpg;
    String profileImageUrl;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }

    public SetUpProfileImage(){
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.set_up_profile_layout,container,false);
        uname=(EditText)v.findViewById(R.id.txt_username);
        mSelectedImage=(ImageView)v.findViewById(R.id.img_view);
        imgpg=(ProgressBar)v.findViewById(R.id.img_pg);
        mAuth=FirebaseAuth.getInstance();
        loadUserInformation();



        mSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showImageChooser();

                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_PICKER_SELECT);

                //yaha samma image ko
            }
        });


        btn_save=(Button)v.findViewById(R.id.save_button);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();

            }
        });
        return v;
    }

    private void loadUserInformation() {
        FirebaseUser user=mAuth.getCurrentUser();

        if(user!=null) {
//            if (user.getPhotoUrl() != null) {
////                Glide.with(getActivity()).load(user.getPhotoUrl().toString())
//                        .into(mSelectedImage);
//            }
            if (user.getDisplayName() != null) {
                uname.setText(user.getDisplayName());

            }
        }

    }

    private void saveUserInformation() {

        String username=uname.getText().toString();
        if(username.isEmpty()){
            uname.setError("Username required");
            uname.requestFocus();
            return;
        }



        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null && profileImageUrl!=null){
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder()
            .setDisplayName(username)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        Fragment fragment =new Backup();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main,fragment,"S");
                        ft.addToBackStack("MainActivity");

                        FragmentManager fm=getActivity().getSupportFragmentManager();
//                        fm.popBackStack("MainActivity",0);
                        fm.popBackStack("Login", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ft.commit();

                    }
                }
            });
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == IMAGE_PICKER_SELECT  && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null ) {
        MainActivity activity = (MainActivity)getActivity();
        uriProfileImage=data.getData();
        Bitmap bitmap = getBitmapFromCameraData(data, activity);
        mSelectedImage.setImageBitmap(bitmap);
        uploadImageToFirebaseStorage();
    }
}

    private void setFullImageFromFilePath(String imagePath) {
        // Get the dimensions of the View
        int targetW = mSelectedImage.getWidth();
        int targetH = mSelectedImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        mSelectedImage.setImageBitmap(bitmap);
    }

    /**
     * Use for decoding camera response data.
     *
     * @param data
     * @param context
     * @return
     */
    public static Bitmap getBitmapFromCameraData(Intent data, Context context){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }




    //Firebase upload image
    private void uploadImageToFirebaseStorage(){
        final StorageReference profileImageReference= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage==null){
                        Toast.makeText(getActivity(), "Image cannot be blank", Toast.LENGTH_SHORT).show();

                    }
        if(uriProfileImage!=null){
            imgpg.setVisibility(View.VISIBLE);
            profileImageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgpg.setVisibility(View.GONE);
                    profileImageUrl=taskSnapshot.getDownloadUrl().toString();


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                    imgpg.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
//            profileImageReference.putFile(uriProfileImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    imgpg.setVisibility(View.GONE);
//                    if(task.isSuccessful()){
//                        task.
////                        Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }

}




