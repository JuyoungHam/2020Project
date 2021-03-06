package com.ici.myproject73029.tabs;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.ici.myproject73029.items.Show;

public class ShowTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private MainActivity mainActivity;
    private SwipeRefreshLayout refreshLayout;
    private FundamentalAdapter adapter;
    private FirebaseFirestore db;
    private Spinner spinner;
    private ArrayAdapter<String> spinner_adapter;

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

        final ConstraintLayout constraintLayout = rootView.findViewById(R.id.constraintLayout_gridtab);
        refreshLayout = rootView.findViewById(R.id.swipeRefreshLayout_grid);
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

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);

//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager);

        mainActivity = (MainActivity) getActivity();
        mainActivity.isActionBarVisible(false);

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();

        final AssetManager assetManager = this.getContext().getAssets();
        spinner = rootView.findViewById(R.id.spinner);
        spinner_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                Constant.SHOW_TAGS) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(assetManager,
                        "font/Cafe24Oneprettynight.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(20);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(assetManager,
                        "font/Cafe24Oneprettynight.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(20);
                v.setPadding(30, 30, 30, 30);
                return v;
            }
        };
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(this);

        FloatingActionButton fab = rootView.findViewById(R.id.upButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.getLayoutManager().scrollToPosition(0);
            }
        });

        adapter = new FundamentalAdapter();

        updateItemList(null);

        return rootView;
    }

    private void updateItemList(String tag) {
        adapter.clearItems();
        adapter.notifyDataSetChanged();
        Query query = db.collection("All").whereEqualTo("category", 102).whereEqualTo("isOn", true);

        if (tag != null && !tag.equals(Constant.SHOW_TAGS[0])) {
            query = query.whereArrayContains("tag", tag);
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Show item = document.toObject(Show.class);
                        adapter.addItem(item);
                        adapter.notifyItemInserted(i++);
                    }
                    adapter.setIsGrid(true);
                } else {
                    Log.d(Constant.TAG, "Error getting documents: ", task.getException());
                }

                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Show item = (Show) adapter.getItem(position);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.onItemFragmentChanged(item);
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh() {
        String spinner_item = spinner.getSelectedItem().toString();
        spinner.setSelection(spinner_adapter.getPosition(spinner_item));
        updateItemList(spinner_item);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateItemList(Constant.SHOW_TAGS[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        updateItemList(null);
    }
}