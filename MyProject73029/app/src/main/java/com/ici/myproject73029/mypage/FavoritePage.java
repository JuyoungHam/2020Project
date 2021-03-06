package com.ici.myproject73029.mypage;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.adapters.FundamentalAdapter;
import com.ici.myproject73029.adapters.OnItemClickListener;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.FundamentalItem;

public class FavoritePage extends Fragment implements View.OnClickListener,
        MainActivity.onBackPressedListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private Firebase firebase;
    private FirebaseFirestore db;
    private FundamentalAdapter adapter;
    private MainActivity mainActivity;
    private FirebaseUser user;
    private SwipeRefreshLayout refreshLayout;
    private ConstraintLayout constraintLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_favorite, container, false);

        user = mainActivity.mAuth.getCurrentUser();

        recyclerView = rootView.findViewById(R.id.favorite_recyclerView);
        constraintLayout = rootView.findViewById(R.id.constraintlayout_favorite);
        refreshLayout = rootView.findViewById(R.id.swipeRefreshLayout_favorite);
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
        adapter = new FundamentalAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        mainActivity.isActionBarVisible(true);
        mainActivity.setActionBarTitle("좋아요");
        mainActivity.setActionBarOption(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        firebase = new Firebase();
        db = firebase.startFirebase();

        updateFavorites();

        return rootView;
    }

    private void updateFavorites() {
        db.collection("Users").document(user.getUid()).collection("favorite").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                getFavorite(document.getId());
                            }
                            if (task.getResult().getDocuments().size() == 0) {
                                TextView textView = new TextView(getContext());
                                textView.setText("좋아요를 설정한 아이템이 없습니다.");
                                constraintLayout.addView(textView);
                            }
                        } else {
                            Log.d(Constant.TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void getFavorite(String id) {
        adapter.clearItems();
        adapter.notifyDataSetChanged();
        db.collection("All").whereEqualTo("title", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Exhibition item = document.toObject(Exhibition.class);
                        adapter.addItem(item);
                        adapter.notifyItemInserted(i++);
                    }
                } else {
                    Log.d(Constant.TAG, "Error getting documents: ", task.getException());
                }
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Exhibition item = (Exhibition) adapter.getItem(position);
                        item.setType(Constant.FAVORITE);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.onItemFragmentChanged(item);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
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

    @Override
    public void onRefresh() {
        updateFavorites();
        refreshLayout.setRefreshing(false);
    }
}