package com.theway4wardacademy.report.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theway4wardacademy.report.Activity.AddNews;
import com.theway4wardacademy.report.R;


public class HomeFragment extends Fragment {

    FloatingActionButton gotoAdd;
    int layoutId = R.layout.fragment_home;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        
        View root = inflater.inflate(layoutId, container, false);



        gotoAdd = (FloatingActionButton)root.findViewById(R.id.gotoAdd);
        gotoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddNews.class);
                startActivity(intent);
            }
        });

        return root;
    }


}