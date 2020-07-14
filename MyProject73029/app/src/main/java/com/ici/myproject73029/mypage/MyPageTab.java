package com.ici.myproject73029.mypage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.firebase.FirebaseUIActivity;

import java.util.Map;

public class MyPageTab extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private TextView text_profile;
    private Button login_control_button;
    private FirebaseUser user;
    private ImageView profile_image;
    private FirebaseAuth auth;

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

        Button favorite_button = rootView.findViewById(R.id.button_favorite);
        favorite_button.setOnClickListener(this);
        Button review_button = rootView.findViewById(R.id.button_myreview);
        review_button.setOnClickListener(this);
        text_profile = rootView.findViewById(R.id.text_profile);
        profile_image = rootView.findViewById(R.id.image_profile);

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
        }
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            mainActivity.db.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            text_profile.setText(document.get("nickname").toString());
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
            text_profile.setText(null);
        }
    }

}