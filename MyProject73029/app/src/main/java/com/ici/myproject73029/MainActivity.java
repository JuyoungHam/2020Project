package com.ici.myproject73029;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    ItemFragment itemFragment;
    Tab1Fragment tab1Fragment;

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

        Firebase firebase = new Firebase();
        FirebaseFirestore db = firebase.startFirebase();


        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        tab1Fragment = new Tab1Fragment();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        if (homeFragment.isAdded()) {
                            getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , tab1Fragment).commit();
                        return true;
                    case R.id.tab_home:
                        if (tab1Fragment.isAdded()) {
                            getSupportFragmentManager().beginTransaction().remove(tab1Fragment).commit();
                        }
                        if (homeFragment.isAdded()) {
                            getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , homeFragment).commit();
                        return true;
//                    case R.id.tab2:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
//                                , homeFragment).commit();
//                        return true;

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