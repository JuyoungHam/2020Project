package com.ici.myproject73029.mypage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Review;

import java.util.Map;

public class ReviewFragment extends Fragment implements MainActivity.onBackPressedListener {
    String title;
    String comments;
    int type;
    private MainActivity mainActivity;

    public ReviewFragment(Review item) {
        this.title = item.getTitle();
        this.comments = item.getComments();
        type = Constant.REVIEW;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container,
                false);
        mainActivity = (MainActivity) getActivity();

        TextView item_title = itemView.findViewById(R.id.item_title);
        TextView item_description = itemView.findViewById(R.id.item_comments);

        item_title.setText(title);
        item_description.setText(comments);

        Firebase firebase = new Firebase();
        FirebaseFirestore db = firebase.startFirebase();
        db.collection("Users")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> fieldData = document.getData();
                            }
                        } else {
                            Log.d(Constant.TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });

        return itemView;
    }


    @Override
    public void onBack() {
        Log.d("Other", "onBack()");
        mainActivity.setOnBackPressedListener(null);
        if (type == Constant.REVIEW) {
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.myReviewPage).commit();
        } else if (type == Constant.FAVORITE) {
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.favoritePage).commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity) context).setOnBackPressedListener(this);
    }
}