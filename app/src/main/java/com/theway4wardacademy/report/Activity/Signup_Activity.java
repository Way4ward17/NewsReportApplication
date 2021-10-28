package com.theway4wardacademy.report.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theway4wardacademy.report.R;
import com.theway4wardacademy.report.Utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Signup_Activity extends AppCompatActivity {



    ArrayList<String> agentList = new ArrayList<>();
    ArrayAdapter<String> agentAdapter;
    RequestQueue requestQueue;

    String userid;
    private EditText fullname, usernamee, password, mail, code;
    private Spinner department;
    private Button submit;
    RadioButton userRadio, agentRadio;
    private TextView login, read;
    private ImageView back;
    Dialog dialog, dialogg;
    ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference reference;
    String paystack_public_key = "https://theway4wardacademy.com/datapolicy.html";
    LinearLayout agentListPane;
    String acctType = "";
    int click = 0;
    private static final String TAG = "Signup_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_);
        auth = FirebaseAuth.getInstance();
        back = (ImageView) (findViewById(R.id.backSignup));
        fullname = (EditText) (findViewById(R.id.fullname));
        usernamee = (EditText) (findViewById(R.id.username));
        password = (EditText) (findViewById(R.id.password));
        code = (EditText) (findViewById(R.id.code));
        department = (Spinner) (findViewById(R.id.department));
        requestQueue = Volley.newRequestQueue(this);

        userRadio = (RadioButton)(findViewById(R.id.radioUser));
        agentRadio = (RadioButton)(findViewById(R.id.radioAgent));
        agentListPane = (LinearLayout)(findViewById(R.id.agentListPane));




        mail = (EditText) (findViewById(R.id.email1));

        reference = FirebaseDatabase.getInstance().getReference("Users");

        submit = (Button) (findViewById(R.id.login));
        login = (TextView) (findViewById(R.id.signupButton));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Signup_Activity.this, ActivityLogin.class);
                startActivity(intent);
            }
        });
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                click = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Select an agent", Snackbar.LENGTH_SHORT).show();
            }
        });


        getList();
        initWideget();
    }


    //BEGIN

    private void getList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.GETAGENT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("agents");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String countryName = jsonObject.optString("agentname");
                        agentList.add(countryName);
                        agentAdapter = new ArrayAdapter<>(Signup_Activity.this,
                                android.R.layout.simple_spinner_item, agentList);
                        agentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        department.setAdapter(agentAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
       // spinnerCountry.setOnItemSelectedListener(this);

    }

    public void userClick(View view) {
        agentListPane.setVisibility(View.GONE);
        acctType = "user";
    }
    public void agentClick(View view) {
        agentListPane.setVisibility(View.VISIBLE);
        acctType = "agent";
    }



    public void onClicku(View view) {
        dialog.show();
        String username = usernamee.getText().toString();
        String txt_username = fullname.getText().toString();

        String txt_email = mail.getText().toString();
        String txt_password = password.getText().toString();
        String txt_dept = department.getSelectedItem().toString();
        String codes = code.getText().toString();




        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Alaye, fill the necessary forms now", Snackbar.LENGTH_SHORT).show();
            dialog.dismiss();
        } else if (txt_password.length() < 6) {
            dialog.dismiss();
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Your password must pass 6 characters", Snackbar.LENGTH_SHORT).show();

        }else if(acctType ==""){
            dialog.dismiss();
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Select an Acct Type", Snackbar.LENGTH_SHORT).show();
        } else {
            if (click == 0 && agentListPane.getVisibility() == View.VISIBLE) {
                dialog.dismiss();
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Select an agent", Snackbar.LENGTH_SHORT).show();

            } else {
                saveUser(Random(), username, txt_username, acctType, txt_email, txt_password, txt_dept, codes);
            }
        }

    }


    private void saveUser(String userId,
                          String userName,
                          String fullName,
                          String acctType,
                          String email,
                          String password,
                          String department,
                          String code
                          ){

        AndroidNetworking.post(Constant.REG)
                .addBodyParameter("userid", userId)
                .addBodyParameter("username", userName)
                .addBodyParameter("fullname", fullName)
                .addBodyParameter("accttype", acctType)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("department", department)
                .addBodyParameter("code", code)

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
                            String message = product1.getString("message");

                            if(success == 0) {
                                dialog.dismiss();
                                Toast.makeText(Signup_Activity.this, "Error"+message, Toast.LENGTH_LONG).show();
                            }else{
                                dialog.dismiss();

                                Toast.makeText(Signup_Activity.this, message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Signup_Activity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException ex) {
                            dialog.dismiss();
                            ex.printStackTrace();
                            Toast.makeText(Signup_Activity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.hide();
                        Toast.makeText(getApplicationContext(), "Error Uploading"+anError,Toast.LENGTH_LONG).show();

                    }
                });

    }



    private void initWideget(){
        dialog = new Dialog(Signup_Activity.this);
        dialog.setContentView(R.layout.dialogloading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        progressBar = (ProgressBar)dialog.findViewById(R.id.spin_kit);
    }







    //END

    private void register(final String txt_username, final String txt_password
            , final String txt_department, final String txt_faculty, final String txt_email,
                          final String username) {
        auth.createUserWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            userid = firebaseUser.getUid();


                            HashMap<String, Object> hashMap = new HashMap<>();


                        } else if (!task.isSuccessful()) {

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e1) {
                                Log.e(TAG, e1.getMessage() + "WEAK PASSWORD");
                            } catch (FirebaseAuthInvalidCredentialsException e2) {
                                Log.e(TAG, e2.getMessage() + "INVALID");
                            } catch (FirebaseAuthUserCollisionException e3) {
                                Log.e(TAG, e3.getMessage() + "COLLISION");
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(Signup_Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, task.getException() + "", Snackbar.LENGTH_LONG).show();

                        }
                    }
                });


    }


    private void upload(final String deviceToken) {

        AndroidNetworking.post(Constant.REG)
                .addBodyParameter("userid", userid)
                .addBodyParameter("status", "offline")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress

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
                                Toast.makeText(Signup_Activity.this, "Error" + message, Toast.LENGTH_LONG).show();
                            } else {


                                FirebaseAuth.getInstance().signOut();


                                Toast.makeText(Signup_Activity.this, message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Signup_Activity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            Toast.makeText(Signup_Activity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Error Uploading" + anError, Toast.LENGTH_LONG).show();

                    }
                });

    }


    private String Random() {
        Random rd = new Random();
        String t = "";
        int a = (int) (Math.random() * 3) + 1;
        if (a == 1) {
            String t1 = ("RET" + rd.nextInt(888888888 + 1));
            t = ("WLT" + rd.nextInt(99999999 + 1)) + t1;
        } else if (a == 2) {
            String t1 = ("RET" + rd.nextInt(99999999 + 1));
            t = ("WLT" + rd.nextInt(888888888 + 1)) + t1;
        } else if (a == 3) {
            String t1 = ("RET" + rd.nextInt(888888888 + 1));
            t = ("WLT" + rd.nextInt(99999999 + 1)) + t1;
        }


        return t;
    }


    private void data() {

        final WebView webpay = (WebView) dialogg.findViewById(R.id.webPay);
        final ProgressBar progressBar = dialogg.findViewById(R.id.dialog);
        Button accept = dialogg.findViewById(R.id.accept);
        webpay.loadUrl(paystack_public_key);
        WebSettings webSettings = webpay.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(true);
        webpay.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // progressBar.setVisibility(View.GONE);
                webpay.loadUrl(paystack_public_key);
            }

            public void onPageFinished(WebView view, String url) {
                // progressBar.setVisibility(View.GONE);


            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogg.dismiss();
            }
        });

    }

    public void read(View view) {
        dialogg.show();
    }


    private void saveToken(String token, String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Tokens").
                child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);

        reference.setValue(hashMap);
    }


}


//    private void getAgentList(){
//        final List<String> areas = new ArrayList<String>();
//
//            areas.add(areaName);
//
//
//        Spinner areaSpinner = (Spinner) findViewById(R.id.supervisorPicker);
//        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_spinner_item, areas);
//        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        areaSpinner.setAdapter(areasAdapter);
//    }
//
//}



