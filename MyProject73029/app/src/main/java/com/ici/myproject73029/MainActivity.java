package com.ici.myproject73029;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.Review;
import com.ici.myproject73029.items.Show;
import com.ici.myproject73029.mypage.FavoritePage;
import com.ici.myproject73029.mypage.MyPageTab;
import com.ici.myproject73029.mypage.MyReviewPage;
import com.ici.myproject73029.mypage.ReviewFragment;
import com.ici.myproject73029.tabs.ExhibitionTab;
import com.ici.myproject73029.tabs.GridTab;
import com.ici.myproject73029.tabs.HomeTab;
import com.ici.myproject73029.tabs.ItemFragment;
import com.ici.myproject73029.tabs.ShowTab;

public class MainActivity extends AppCompatActivity {

    HomeTab homeTab;
    ExhibitionTab exhibitionTab;
    ShowTab showTab;
    ItemFragment item_Show;
    ItemFragment item_Exhibition;
    ReviewFragment item_review;
    GridTab gridTab;
    private MyPageTab myPageTab;
    private BottomNavigationView bottomNavigation;

    private FirebaseAuth mAuth;

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    private FirebaseUser user;


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

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
            Toast.makeText(this, user.getDisplayName() + "님, 환영합니다", Toast.LENGTH_SHORT).show();
        }

        homeTab =
                (HomeTab) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        exhibitionTab = new ExhibitionTab();
        showTab = new ShowTab();
        gridTab = new GridTab();
        myPageTab = new MyPageTab();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.tab_home);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_exhibition:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , exhibitionTab).commit();
                        return true;
                    case R.id.tab_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , homeTab).commit();
                        return true;
                    case R.id.tab_show:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment
                                , gridTab).commit();
                        return true;
                    case R.id.tab_mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                                myPageTab).commit();
                        return true;
                }
                return false;
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent.getStringExtra("purpose") != null) {
            bottomNavigation.setSelectedItemId(R.id.tab_mypage);
            Toast.makeText(this, intent.getStringExtra(Constant.USERNAME), Toast.LENGTH_SHORT).show();
        }
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

    public void onMyPageChanged(int i) {
        if (i == R.id.button_favorite) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                    new FavoritePage()).commit();
        } else if (i == R.id.button_myreview) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                    new MyReviewPage()).commit();
        }
    }

    public void onItemFragmentChanged(Review item) {
        item_review = new ReviewFragment(item);

        Toast.makeText(this, "마이리뷰" + item.getTitle(), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_myreview, item_review).commit();
    }

    public void goToFragment(int i) {
        if (i == R.id.review_to_mypage) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                    myPageTab).commit();
        } else if (i == R.id.item_to_review) {
            getSupportFragmentManager().beginTransaction().remove(item_review).commit();
        }

    }

}