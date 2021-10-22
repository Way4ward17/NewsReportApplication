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

import com.theway4wardacademy.report.R;


public class HomeFragment extends Fragment {

    int layoutId = R.layout.fragment_home;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        
        View root = inflater.inflate(layoutId, container, false);

        return root;
    }


}