package com.ici.myproject73029.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.adapters.HorizontalAdapter;
import com.ici.myproject73029.adapters.OnItemClickListener;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.FundamentalItem;

import java.sql.Time;

public class HomeTab extends Fragment {
    private MainActivity mainActivity;
    private FirebaseFirestore db;
    private RecyclerView new_recycler;
    private RecyclerView pop_recycler;
    private RecyclerView exp_recycler;

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.isActionBarVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_home, container, false);

        mainActivity = (MainActivity) getActivity();

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayout profile_set = rootView.findViewById(R.id.home_profile_set);
        ImageView profile_image = rootView.findViewById(R.id.home_profile_image);
        TextView profile_text = rootView.findViewById(R.id.home_profile_text);
        if (user != null) {
            profile_set.setVisibility(View.VISIBLE);
            mainActivity.getUserProfileImage(profile_image);
            profile_text.setText(user.getDisplayName() + "님, 안녕하세요!");
        }

        new_recycler = rootView.findViewById(R.id.new_recyclerview);
        pop_recycler = rootView.findViewById(R.id.pop_recyclerview);
        exp_recycler = rootView.findViewById(R.id.exp_recyclerview);

        GridLayoutManager newlm = new GridLayoutManager(getContext(), 1,
                RecyclerView.HORIZONTAL, false);
        GridLayoutManager poplm = new GridLayoutManager(getContext(), 1,
                RecyclerView.HORIZONTAL, false);
        GridLayoutManager explm = new GridLayoutManager(getContext(), 1,
                RecyclerView.HORIZONTAL, false);

        new_recycler.setLayoutManager(newlm);
        pop_recycler.setLayoutManager(poplm);
        exp_recycler.setLayoutManager(explm);

        updateItemList(new_recycler);
        updateItemList(pop_recycler);
        updateItemList(exp_recycler);


        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.hide();
        return rootView;
    }

    private void updateItemList(final RecyclerView recyclerView) {
        final HorizontalAdapter adapter = new HorizontalAdapter();
        Query query = db.collection("All");

        if (recyclerView == new_recycler) {
            query = query.orderBy("start_date", Query.Direction.DESCENDING);
        } else if (recyclerView == pop_recycler) {
            query = query.orderBy("favorite_count",
                    Query.Direction.DESCENDING);
        } else if (recyclerView == exp_recycler) {
            query.whereGreaterThan("start_date", Timestamp.now()).orderBy("start_date",
                    Query.Direction.DESCENDING);
        }

        query.limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        FundamentalItem item = document.toObject(FundamentalItem.class);
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
                        FundamentalItem item = (FundamentalItem) adapter.getItem(position);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.onItemFragmentChanged(item);
                    }
                });
            }
        });
    }
}