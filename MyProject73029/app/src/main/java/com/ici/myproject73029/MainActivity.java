package com.ici.myproject73029;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.FundamentalItem;
import com.ici.myproject73029.items.Review;
import com.ici.myproject73029.mypage.FavoritePage;
import com.ici.myproject73029.mypage.MyPageTab;
import com.ici.myproject73029.mypage.MyReviewPage;
import com.ici.myproject73029.mypage.MyReviewFragment;
import com.ici.myproject73029.tabs.ExhibitionTab;
import com.ici.myproject73029.tabs.GridTab;
import com.ici.myproject73029.tabs.HomeTab;
import com.ici.myproject73029.tabs.ItemFragment;
import com.ici.myproject73029.tabs.ReviewListFragment;
import com.ici.myproject73029.tabs.ShowTab;

public class MainActivity extends AppCompatActivity {

    public HomeTab homeTab;
    public ExhibitionTab exhibitionTab;
    public ShowTab showTab;
    public MyReviewFragment item_review;
    public MyPageTab myPageTab;
    public FavoritePage favoritePage;
    public MyReviewPage myReviewPage;
    ItemFragment item_Show;
    ItemFragment item_Exhibition;
    GridTab gridTab;
    private BottomNavigationView bottomNavigation;
    private ActionBar actionBar;
    private long pressedTime = 0;
    public FirebaseAuth mAuth;
    private FirebaseUser user;
    private onBackPressedListener onBackPressedListener;
    public FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri user_image_uri;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference().child("profile_images");
        }

        homeTab =
                (HomeTab) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        exhibitionTab = new ExhibitionTab();
        showTab = new ShowTab();
        gridTab = new GridTab();
        myPageTab = new MyPageTab();

        favoritePage = new FavoritePage();
        myReviewPage = new MyReviewPage();

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

        actionBar = getSupportActionBar();
        isActionBarVisible(false);

    }


    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        Intent intent = getIntent();
        if (intent.getStringExtra("purpose") != null) {
            bottomNavigation.setSelectedItemId(R.id.tab_mypage);
        }
    }

    public void setOnBackPressedListener(onBackPressedListener listener) {
        onBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        // 다른 Fragment 에서 리스너를 설정했을 때 처리됩니다.
        if (onBackPressedListener != null) {
            onBackPressedListener.onBack();
            Log.e("!!!", "Listener is not null");
        } else {
            Log.e("!!!", "Listener is null");
            if (pressedTime == 0) {
                Toast.makeText(this,
                        " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                pressedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);
                if (seconds > 2000) {
                    Toast.makeText(this,
                            " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                    pressedTime = 0;
                } else {
                    super.onBackPressed();
                    Log.e("!!!", "onBackPressed : finish, killProcess");
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        }
    }

    public void onItemFragmentChanged(FundamentalItem item) {
        if (item.getType() == Constant.EXHIBITION) {
            item_Exhibition = new ItemFragment(item);

            Toast.makeText(this, "전시제목 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, item_Exhibition).commit();
        } else if (item.getType() == Constant.SHOW) {
            item_Show = new ItemFragment(item);

            Toast.makeText(this, "공연제목 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, item_Show).commit();
        } else if (item.getType() == Constant.REVIEW) {
            item_review = new MyReviewFragment((Review) item);
            Toast.makeText(this, "마이리뷰 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, item_review).commit();
        } else if (item.getType() == Constant.FAVORITE) {
            item_Exhibition = new ItemFragment(item);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, item_Exhibition).commit();
        }

    }

    public void onMyPageChanged(int i) {
        if (i == R.id.button_favorite) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                    favoritePage).commit();
        } else if (i == R.id.button_myreview) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                    myReviewPage).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    public void isActionBarVisible(Boolean b) {
        actionBar.setShowHideAnimationEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (b == true) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    public void setActionBarTitle(String title) {
        actionBar.setTitle(title);
    }

    public void setActionBarOption(int option) {
        actionBar.setDisplayOptions(option);
    }

    public interface onBackPressedListener {
        void onBack();
    }

    public void getUserProfileImage(final ImageView imageView) {
        if (user != null) {
            storageRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } else {
            imageView.setImageBitmap(null);
        }
    }
}