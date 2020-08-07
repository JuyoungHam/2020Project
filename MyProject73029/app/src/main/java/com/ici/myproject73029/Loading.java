package com.ici.myproject73029;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ici.myproject73029.mypage.ThemeChanger;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChanger.createPreference(getApplicationContext());
        ThemeChanger.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Handler handler = new Handler();
        handler.postDelayed(new startLoading(), 1000);
        getSupportActionBar().hide();
    }

    public class startLoading implements Runnable{
        @Override
        public void run() {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            Loading.this.finish();
        }
    }
}