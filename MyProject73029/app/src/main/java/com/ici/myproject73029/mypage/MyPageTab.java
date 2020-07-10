package com.ici.myproject73029.mypage;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.FirebaseUIActivity;

public class MyPageTab extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private TextView text_profile;
    private Button login_button;
    private Button logout_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (requestCode == Constant.LOGIN_REQUEST_FROM_MYPAGE) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_mypage, container, false);

        login_button = rootView.findViewById(R.id.button_login);
        login_button.setOnClickListener(this);
        logout_button = rootView.findViewById(R.id.button_logout);
        logout_button.setOnClickListener(this);

        Button favorite_button = rootView.findViewById(R.id.button_favorite);
        favorite_button.setOnClickListener(this);
        Button review_button = rootView.findViewById(R.id.button_myreview);
        review_button.setOnClickListener(this);
        text_profile = rootView.findViewById(R.id.text_profile);

        FirebaseUser user = mainActivity.getUser();
        updateUI(user);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_login) {
            Intent intent = new Intent(getContext(), FirebaseUIActivity.class);
            startActivityForResult(intent, Constant.LOGIN_REQUEST_FROM_MYPAGE);
        } else if (i == R.id.button_favorite) {
            mainActivity.onMyPageChanged(i);
        } else if (i == R.id.button_myreview) {
            mainActivity.onMyPageChanged(i);
        } else if(i == R.id.button_logout){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Toast.makeText(mainActivity, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void updateUI(FirebaseUser user){
        if (user != null) {
            login_button.setVisibility(View.GONE);
            logout_button.setVisibility(View.VISIBLE);
            text_profile.setText(user.getDisplayName());
        } else {
            login_button.setVisibility(View.VISIBLE);
            logout_button.setVisibility(View.GONE);
            text_profile.setText(null);
        }
    }

}