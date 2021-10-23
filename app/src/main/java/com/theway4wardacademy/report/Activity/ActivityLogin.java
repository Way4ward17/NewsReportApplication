package com.theway4wardacademy.report.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.theway4wardacademy.report.R;
import com.theway4wardacademy.report.Utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLogin extends AppCompatActivity {
    private TextView signupButton;
    private Button Login;
    private TextView gotosignup;
    Dialog dialog;
    private EditText emailPassword1, emailLogin1;
    FirebaseAuth auth;
    ProgressBar progressBar;
    private static final String TAG = "ActivityLogin";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        signupButton = (TextView)(findViewById(R.id.signupButton));
        emailLogin1 = (EditText)(findViewById(R.id.emailLogin));
        emailPassword1 = (EditText)(findViewById(R.id.emailPassword));
        Login = (Button)(findViewById(R.id.login));

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this,
                        Signup_Activity.class);
                startActivity(intent);
            }
        });
        initWideget();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                final String txt_email = emailLogin1.getText().toString();
               final String txt_password = emailPassword1.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Sho!!.. You wan login without anything .. ", Snackbar.LENGTH_SHORT).show();

                } else {
                    dialog.show();
                    AndroidNetworking.post(Constant.LOGIN)

                            .addBodyParameter("email", txt_email)
                            .addBodyParameter("password", txt_password)


                            .setTag("uploadTest")
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long bytesUploaded, long totalBytes) {
                                    // do anything with progress

                                    float progress = (float)bytesUploaded/totalBytes * 100;
                                }
                            })
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {


                                    try {
                                        JSONObject product1 = new JSONObject(response);
                                        int success = product1.getInt("status");

                                        if(success == 0) {
                                            dialog.dismiss();
                                            View parentLayout = findViewById(android.R.id.content);
                                            Snackbar.make(parentLayout, "Incorrect login", Snackbar.LENGTH_SHORT).show();
                                        }else{
                                            dialog.dismiss();
                                            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } catch (JSONException ex) {
                                        dialog.hide();
                                        ex.printStackTrace();
                                        Toast.makeText(ActivityLogin.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onError(ANError anError) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Error Uploading"+anError,Toast.LENGTH_LONG).show();

                                }
                            });



                }
            }
        });

    }

    private void initWideget(){
        dialog = new Dialog(ActivityLogin.this);
        dialog.setContentView(R.layout.dialogloading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        progressBar = (ProgressBar)dialog.findViewById(R.id.spin_kit);
    }

}
