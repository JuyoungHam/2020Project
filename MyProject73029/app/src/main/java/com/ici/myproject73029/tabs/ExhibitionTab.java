package com.ici.myproject73029.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.adapters.OnItemClickListener;
import com.ici.myproject73029.R;
import com.ici.myproject73029.adapters.FundamentalAdapter;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Exhibition;

import java.util.ArrayList;

public class ExhibitionTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private MainActivity mainActivity;
    private SwipeRefreshLayout refreshLayout;
    private FundamentalAdapter adapter;
    private FirebaseFirestore db;
    private ArrayList<String> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_exhibition, container, false);
//
//        recyclerView = rootView.findViewById(R.id.exhibition_recyclerView);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
//                LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        final FundamentalAdapter adapter = new FundamentalAdapter();

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_grid, container, false);

        recyclerView = rootView.findViewById(R.id.grid_recyclerView);

        final FrameLayout frameLayout = rootView.findViewById(R.id.framelayout_gridtab);
        refreshLayout = rootView.findViewById(R.id.swipeRefreshLayout_grid);
        refreshLayout.setDistanceToTriggerSync(200);
        refreshLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (frameLayout.getScrollY() == 0)
                    refreshLayout.setEnabled(true);
                else
                    refreshLayout.setEnabled(false);
            }
        });
        refreshLayout.setOnRefreshListener(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager);

        mainActivity = (MainActivity) getActivity();
        mainActivity.isActionBarVisible(false);

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();

        list = new ArrayList<>();
        updateItemList();

        return rootView;
    }

    private void updateItemList() {
        adapter = new FundamentalAdapter();
        db.collection("All").whereEqualTo("category", 101).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Exhibition item = document.toObject(Exhibition.class);
                                if (document.get("subjectCategory") != null)
                                    list.add(document.get("subjectCategory").toString());
                                adapter.addItem(item);
                                adapter.setIsGrid(true);
                            }
                            for (String str : list)
                                Log.d("list", str);
                        } else {
                            Log.d(Constant.TAG, "Error getting documents: ", task.getException());
                        }

                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Exhibition item = (Exhibition) adapter.getItem(position);
                                MainActivity mainActivity = (MainActivity) getActivity();
                                mainActivity.onItemFragmentChanged(item);
                            }
                        });
                    }
                });
    }

    @Override
    public void onRefresh() {
//        updateItemList();
        refreshLayout.setRefreshing(false);
    }
}