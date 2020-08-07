package com.ici.myproject73029.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.FirebaseUIActivity;

public class MyPageTab extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private MainActivity mainActivity;
    private TextView text_profile;
    private Button login_control_button;
    private FirebaseUser user;
    private ImageView profile_image;
    private FirebaseAuth auth;
    private SwipeRefreshLayout refreshLayout;
    private Button review_button;
    private Button favorite_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        user = auth.getCurrentUser();
        updateUI(user);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_mypage, container, false);
        mainActivity.isActionBarVisible(false);

        login_control_button = rootView.findViewById(R.id.button_login_control);
        login_control_button.setOnClickListener(this);

        favorite_button = rootView.findViewById(R.id.button_favorite);
        favorite_button.setOnClickListener(this);
        review_button = rootView.findViewById(R.id.button_myreview);
        review_button.setOnClickListener(this);
        text_profile = rootView.findViewById(R.id.text_profile);
        profile_image = rootView.findViewById(R.id.image_profile);
        final ConstraintLayout constraintLayout = rootView.findViewById(R.id.constraintlayout_mypage);
        refreshLayout = rootView.findViewById(R.id.swipeRefreshLayout_mypage);
        refreshLayout.setDistanceToTriggerSync(200);
        refreshLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (constraintLayout.getScrollY() == 0)
                    refreshLayout.setEnabled(true);
                else
                    refreshLayout.setEnabled(false);
            }
        });
        refreshLayout.setOnRefreshListener(this);

        Button theme1 = rootView.findViewById(R.id.theme1);
        theme1.setOnClickListener(this);
        Button theme2 = rootView.findViewById(R.id.theme2);
        theme2.setOnClickListener(this);
        Button theme3 = rootView.findViewById(R.id.theme3);
        theme3.setOnClickListener(this);
        Button theme4 = rootView.findViewById(R.id.theme4);
        theme4.setOnClickListener(this);
        Button theme5 = rootView.findViewById(R.id.theme5);
        theme5.setOnClickListener(this);
        Button theme6 = rootView.findViewById(R.id.theme6);
        theme6.setOnClickListener(this);

        user = auth.getCurrentUser();
        updateUI(user);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_login_control) {
            Intent intent = new Intent(getContext(), FirebaseUIActivity.class);
            startActivityForResult(intent, Constant.LOGIN_REQUEST_FROM_MYPAGE);
        } else if (i == R.id.button_favorite) {
            mainActivity.onMyPageChanged(i);
        } else if (i == R.id.button_myreview) {
            mainActivity.onMyPageChanged(i);
        } else if (i == R.id.theme1) {
            ThemeChanger.themeChange(mainActivity, 1);
        } else if (i == R.id.theme2) {
            ThemeChanger.themeChange(mainActivity, 2);
        } else if (i == R.id.theme3) {
            ThemeChanger.themeChange(mainActivity, 1);
        } else if (i == R.id.theme4) {
            ThemeChanger.themeChange(mainActivity, 1);
        } else if (i == R.id.theme5) {
            ThemeChanger.themeChange(mainActivity, 1);
        } else if (i == R.id.theme6) {
            ThemeChanger.themeChange(mainActivity, 1);
        }
    }

    public void updateUI(final FirebaseUser user) {
        if (user != null) {
            review_button.setVisibility(View.VISIBLE);
            favorite_button.setVisibility(View.VISIBLE);
            login_control_button.setText("계정 관리");
            mainActivity.db.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            text_profile.setText(user.getDisplayName());
                            mainActivity.getUserProfileImage(profile_image);
                            Log.d(Constant.TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Toast.makeText(getContext(), "DB에 사용자 정보가 존재하지 않습니다.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(Constant.TAG, "No such document");
                        }
                    } else {
                        Toast.makeText(getContext(), "DB접속에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.d(Constant.TAG, "get failed with ", task.getException());
                    }
                }
            });
        } else {
            review_button.setVisibility(View.GONE);
            favorite_button.setVisibility(View.GONE);
            login_control_button.setText("로그인 하기");
            text_profile.setText("로그인이 필요합니다.");
            profile_image.setImageResource(R.drawable.ic_baseline_portrait_24);
        }
    }

    @Override
    public void onRefresh() {
        updateUI(user);
        refreshLayout.setRefreshing(false);
    }
}