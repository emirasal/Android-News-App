package com.example.cs310_hw2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    Context ctx;
    List<JSONObject> data = new ArrayList<>();

    public CommentsAdapter(Context ctx, List<JSONObject> data) {
        this.ctx = ctx;
        this.data = data;
    }



    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(ctx).inflate(R.layout.comments_row_layout, parent, false);
        CommentsViewHolder vHolder = new CommentsViewHolder(row);

       // vHolder.setIsRecyclable(false);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {


    JSONObject current = data.get(position);


        try {
            holder.name.setText(current.getString("name"));
            holder.comment.setText(current.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {return data.size();}







    class CommentsViewHolder extends RecyclerView.ViewHolder{


        ImageView img;
        TextView name;
        TextView comment;
        ConstraintLayout row;


        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.commenterImage);
            img.setImageResource(R.drawable.user);

            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
