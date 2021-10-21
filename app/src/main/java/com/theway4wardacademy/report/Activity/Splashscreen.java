package com.theway4wardacademy.report.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.theway4wardacademy.report.Utils.SharedPrefManager;



public class Splashscreen extends Activity {


    private int integers  =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkLogin();
    }


    private void sendToMain() {
        Intent main = new Intent(Splashscreen.this, MainActivity.class);
        startActivity(main);
        finish();
    }






    private void checkLogin() {

        if (SharedPrefManager.getInstance(Splashscreen.this).isLoggedIn()) {
            sendToMain();
        } else {

            Intent main = new Intent(Splashscreen.this, ActivityLogin.class);
            startActivity(main);


        }
    }

}

