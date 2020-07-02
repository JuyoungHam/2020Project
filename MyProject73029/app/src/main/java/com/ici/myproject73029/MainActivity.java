package com.ici.myproject73029;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    ItemFragment itemFragment;
    Tab1Fragment tab1Fragment;
    showFragment showFragment;
    public Firebase firebase;
    public FirebaseFirestore db;

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

        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        firebase = new Firebase();
        db = firebase.startFirebase();
        firebase.getData(db);

        tab1Fragment = new Tab1Fragment();
        showFragment=new showFragment();

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
//                    case R.id.tab2:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
//                                , homeFragment).commit();
//                        return true;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , showFragment).commit();
                        return true;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , homeFragment).commit();
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
}