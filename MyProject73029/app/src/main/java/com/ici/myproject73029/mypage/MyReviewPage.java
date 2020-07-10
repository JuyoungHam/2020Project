package com.ici.myproject73029.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.adapters.OnItemClickListener;
import com.ici.myproject73029.adapters.ReviewAdapter;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Review;

public class MyReviewPage extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    MainActivity mainActivity;
    private ImageButton review_to_mypage;
    private ImageButton item_to_review;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_myreview, container, false);

        recyclerView = rootView.findViewById(R.id.myreview_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final ReviewAdapter adapter = new ReviewAdapter();

        mainActivity = (MainActivity) getActivity();

        review_to_mypage = rootView.findViewById(R.id.review_to_mypage);
        review_to_mypage.setOnClickListener(this);
        item_to_review = rootView.findViewById(R.id.item_to_review);
        item_to_review.setOnClickListener(this);
        item_to_review.setVisibility(View.GONE);

        Firebase firebase = new Firebase();
        FirebaseFirestore db = firebase.startFirebase();
        {
            db.collection("Users").document("comments").collection("comments").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Review item = document.toObject(Review.class);
                                    adapter.addItem(item);
                                }
                            } else {
                                Log.d(Constant.TAG, "Error getting documents: ", task.getException());
                            }

                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Review item = (Review) adapter.getItem(position);
                                    MainActivity mainActivity = (MainActivity) getActivity();
                                    mainActivity.onItemFragmentChanged(item);
                                    review_to_mypage.setVisibility(View.GONE);
                                    item_to_review.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
        }


        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.item_to_review) {
            Toast.makeText(mainActivity, "to review", Toast.LENGTH_SHORT).show();
            mainActivity.goToFragment(R.id.item_to_review);
            item_to_review.setVisibility(View.GONE);
            review_to_mypage.setVisibility(View.VISIBLE);
        } else if (i == R.id.review_to_mypage) {
            Toast.makeText(mainActivity, "to mypage", Toast.LENGTH_SHORT).show();
            mainActivity.goToFragment(R.id.review_to_mypage);
            item_to_review.setVisibility(View.GONE);
        }
    }
}