package com.theway4wardacademy.report.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Way4wardPC on 10/12/2018.
 */
public class SharedPrefManager {
    private static SharedPrefManager mInstance;

    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "myshared";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "userid";

    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }



    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(
                KEY_USER_ID, Context.MODE_PRIVATE);
        if(sharedPreferences.getString("id", null) !=null){
          return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(
                KEY_USER_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;

    }


    public String getID(){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(
                KEY_USER_ID, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", null);
    }

    public boolean saveID(String id){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(
                KEY_USER_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);

        editor.apply();
        return true;
    }


}

