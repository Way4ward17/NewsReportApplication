package com.theway4wardacademy.report.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.theway4wardacademy.report.R;
import com.theway4wardacademy.report.Utils.Constant;
import com.theway4wardacademy.report.Utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.iceteck.silicompressorr.FileUtils.getDataColumn;
import static com.iceteck.silicompressorr.FileUtils.isDownloadsDocument;
import static com.iceteck.silicompressorr.FileUtils.isMediaDocument;
import static com.iceteck.silicompressorr.Util.isExternalStorageDocument;

public class AddNews extends AppCompatActivity {

    ImageView imageView1, imageView2;
    File videoFile, fileVideoImage, audioFile, imageFile;
    Uri videoUri, videoImageUri;
    EditText text;
    MediaMetadataRetriever retriever;
    MediaPlayer mediaPlayer;
    String textForm;
    String date;
    TextView audioBtn, videoBtn, imageBtn;
    String videoAv = "No", audioAv = "No", imageAv = "No";


    private static final String IMAGE_DIRECTORY = "/Report";
    public File cachefolder = new File(
            Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/Delete Regularly");


    ActivityResultLauncher<String> mGetContent, mGetContent2,mGetContentAudio, mGetContentVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        text = (EditText)findViewById(R.id.textField);

        audioBtn = (TextView) findViewById(R.id.audioButton);
        videoBtn = (TextView) findViewById(R.id.videoButton);
        imageBtn = (TextView) findViewById(R.id.photoButton);


        mGetContentAudio = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                audioBtn.setBackgroundColor(Color.parseColor("#FF14A81B"));
                audioAv = "Yes";

                String path = FileUtils.getPath(AddNews.this, result);
                audioFile = new File(path);

            }
        });

        mGetContentVideo = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                videoBtn.setBackgroundColor(Color.parseColor("#FF14A81B"));
                videoAv = "Yes";
                new setVideo().execute(result);

            }
        });


        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageBtn.setBackgroundColor(Color.parseColor("#FF14A81B"));
                imageAv = "Yes";
                imageView1.setImageURI(result);
                String path = FileUtils.getPath(AddNews.this, result);
                imageFile = new File(path);

            }
        });


    }




    public void chooseImage(View view) {
            mGetContent.launch("image/*");
    }

    public void chooseAudio(View view) {
        mGetContentAudio.launch("audio/*");
    }

    public void chooseVideo(View view) {
        mGetContentVideo.launch("video/*");
    }

    public void uploadPost(View view) {
       new BackgroundUpload().execute();
    }


    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        return sdf.format(new Date());
    }

    private void getData(){
         textForm = text.getText().toString();
         date = getTimestamp();

    }



    private void UploadVideo(){
        getData();

        AndroidNetworking.upload(Constant.UPLOADVIDEP)
                .addMultipartFile("image", videoFile)
                .addMultipartFile("image2", fileVideoImage)
                .addMultipartFile("audio", audioFile)
                .addMultipartFile("picture", imageFile)
                .addMultipartParameter("folder", SharedPrefManager.getInstance(AddNews.this).getID())
                .addMultipartParameter("type", "video")
                .addMultipartParameter("video", videoAv)
                .addMultipartParameter("audio", audioAv)
                .addMultipartParameter("image", imageAv)
                .addMultipartParameter("text", textForm)
                .addMultipartParameter("time", date)
                .addMultipartParameter("userid",SharedPrefManager.getInstance(AddNews.this).getID())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        float progress = (float) bytesUploaded / totalBytes * 100;


                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject product1 = new JSONObject(response);
                            int success = product1.getInt("status");
                            String message = product1.getString("message");
                            if (success == 0) {

                                Toast.makeText(AddNews.this, message,Toast.LENGTH_LONG).show();

//                                dialog.dismiss();
                            } else {
//                                dialog.dismiss();
                                Toast.makeText(AddNews.this, message,Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
//                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                     //   dialog.dismiss();
                        //.makeText(getApplicationContext(), "Error Uploading" + anError, //.LENGTH_LONG).show();

                    }
                });

    }



    private void AudioUpload(){

    }



    class BackgroundUpload extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... uri) {
            UploadVideo();
            return null;
        };

    }



    class setVideo extends AsyncTask<Uri,Void,Void> {

        @Override
        protected void onPreExecute() {
            retriever = new MediaMetadataRetriever();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Uri... data) {

            Uri videoContentUri = data[0];
            String path = FileUtils.getPath(AddNews.this, videoContentUri);
            videoFile = new File(path);
            videoUri = Uri.fromFile(videoFile);
            retriever.setDataSource(videoFile.getAbsolutePath());
            videoImageUri = getImageUri(AddNews.this, retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST));
            String pathVideoImage = getPath(AddNews.this, compressImage(videoImageUri));
            fileVideoImage = new File(pathVideoImage);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageView1.setImageURI(videoImageUri);
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