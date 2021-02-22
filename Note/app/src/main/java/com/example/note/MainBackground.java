package com.example.note;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


public class MainBackground extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_background);

        //Ẩn thanh ActionBar trong android
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Tự động chuyển màn hình
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainBackground.this, MainActivity.class);
                startActivity(intent);
            }
        },2000);
    }

}