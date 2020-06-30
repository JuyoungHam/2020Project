package com.ici.myproject73029;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    ItemFragment itemFragment;

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().remove(itemFragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        itemFragment = new ItemFragment();
    }

    public void onItemFragmentChanged(String title) {
        Toast.makeText(this, "전시제목 : " + title, Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, itemFragment).commit();
    }
}