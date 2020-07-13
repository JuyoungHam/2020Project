package com.ici.myproject73029.mypage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.FirebaseUIActivity;

public class MyPageTab extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private TextView text_profile;
    private Button login_control_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        updateUI(mainActivity.getUser());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_mypage, container, false);
        mainActivity.isActionBarVisible(false);

        login_control_button = rootView.findViewById(R.id.button_login_control);
        login_control_button.setOnClickListener(this);

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
        if (i == R.id.button_login_control) {
            Intent intent = new Intent(getContext(), FirebaseUIActivity.class);
            startActivityForResult(intent, Constant.LOGIN_REQUEST_FROM_MYPAGE);
        } else if (i == R.id.button_favorite) {
            mainActivity.onMyPageChanged(i);
        } else if (i == R.id.button_myreview) {
            mainActivity.onMyPageChanged(i);
        }
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            text_profile.setText(user.getDisplayName());
        } else {
            text_profile.setText(null);
        }
    }

}