package com.example.cs310_hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CommentsActivity extends AppCompatActivity {

    // id of the current news displayed
    String id;
    List<JSONObject> data = new ArrayList<>();

    NewsRepository repo = new NewsRepository();
    RecyclerView recView;

    ProgressBar prgBar;


    Handler commentsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            data = (List<JSONObject>) message.obj;

            CommentsAdapter adapter = new CommentsAdapter(getApplicationContext(), data);
            recView.setAdapter(adapter);

            prgBar.setVisibility(View.INVISIBLE);
            return true;
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ExecutorService srv = ((NewsApplication)getApplication()).srv;

        prgBar = findViewById(R.id.progressBarComments);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Comments");

        // Getting the ID from the previous Activity
        id = getIntent().getStringExtra("id");


        recView = findViewById(R.id.recViewComments);
        recView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();

        ExecutorService srv = ((NewsApplication)getApplication()).srv;

        prgBar.setVisibility(View.VISIBLE);
        //Getting the comments
        repo.getCommentsWithID(srv, commentsHandler, id);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.comments_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.postCommentButton){

            Intent i = new Intent(this, PostCommentActivity.class);
            i.putExtra("id", id);
            startActivity(i);

        }else{
            // Back Button clicked
            finish();
        }

        return true;
    }
}