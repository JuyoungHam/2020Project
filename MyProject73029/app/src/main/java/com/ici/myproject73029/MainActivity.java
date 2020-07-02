package com.ici.myproject73029;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    ItemFragment itemFragment;
    Tab1Fragment tab1Fragment;
    ShowFragment showFragment;
    ShowFragment2 showFragment2;

    @Override
    public void onBackPressed() {
        if (itemFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(itemFragment).commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment =
                (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        tab1Fragment = new Tab1Fragment();
        showFragment = new ShowFragment();
        showFragment2 = new ShowFragment2();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , tab1Fragment).commit();
                        return true;
                    case R.id.tab_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , homeFragment).commit();
                        return true;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , showFragment2).commit();
                        return true;
                }
                return false;
            }
        });


    }

    public void onItemFragmentChanged(Exhibitions item) {
        itemFragment = new ItemFragment(item);

        Toast.makeText(this, "전시제목 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, itemFragment).commit();
    }

    public void onItemFragmentChanged(Show item) {
        itemFragment = new ItemFragment(item);

        Toast.makeText(this, "공연제목 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, itemFragment).commit();
    }
}