package com.example.weilin.travelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CommentActivity extends AppCompatActivity {
    private ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        backImage = (ImageView) findViewById(R.id.back_iv);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
    }
}

