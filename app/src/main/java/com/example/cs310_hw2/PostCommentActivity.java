package com.example.cs310_hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;

public class PostCommentActivity extends AppCompatActivity {

    EditText name;
    EditText comment;
    Button postCommentBtn;

    ProgressBar prgBar;
    TextView uploadingMessage;

    String id;

    NewsRepository repo = new NewsRepository();

    Handler PostCommentHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.obj.toString().equals("1")) {
                prgBar.setVisibility(View.INVISIBLE);
                uploadingMessage.setVisibility(View.INVISIBLE);

                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);

        id = getIntent().getStringExtra("id");

        ExecutorService srv = ((NewsApplication)getApplication()).srv;

        getSupportActionBar().setTitle("Post Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        name = findViewById(R.id.PCName);
        comment = findViewById(R.id.PCComment);
        postCommentBtn = findViewById(R.id.PCButton);

        prgBar = findViewById(R.id.progressBarPostComment);
        uploadingMessage = findViewById(R.id.DialogMessagePostComment);

        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // First We check for empty fields
                if (name.getText().length() == 0 || comment.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
                }else {

                    prgBar.setVisibility(View.VISIBLE);
                    uploadingMessage.setVisibility(View.INVISIBLE);

                    repo.PostComment(srv, PostCommentHandler, name.getText().toString(), comment.getText().toString(), id);
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        finish();
        return true;
    }
}