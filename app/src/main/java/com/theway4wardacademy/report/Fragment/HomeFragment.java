package com.theway4wardacademy.report.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.badoo.mobile.util.WeakHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theway4wardacademy.lic.Activity.Notifications;
import com.theway4wardacademy.lic.Activity.What_Activity;
import com.theway4wardacademy.lic.Adapter.MainAdapter;
import com.theway4wardacademy.lic.Adapter.PeopleMayKnowAdapter;
import com.theway4wardacademy.lic.Chat.Chat;
import com.theway4wardacademy.lic.Chat.MainActivity;
import com.theway4wardacademy.lic.Chat.SharedPrefManager;
import com.theway4wardacademy.lic.Constant;
import com.theway4wardacademy.lic.Data_Pojo.Post;
import com.theway4wardacademy.lic.Data_Pojo.PostIncase;
import com.theway4wardacademy.lic.Data_Pojo.PostSponsored;
import com.theway4wardacademy.lic.Data_Pojo.PostSuggestion;
import com.theway4wardacademy.lic.Data_Pojo.User;
import com.theway4wardacademy.lic.R;
import com.theway4wardacademy.lic.Utils.MasterClass;
import com.theway4wardacademy.lic.Utils.RequestHandler;
import com.theway4wardacademy.lic.Utils.SQLiteManager;
import com.theway4wardacademy.lic.story.StoryUpload;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {


    //New Methods
    final WeakHandler mHandler = new WeakHandler();
    private ArrayList<Object> objects = new ArrayList<>();
    private int pageNumber = 1;
    PeopleMayKnowAdapter peopleMayKnowAdapter;
    DatabaseReference reference;
    ProgressBar progressBar;
    static List<User> userList, userListCollector;
    ImageView profileImage, chat, settings, searchImage;
    FloatingActionButton back;
    MainAdapter adapter;
    private RecyclerView mRecyclerView;
    MasterClass masterClass;
    private static List<Post> postModel, postModelCollector,postModel1, postModelCollector1;
    private static List<PostIncase> postIncase, postIncaseCollector;
    private static List<PostSuggestion> suggestionModel, suggestCollector;
    private static List<PostSponsored> sponsordModel, sponsordModelCollector;
    SwipeRefreshLayout swipeRefreshLayout;
    int layoutId = R.layout.fragment_home;
    private static SQLiteManager sqLiteManager;
    RequestQueue requestQueue;
    TextView search;
    RelativeLayout hider;
    int a = 0;
    private RecyclerView recyclerView_story;
    CircleImageView story_photo;
    RelativeLayout story_photoSwitch;
    LinearLayoutManager linearLayoutManager;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        
        View root = inflater.inflate(layoutId, container, false);
        masterClass = new MasterClass(getContext());
        new initArrayList().execute(root);
        story_photoSwitch = root.findViewById(R.id.story_photo);
        story_photo = root.findViewById(R.id.story_photoSwitch);

        story_photoSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() ,  StoryUpload.class);
                startActivity(intent);

            }
        });

        progressBar = (ProgressBar) root.findViewById(R.id.progress_indicator);
        swipeRefreshLayout = root.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(true);
                objects.clear();
                start();
            }
        });
        textView = root.findViewById(R.id.unseen);
        settings = root.findViewById(R.id.settings);
        requestQueue = Volley.newRequestQueue(getActivity());
        recyclerView_story = root.findViewById(R.id.storyrecycler);
        hider = root.findViewById(R.id.hider);
        searchImage = root.findViewById(R.id.upsearch);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() ,  Notifications.class);
                startActivity(intent);
            }
        });
        mRecyclerView = root.findViewById(R.id.blog_list_view);
        linearLayoutManager = new LinearLayoutManager(getContext());

        sqLiteManager = new SQLiteManager(getContext());

        chat = (ImageView) root.findViewById(R.id.chat);
        back = (FloatingActionButton) root.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() ,  What_Activity.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        masterClass.showProfileImage(story_photo);


        start();

        return root;
    }


    private void start(){


        new getData().execute();
        new getPromotedPost().execute();
        searchImage.setVisibility(View.VISIBLE);
         new getfetchData().execute();



        textView.setText(SharedPrefManager.getInstance(getContext()).getUnseen());
    }


    class initArrayList extends AsyncTask<View, Void, Void> {

        @Override
        protected Void doInBackground(View... voids) {

            return null;
        }
    }


    private List<Object> getObject() {
        return objects;
    }

    public static List<PostIncase> getIncaseData() {
        return postIncaseCollector;
    }
    public static List<Post> getTopData() {
        return postModelCollector;
    }
    public static List<User> getMayKnowData() {
        return userListCollector;
    }
    public static List<PostSuggestion> getSuggestionData() {

        return suggestCollector;
    }
    public static List<PostSponsored> getSponsoredData() {
        return sponsordModelCollector;
    }
    private void postRecycler(){


    }


    class getData1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String url = Constant.GETPOSTT + SharedPrefManager.getInstance(getContext()).getID();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("ResponseTop 15 to 15", response);
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Post>>() {

                        }.getType();
                        postModel1 = gson.fromJson(response, type);
                        sqLiteManager.deleteOldCache2();

                        for (int a = 0; a < postModel1.size(); a++) {
                            sqLiteManager.addData2(postModel1.get(a));
                        }

                    } catch (Exception e) {


                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ErrorTop", "No network connection");
                }
            });

            requestQueue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getPeopleMayKnow();
            super.onPostExecute(aVoid);
        }
    }


     class getData extends AsyncTask<Void, Void, Void> {


         @Override
         protected Void doInBackground(Void... voids) {

             String url = Constant.GETPOST + SharedPrefManager.getInstance(getContext()).getID();

             StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {

                     Log.d("ResponseTop 0 t0 15", response);
                     try {
                         Gson gson = new Gson();
                         Type type = new TypeToken<ArrayList<Post>>() {

                         }.getType();
                         postModel = gson.fromJson(response, type);
                         sqLiteManager.deleteOldCache();



                         for (int a = 0; a < postModel.size(); a++) {
                             sqLiteManager.addData(postModel.get(a));

                         }



                     } catch (Exception e) {


                     }
                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     Log.d("ErrorTop", "No network connection");
                 }
             });

             requestQueue.add(stringRequest);
             return null;
         }

         @Override
         protected void onPostExecute(Void aVoid) {
             new getSuggestionPost().execute();

             super.onPostExecute(aVoid);
         }
     }

    class getSuggestionPost extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.GETSUGGESTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("ResponseSuggestion",response);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<PostSuggestion>>() {
                                }.getType();
                                suggestionModel = gson.fromJson(response, type);
                                sqLiteManager.deleteOldSuggestion();

                                for (int a = 0; a < suggestionModel.size(); a++) {
                                    if(!suggestionModel.get(a).getUserid().equals(SharedPrefManager.getInstance(getContext()).getID()))
                                        sqLiteManager.addSuggestion(suggestionModel.get(a));

                                }




                            } catch (Exception e) {
                                Log.d("Error Caught", "onResponse: response = " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError erro) {
                            Log.d("Error Response", "onErrorResponse: Volley Error = " + erro);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", SharedPrefManager.getInstance(getContext()).getID());

                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            new getSponsordPost().execute();
            super.onPostExecute(aVoid);
        }
    }



    class getfetchData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
          //  progressBar.setVisibility(View.VISIBLE);
            postModelCollector = new ArrayList<>();
            postModelCollector1 = new ArrayList<>();
            postModel = new ArrayList<>();
            postModel1 = new ArrayList<>();
            suggestCollector = new ArrayList<>();

            suggestionModel = new ArrayList<>();
            userListCollector = new ArrayList<>();
            userList = new ArrayList<>();
            sponsordModel = new ArrayList<>();
            sponsordModelCollector = new ArrayList<>();

            postIncase = new ArrayList<>();
            postIncaseCollector = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {



            userListCollector = sqLiteManager.getPeopleMayKnow();
            postModelCollector = sqLiteManager.getData();
            suggestCollector = sqLiteManager.getSuggestion();
            sponsordModelCollector = sqLiteManager.getSponsor();
            postIncaseCollector = sqLiteManager.getTele();
            //postModelCollector1 = masterClass.sort(sqLiteManager.getData2());




            if(!postModelCollector.isEmpty()){
                objects.add(getTopData().get(0));
                Log.d("added","top");
            }

            if(!suggestCollector.isEmpty()) {
                objects.add(getSuggestionData().get(0));
                Log.d("added","suggest");
            }
            if(!sponsordModelCollector.isEmpty()){
                objects.add(getSponsoredData().get(0));
            }
            if(!userListCollector.isEmpty()){
                objects.add(getMayKnowData().get(0));
                Log.d("added","mayknow");
            }

            if(!postIncaseCollector.isEmpty()){
                objects.add(getIncaseData().get(0));
                Log.d("added","incase");
            }
            if(!sponsordModelCollector.isEmpty()){
                objects.add(getSponsoredData().get(0));
                Log.d("added","sponsored");
            }
            if(postModelCollector1.isEmpty()){
                objects.add(postModel1);
                Log.d("added","top");
            }
            if(!sponsordModelCollector.isEmpty()){
                objects.add(getSponsoredData().get(0));
            }






            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

          //  progressBar.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            adapter = new MainAdapter(getContext(), getObject());
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(aVoid);
        }
    }



    private void getPeopleMayKnow() {

        Log.d("Pge", "Page = " + pageNumber);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.GETMAYKNOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("ResponseMayKnow",   response);
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<User>>() {

                            }.getType();
                            userList = gson.fromJson(response, type);
                            sqLiteManager.deleteOldMayKnow();
                            for (int a = 0; a < userList.size(); a++) {
                                if(!userList.get(a).getUserid().equals(SharedPrefManager.getInstance(getContext()).getID()))
                                    sqLiteManager.addMayKnow(userList.get(a));
                            }

                        } catch (Exception e) {
                            Log.d("Error Caught", "onResponse: response = " +e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error Response", "onErrorResponse: Volley Error = " + error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("page", String.valueOf(pageNumber));
                params.put("faculty", SharedPrefManager.getInstance(getContext()).getFaculty());
                params.put("department", SharedPrefManager.getInstance(getContext()).getDepartment());
                params.put("userid", SharedPrefManager.getInstance(getContext()).getID());

                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    class getSponsordPost extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.GETSPONSOR,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Sponsord",   response);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<PostSponsored>>() {

                                }.getType();
                                sponsordModel = gson.fromJson(response, type);

                                sqLiteManager.deleteOldSponsor();
                                for (int a = 0; a < sponsordModel.size(); a++) {
                                    sqLiteManager.addSponsor(sponsordModel.get(a));
                                }

                            } catch (Exception e) {
                                Log.d("Error Caught", "onResponse: response = " + e.getMessage());
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error Response", "onErrorResponse: Volley Error = " + error);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("school", SharedPrefManager.getInstance(getContext()).getID());
                    params.put("department", SharedPrefManager.getInstance(getContext()).getDepartment());
                    params.put("faculty", SharedPrefManager.getInstance(getContext()).getFaculty());
                    params.put("session", SharedPrefManager.getInstance(getContext()).getSession());
                    params.put("state", SharedPrefManager.getInstance(getContext()).getID());
                    params.put("userid", SharedPrefManager.getInstance(getContext()).getID());
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new getIncasePost().execute();

            super.onPostExecute(aVoid);
        }
    }



    class getPromotedPost extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.GETSPONSOR,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Sponsord nEW",   response);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<Post>>() {

                                }.getType();

                                postModel = gson.fromJson(response, type);


                                for (int a = 0; a < postModel.size(); a++) {
                                    sqLiteManager.addData(postModel.get(a));
                                }

                            } catch (Exception e) {
                                Log.d("Error Caught", "onResponse: response = " + e.getMessage());
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error Response", "onErrorResponse: Volley Error = " + error);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("school", SharedPrefManager.getInstance(getContext()).getID());
                    params.put("department", SharedPrefManager.getInstance(getContext()).getDepartment());
                    params.put("faculty", SharedPrefManager.getInstance(getContext()).getFaculty());
                    params.put("session", SharedPrefManager.getInstance(getContext()).getSession());
                    params.put("state", SharedPrefManager.getInstance(getContext()).getID());
                    params.put("userid", SharedPrefManager.getInstance(getContext()).getID());
                    return params;

                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new getIncasePost().execute();

            super.onPostExecute(aVoid);
        }
    }

    class getIncasePost extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.INCASE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("Incase",   response);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<PostIncase>>() {

                                }.getType();
                                postIncase = gson.fromJson(response, type);

                                sqLiteManager.deleteOldTele();
                                for (int a = 0; a < postIncase.size(); a++) {
                                    if(!postIncase.get(a).getUserid().equals(SharedPrefManager.getInstance(getContext()).getID()))
                                        sqLiteManager.addTv(postIncase.get(a));

                                }

                                SharedPrefManager.getInstance(getContext()).saveTime(new Date().getTime());
                            } catch (Exception e) {
                                Log.d("Error Caught", "onResponse: response = " + e.getMessage());
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error Response", "onErrorResponse: Volley Error = " + error);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid",SharedPrefManager.getInstance(getContext()).getID());
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new getData1().execute();
          //  masterClass.showProfileImage(story_photo);
            super.onPostExecute(aVoid);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        new getUnseen().execute();
        super.onResume();
    }


    class getUnseen extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            textView.setText(SharedPrefManager.getInstance(getContext()).getUnseen());
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            reference = FirebaseDatabase.getInstance().getReference("Chats");
            reference.keepSynced(true);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    int unread = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(SharedPrefManager.getInstance(getContext()).getID()) && !chat.isIsseen()) {
                            unread++;
                        }
                    }

                    SharedPrefManager.getInstance(getContext()).saveSeen(""+unread);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            textView.setText(SharedPrefManager.getInstance(getContext()).getUnseen());
            super.onPostExecute(s);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}