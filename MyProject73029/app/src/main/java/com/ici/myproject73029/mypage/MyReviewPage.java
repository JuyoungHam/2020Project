package com.ici.myproject73029.mypage;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MyReviewPage extends Fragment implements MainActivity.onBackPressedListener {

    private RecyclerView recyclerView;
    MainActivity mainActivity;

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
        mainActivity.isActionBarVisible(true);
        mainActivity.setActionBarTitle("내 리뷰");
        mainActivity.setActionBarOption(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        Firebase firebase = new Firebase();
        FirebaseFirestore db = firebase.startFirebase();
        {
            db.collection("Users").document("user1").collection("comments").get()
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
                                }
                            });
                        }
                    });
        }


        return rootView;
    }

    @Override
    public void onBack() {
        Log.e("Other", "onBack()");
        mainActivity.setOnBackPressedListener(null);
        mainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, mainActivity.myPageTab).commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity) context).setOnBackPressedListener(this);
    }


}