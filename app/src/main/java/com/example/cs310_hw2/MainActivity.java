package com.example.cs310_hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {


    ViewPager2 viewPager2;
    TabLayout tabLayout;
    VPAdapter VPAdapter;

    NewsRepository repo = new NewsRepository();

    private List<String> titles = new ArrayList<>();

    Handler setTitles = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            titles = (List<String>) message.obj;

            new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> tab.setText(titles.get(position)))).attach();
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExecutorService srv = ((NewsApplication)getApplication()).srv;

        setContentView(R.layout.activity_main);

        // Setting up tabs and their titles
        viewPager2 = findViewById(R.id.ViewPager2);
        tabLayout = findViewById(R.id.tabLayout);
        VPAdapter = new VPAdapter(this);

        viewPager2.setAdapter(VPAdapter);

        // Make loading
        repo.getNewsCategories(srv, setTitles);
        //


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("CS310 News");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.news);






    }
}