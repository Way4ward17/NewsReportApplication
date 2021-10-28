package com.theway4wardacademy.report.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;


import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.theway4wardacademy.report.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.iceteck.silicompressorr.FileUtils.getDataColumn;
import static com.iceteck.silicompressorr.FileUtils.isDownloadsDocument;
import static com.iceteck.silicompressorr.FileUtils.isMediaDocument;
import static com.iceteck.silicompressorr.Util.isExternalStorageDocument;

public class AddNews extends AppCompatActivity {

    ImageView imageView1, imageView2;
    File videoFile, fileVideoImage;
    Uri videoUri, videoImageUri;
    MediaMetadataRetriever retriever;


    private static final String IMAGE_DIRECTORY = "/Report";
    public File cachefolder = new File(
            Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/Delete Regularly");


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


    private void ImageUpload(){

    }


    private void VideoUpload(){

    }


    private void AudioUpload(){

    }



    class setVideo extends AsyncTask<Intent,Void,Void> {

        @Override
        protected void onPreExecute() {
            retriever = new MediaMetadataRetriever();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Intent... data) {

            Uri videoContentUri = data[0].getData();
            String path = FileUtils.getPath(AddNews.this, videoContentUri);
            videoFile = new File(path);
            videoUri = Uri.fromFile(videoFile);
            retriever.setDataSource(videoFile.getAbsolutePath());
            videoImageUri = getImageUri(AddNews.this, retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST));
            String pathVideoImage = getPath(AddNews.this, compressImage(videoImageUri));
            fileVideoImage = new File(pathVideoImage);

            return null;
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public Uri compressImage(Uri imageUriCompress) {
        Uri uri = null;
        if (imageUriCompress != null) {
            File file = new File(SiliCompressor
                    .with(AddNews.this).compress(FileUtils.getPath(AddNews.this, imageUriCompress), new File(cachefolder.getPath())));
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(cachefolder.exists()){
            cachefolder.mkdir();
        }
    }
}