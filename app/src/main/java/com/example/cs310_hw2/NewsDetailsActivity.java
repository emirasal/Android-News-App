package com.example.cs310_hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;



public class NewsDetailsActivity extends AppCompatActivity {

    ImageView img;
    TextView title;
    TextView date;
    TextView text;
    ProgressBar prgBar;
    boolean imageDownloaded = false;

    JSONObject data;

    NewsRepository repo = new NewsRepository();

    Handler imageHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            Bitmap imageBitmap = (Bitmap)message.obj;
            img.setImageBitmap(imageBitmap);

            imageDownloaded = true;
            prgBar.setVisibility(View.INVISIBLE);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);


        ExecutorService srv = ((NewsApplication)getApplication()).srv;

        try {

            // First we get the data
            data = new JSONObject(getIntent().getStringExtra("news"));


            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(data.getString("categoryName"));
            actionBar.setDisplayHomeAsUpEnabled(true);



            title = findViewById(R.id.NewsDetailsTitle);
            title.setText(data.getString("title"));

            date = findViewById(R.id.NewsDetailsDate);
            date.setText(data.getString("date"));

            text = findViewById(R.id.NewsDetailsText);
            text.setText(data.getString("text"));


            prgBar = findViewById(R.id.progressBarDetails);

            img = findViewById(R.id.NewsDetailsImage);

            prgBar.setVisibility(View.VISIBLE);
            repo.downloadImage(srv, imageHandler, data.getString("image"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.details_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.showComments){
            try {
                Intent i = new Intent(this, CommentsActivity.class);
                i.putExtra("id", data.getString("id"));
                startActivity(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            // Means back button is clicked
            finish();
        }

        return true;
    }
}