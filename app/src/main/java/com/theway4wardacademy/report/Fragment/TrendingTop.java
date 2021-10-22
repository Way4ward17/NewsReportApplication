package com.theway4wardacademy.report.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theway4wardacademy.lic.Activity.TrendingFragment;
import com.theway4wardacademy.lic.Adapter.SearchTagAdapterTrend;
import com.theway4wardacademy.lic.Constant;
import com.theway4wardacademy.lic.Data_Pojo.Tag;
import com.theway4wardacademy.lic.R;
import com.theway4wardacademy.lic.Utils.MasterClass;
import com.theway4wardacademy.lic.Utils.RequestHandler;
import com.theway4wardacademy.lic.Utils.SQLiteManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class TrendingTop extends Fragment {

    TextView search;
    private int pageNumberTag = 1;
    RecyclerView tagrecyclerView;
    SearchTagAdapterTrend searchTagAdapter;
    SQLiteManager sqLiteManager;
    StringRequest stringRequest;
    MasterClass masterClass;
    ArrayList<Tag> searchTagModelList;
    CircleImageView profile,settings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_trending_top, container, false);
        profile = root.findViewById(R.id.profile);
        settings = root.findViewById(R.id.settings);
        tagrecyclerView = root.findViewById(R.id.recycler_view);
        sqLiteManager = new SQLiteManager(getContext());
        masterClass = new MasterClass(getContext());

        search = root.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TrendingFragment.class);
                startActivity(intent);
            }
        });
        MasterClass masterClass = new MasterClass(getContext());
        masterClass.showProfileImage(profile);
        searchTagModelList = new ArrayList<>();
        searchTagModelList = sqLiteManager.getTrending();
        tagrecyclerView.setHasFixedSize(true);
        tagrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchTagAdapter = new SearchTagAdapterTrend(getContext(), searchTagModelList);
        tagrecyclerView.setAdapter(searchTagAdapter);

        searchTag();
        return root;

    }



    public void searchTag(){


         stringRequest = new StringRequest(Request.Method.POST,
                Constant.GETTRENDINGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Tag>>() {
                            }.getType();
                            sqLiteManager.deleteOldTrending();
                            searchTagModelList = gson.fromJson(response, type);
                            for (int i = 0; i < searchTagModelList.size() ; i++) {
                                sqLiteManager.addTrending(searchTagModelList.get(i));
                            }
                            showData();


                        } catch (Exception e) {
                            Log.d("Error Caught", "onResponse: response = " + response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError erro) {
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        Log.d("Error Response", "onErrorResponse: Volley Error = " + erro);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("school", String.valueOf(pageNumberTag));
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }



    private void showData() {

        searchTagAdapter.setFilter(searchTagModelList);
        masterClass.new getUnseen().execute();
    }

}