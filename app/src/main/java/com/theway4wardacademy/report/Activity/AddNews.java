package com.theway4wardacademy.report.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theway4wardacademy.report.R;

public class AddNews extends AppCompatActivity {

    private int cameraCode  = 419;
    ImageView imageView1, imageView2;

    ActivityResultLauncher<String> mGetContent, mGetContent2,mGetContentAudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);


        mGetContentAudio = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageView1.setImageURI(result);

            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageView1.setImageURI(result);

            }
        });

        mGetContent2 = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageView2.setImageURI(result);

            }
        });

    }



    //    private void pickFromGallery(){
//        Intent intent = new Intent(/**Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI**/);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        startActivityForResult(Intent.createChooser(intent, "Select Image"), cameraCode);
//
//    }

    public void chooseImage(View view) {
            mGetContent.launch("image/*");
    }

    public void chooseAudio(View view) {
        mGetContentAudio.launch("audio/*");
    }

    public void chooseImage2(View view) {
        mGetContent2.launch("image/*");
    }

    public void uploadPost(View view) {
    }
}