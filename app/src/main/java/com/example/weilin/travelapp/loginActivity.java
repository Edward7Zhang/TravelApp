package com.example.weilin.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 某人说我俗 on 2017/1/6.
 */

public class loginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent intent = getIntent();
    }
}
