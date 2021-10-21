package com.theway4wardacademy.report.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.theway4wardacademy.report.R;

public class ActivityLogin extends AppCompatActivity {
    private TextView signupButton;
    private Button Login;
    private TextView gotosignup;
    private EditText emailPassword1, emailLogin1;
    FirebaseAuth auth;
    private static final String TAG = "ActivityLogin";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        auth = FirebaseAuth.getInstance();
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

                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){



                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        assert firebaseUser != null;
                                        final String userid = firebaseUser.getUid();




                                    } else if(!task.isSuccessful()) {

                                        try {
                                            throw task.getException();
                                        } catch(FirebaseAuthWeakPasswordException e1) {
                                            Log.e(TAG, e1.getMessage());
                                        } catch(FirebaseAuthInvalidCredentialsException e2) {
                                            Log.e(TAG, e2.getMessage());
                                        } catch(FirebaseAuthUserCollisionException e3) {
                                            Log.e(TAG, e3.getMessage());
                                        } catch(Exception e) {
                                            Log.e(TAG, e.getMessage());
                                            View parentLayout = findViewById(android.R.id.content);
                                            Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        }

                                        View parentLayout = findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout, "Network Error/Your login details no correct", Snackbar.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });

    }

    public void openDia(View view) {
    }

/**
    void getData(String userid){
        StringRequest reques = new StringRequest(Request.Method.POST,
                Constant.URL_GETPROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject product1 = new JSONObject(s);
                            String success = product1.getString("success");
                            JSONArray array = product1.getJSONArray("read");

                            if(success.equals("1")) {
                                for (int i = 0; i < array.length(); i ++) {
                                    JSONObject product = array.getJSONObject(i);

                                    SharedPrefManager.getInstance(ActivityLogin.this).saveFaculty(product.getString("faculty"));
                                    SharedPrefManager.getInstance(ActivityLogin.this).saveFullname(product.getString("fullname"));
                                    SharedPrefManager.getInstance(ActivityLogin.this).saveDepartment(product.getString("department"));
                                    SharedPrefManager.getInstance(ActivityLogin.this).saveUsername(product.getString("username"));
                                    SharedPrefManager.getInstance(ActivityLogin.this).saveEmail(product.getString("email"));
                                    SharedPrefManager.getInstance(ActivityLogin.this).saveSession(product.getString("session"));
                                    SharedPrefManager.getInstance(ActivityLogin.this).saveMatric(product.getString("matricnumber"));
                                    SharedPrefManager.getInstance(ActivityLogin.this).savePhoneNumber(product.getString("phone"));
                                    SharedPrefManager.getInstance(ActivityLogin.this).saveVerified(product.getString("verified"));
                                    if(product.getString("bio") == null){
                                        SharedPrefManager.getInstance(ActivityLogin.this).saveBio(product.getString(""));
                                    }else {
                                        SharedPrefManager.getInstance(ActivityLogin.this).saveBio(product.getString("bio"));
                                    }
                                    SharedPrefManager.getInstance(ActivityLogin.this).saveImageUrl(product.getString("image"));
                                    FirebaseAuth.getInstance().signOut();
                                    masterClass.hideLoading();
                                    masterClass.setUpProfile();
                                    Intent intent = new Intent(ActivityLogin.this, Mainbottom.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }



                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("userid", userid);
                return hashMap;
            }
        };

        MySingleton.getInstance(ActivityLogin.this).addToRequestQueue(reques);



    }



    public void openDia(View view){
        Dialog dialogg;
        dialogg = new Dialog(ActivityLogin.this);
        dialogg.setContentView(R.layout.item_password_dialog);
        dialogg.getWindow().getAttributes().width = LinearLayout.LayoutParams.MATCH_PARENT;
        dialogg.getWindow().getAttributes().height = LinearLayout.LayoutParams.MATCH_PARENT;
        dialogg.setCanceledOnTouchOutside(false);
        EditText editText = dialogg.findViewById(R.id.email);
        ProgressBar progressBar = dialogg.findViewById(R.id.progress);
        TextView reset = dialogg.findViewById(R.id.reset);
        ImageView close = dialogg.findViewById(R.id.close);
        View parentLayout = dialogg.findViewById(android.R.id.content);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(editText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Snackbar.make(parentLayout, "Reset link sent to " + editText.getText().toString(), Snackbar.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }else {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(parentLayout, "Empty Email", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogg.dismiss();
            }
        });

        dialogg.show();
    }

**/


}
