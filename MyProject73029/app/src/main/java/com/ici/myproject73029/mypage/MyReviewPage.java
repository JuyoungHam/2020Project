package com.ici.myproject73029.mypage;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.adapters.OnItemClickListener;
import com.ici.myproject73029.adapters.ReviewAdapter;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Review;

public class MyReviewPage extends Fragment implements MainActivity.onBackPressedListener,
        SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    MainActivity mainActivity;
    FirebaseUser user;
    private SwipeRefreshLayout refreshLayout;
    private ReviewAdapter adapter;
    private FirebaseFirestore db;
    private ConstraintLayout constraintLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_myreview, container, false);

        recyclerView = rootView.findViewById(R.id.myreview_recyclerView);

        constraintLayout = rootView.findViewById(R.id.constraintlayout_myreview);
        refreshLayout = rootView.findViewById(R.id.swipeRefreshLayout_myreview);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReviewAdapter(Constant.MYREVIEWPAGE);

        mainActivity = (MainActivity) getActivity();
        mainActivity.isActionBarVisible(true);
        mainActivity.setActionBarTitle("내 리뷰");
        mainActivity.setActionBarOption(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        user = mainActivity.mAuth.getCurrentUser();

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();

        updateReviews();

        return rootView;
    }

    private void updateReviews() {
        adapter.clearItems();
        adapter.notifyDataSetChanged();

        db.collectionGroup("comments").whereEqualTo("userId",
                user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review item = document.toObject(Review.class);
                                adapter.addItem(item);
                                adapter.notifyItemInserted(i++);
                            }
                            if (task.getResult().getDocuments().size() == 0) {
                                TextView textView = new TextView(getContext());
                                textView.setText("작성한 리뷰가 존재하지 않습니다.");
                                constraintLayout.addView(textView);
                            }
                        } else {
                            Log.d(Constant.TAG, "Error getting documents: ", task.getException());
                        }

                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Review item = adapter.getItem(position);
                                MainActivity mainActivity = (MainActivity) getActivity();
                                mainActivity.onItemFragmentChanged(item);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        adapter.addItem(null);
                    }
                });
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

    private void updateCache() {
        db.collectionGroup("comments").whereEqualTo("userId",
                user.getUid())
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(Constant.TAG, "Listen error", e);
                            return;
                        }

                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                Log.d(Constant.TAG, "New city:" + change.getDocument().getData());
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(Constant.TAG, "Data fetched from " + source);
                        }

                    }
                });
    }

    @Override
    public void onRefresh() {
        updateReviews();
        updateCache();
        refreshLayout.setRefreshing(false);
    }
}