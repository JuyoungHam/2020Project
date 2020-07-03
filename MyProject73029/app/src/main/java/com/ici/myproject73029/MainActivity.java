package com.ici.myproject73029;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.Show;
import com.ici.myproject73029.tabs.ExhibitionTab;
import com.ici.myproject73029.tabs.GridTab;
import com.ici.myproject73029.tabs.HomeTab;
import com.ici.myproject73029.tabs.ShowTab;

public class MainActivity extends AppCompatActivity {

    HomeTab homeTab;
    ExhibitionTab exhibitionTab;
    ShowTab showTab;
    ItemFragment item_Show;
    ItemFragment item_Exhibition;
    GridTab gridTab;

    @Override
    public void onBackPressed() {
        if (item_Show.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(item_Show).commit();
        } else if (item_Exhibition.isAdded()) {
//            getSupportFragmentManager().beginTransaction().remove(item_Show).commit();
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
//        firebase.addData(db);

        homeTab =
                (HomeTab) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        exhibitionTab = new ExhibitionTab();
        showTab = new ShowTab();
        gridTab = new GridTab();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.tab_home);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , exhibitionTab).commit();
                        return true;
                    case R.id.tab_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , homeTab).commit();
                        return true;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , gridTab).commit();
                        return true;
                }
                return false;
            }
        });


    }

    public void onItemFragmentChanged(Exhibition item) {
        item_Exhibition = new ItemFragment(item);

        Toast.makeText(this, "전시제목 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, item_Exhibition).commit();
    }

    public void onItemFragmentChanged(Show item) {
        item_Show = new ItemFragment(item);

        Toast.makeText(this, "공연제목 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, item_Show).commit();
    }
}