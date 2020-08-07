package com.ici.myproject73029;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Handler handler=new Handler();
        handler.postDelayed(new startLoading(),1000);
        getSupportActionBar().hide();
    }

    public class startLoading implements Runnable{
        @Override
        public void run() {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            loading.this.finish();
        }
    }
}