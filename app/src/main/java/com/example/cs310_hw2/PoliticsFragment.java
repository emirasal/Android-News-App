package com.example.cs310_hw2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class PoliticsFragment extends Fragment {


    NewsRepository repo = new NewsRepository();

    List<JSONObject> data = new ArrayList<>();

    MainActivity main;
    Context ctx;
    RecyclerView recView;
    ProgressBar prgBar;

    Handler getDataHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            data = (List<JSONObject>) message.obj;

            NewsAdapter adapter = new NewsAdapter(ctx, data);
            recView.setAdapter(adapter);

            // After we are done make progress Bar Invisible
            prgBar.setVisibility(View.INVISIBLE);
            return true;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_politics, container, false);

        main = (MainActivity) getActivity();
        ctx = main;
        ExecutorService srv = ((NewsApplication)main.getApplication()).srv;


        recView = v.findViewById(R.id.recViewPolitics);
        prgBar = v.findViewById(R.id.progressBarPolitics);

        recView.setLayoutManager(new LinearLayoutManager(ctx));

        // Making progress bar visible and getting the data
        prgBar.setVisibility(View.VISIBLE);
        repo.getNewsDataByCategory(srv, getDataHandler, 3);

        return v;
    }
}