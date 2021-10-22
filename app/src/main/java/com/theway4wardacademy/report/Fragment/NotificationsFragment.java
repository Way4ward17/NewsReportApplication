package com.theway4wardacademy.report.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.badoo.mobile.util.WeakHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theway4wardacademy.lic.Activity.EditProfile;
import com.theway4wardacademy.lic.Adapter.HomeAdapter;
import com.theway4wardacademy.lic.Chat.Chat;
import com.theway4wardacademy.lic.Chat.SharedPrefManager;
import com.theway4wardacademy.lic.Constant;
import com.theway4wardacademy.lic.Data_Pojo.Post;
import com.theway4wardacademy.lic.R;
import com.theway4wardacademy.lic.Settings.MainActivity;
import com.theway4wardacademy.lic.Utils.SQLiteManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationsFragment extends Fragment {


    ImageView chat;
    DatabaseReference reference;
    CircleImageView profile;
    final WeakHandler mHandler = new WeakHandler();
    StringRequest stringRequestFirst;
    SwipeRefreshLayout swipeRefreshLayout;
    private int pageNumber = 2, pageNumberFirst = 1;
    private boolean hasMore = true;
    HomeAdapter postViewHolderText;
    List<Post> imagePost, imagePostFirst;
    int a = 0;
    RecyclerView recyclerView;
    SQLiteManager sqLiteManager;
    GridLayoutManager linearLayoutManager;
    RequestQueue requestQueue;

    private int visibleitemcount, totalitemcount, firstvisibleitems, previouscount = 0;


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        sqLiteManager = new SQLiteManager(getContext());


        chat = (ImageView) root.findViewById(R.id.chat);




        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));

            }
        });


        new getUnseen().execute();

        new setPage().execute();
        imagePostFirst = new ArrayList<>();

        recyclerView = root.findViewById(R.id.recycler_view);
        linearLayoutManager = new GridLayoutManager(getContext(), 1);



        start();


        swipeRefreshLayout = root.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                imagePost.clear();
                imagePostFirst.clear();
                swipeRefreshLayout.setRefreshing(true);
                start();
            }
        });
        return root;
    }

    private void start(){
        new getList().execute(pageNumber);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                new getListFirst().execute(pageNumberFirst);
            }
        };

        mHandler.postDelayed(runnable,2000);
    }

    class setPage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            imagePost = new ArrayList<>();
        //    imagePost.add(new Post("profile",SharedPrefManager.getInstance(getContext()).getUsername()));

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imagePost = sqLiteManager.getUserpost();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setUpRecyclerView();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onStart() {
        super.onStart();



    }










    private void showData() {
        postViewHolderText.setFilter3(imagePost);


    }
  public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    private void setUpRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postViewHolderText = new HomeAdapter(getContext(), imagePost);
        recyclerView.setAdapter(postViewHolderText);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                visibleitemcount = linearLayoutManager.getChildCount();
                totalitemcount = linearLayoutManager.getItemCount();
                firstvisibleitems = linearLayoutManager.findFirstVisibleItemPosition();


                if(hasMore){
                    if(totalitemcount > previouscount){
                        previouscount = totalitemcount;
                        pageNumber++;
                        hasMore = false;
                    }
                }

                if(!hasMore && (firstvisibleitems + visibleitemcount) >= totalitemcount){
                    new getList().execute(pageNumber);
                    hasMore = true;

                }
            }
        });

    }


    class getListFirst extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {


            stringRequestFirst  = new StringRequest(Request.Method.POST,
                    Constant.GETUSERPOST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d(" Response", ""+integers[0]+"onResponse: Volley data Post PROFILE = "+response );


                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<Post>>() {

                                }.getType();



                                sqLiteManager.deleteOldUserpost();
                                imagePostFirst.add(new Post("profile",SharedPrefManager.getInstance(getContext()).getUsername(),SharedPrefManager.getInstance(getContext()).getID(),SharedPrefManager.getInstance(getContext()).getVerified()));
                                sqLiteManager.addUserpost(imagePostFirst.get(0));
                                imagePostFirst = gson.fromJson(response, type);
                                for (int a = 0; a < imagePostFirst.size(); a++) {
                                sqLiteManager.addUserpost(imagePostFirst.get(a));
                            }



                            } catch (Exception e) {
                                Log.d("Error Caught", "onResponse: response = " + response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            stringRequestFirst.setRetryPolicy(new DefaultRetryPolicy(5000,3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            Log.d("Error Response", "onErrorResponse: Volley Error = " + error);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("page", String.valueOf(integers[0]));
                    params.put("userid", SharedPrefManager.getInstance(getContext()).getID());
                    return params;
                }
            };
            requestQueue.add(stringRequestFirst);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(aVoid);
        }
    }




    class getList extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {


            final Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    new getList().execute(integers[0]);
                }
            };

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.GETUSERPOST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d(" Response", ""+integers[0]+"onResponse: Volley data Post = "+response );


                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<Post>>() {

                                }.getType();

                                    imagePost = gson.fromJson(response, type);

                                showData();


                            } catch (Exception e) {
                                Log.d("Error Caught", "onResponse: response = " + response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //    if (progressDialog.isShowing()) progressDialog.dismiss();
                            mHandler.postDelayed(runnable,5000);
                            Log.d("Error Response", "onErrorResponse: Volley Error = " + error);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("page", String.valueOf(integers[0]));
                    params.put("userid", SharedPrefManager.getInstance(getContext()).getID());
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    @Override
    public void onResume() {

        super.onResume();
    }

    public void editProfile(){
        Intent intent = new Intent(getContext(), EditProfile.class);
        startActivity(intent);

    }

    class getUnseen extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

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

            super.onPostExecute(s);
        }
    }

}
