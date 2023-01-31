package com.example.cs310_hw2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    Context ctx;
    List<JSONObject> data = new ArrayList<>();

    NewsAdapter(Context ctx, List<JSONObject> data){
        this.ctx = ctx;
        this.data = data;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(ctx).inflate(R.layout.news_row_layout, parent, false);
        NewsViewHolder vHolder = new NewsViewHolder(row);
        vHolder.setIsRecyclable(false);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        JSONObject current = data.get(position);

        // Setting the text Fields
        try {
            holder.date.setText(current.getString("date"));
            holder.text.setText(current.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        NewsApplication app = (NewsApplication)((AppCompatActivity)ctx).getApplication();
        // Setting and Downloading the Image
        try {
            holder.downloadImage(app.srv, current.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ctx, NewsDetailsActivity.class);
                // The JSON Data to pass it to the Details Activity
                i.putExtra("news", current.toString());

                ((AppCompatActivity)ctx).startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }






    class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView date;
        TextView text;
        ConstraintLayout row;
        boolean imageDownloaded = false;
        ProgressBar prgBar;

        Handler imgHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {

                Bitmap imageBitmap = (Bitmap)message.obj;
                img.setImageBitmap(imageBitmap);

                imageDownloaded = true;
                prgBar.setVisibility(View.INVISIBLE);
                return true;
            }
        });


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            row = itemView.findViewById(R.id.newsRow);
            date = itemView.findViewById(R.id.dateText);
            text = itemView.findViewById(R.id.newsTitleText);
            img = itemView.findViewById(R.id.NewsDetailsImage);
            prgBar = itemView.findViewById(R.id.progressBarImage);
        }

        // Image Download Part
        public void downloadImage(ExecutorService srv, String path){

            if (!imageDownloaded){

                prgBar.setVisibility(View.VISIBLE);

                NewsRepository repo = new NewsRepository();
                repo.downloadImage(srv, imgHandler, path);
            }
        }

    }
}
