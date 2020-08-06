package com.ici.myproject73029.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.adapters.ReviewAdapter;
import com.ici.myproject73029.databinding.FragmentReviewListBinding;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Review;

import java.util.List;

public class ReviewListFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private RecyclerView recyclerView;
    private MainActivity mainActivity = null;
    String title;
    private FirebaseFirestore db;
    private ReviewAdapter adapter;
    private ViewGroup rootView;
    private Button load_review;

    public ReviewListFragment() {
        super();
    }

    public ReviewListFragment(String title) {
        super();
        this.title = title;
    }

    public ReviewListFragment(String title, MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
        this.title = title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review_list, container, false);
        FragmentReviewListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_list,
                container,
                false);
        rootView = (ViewGroup) binding.getRoot();

        Spinner spinner = rootView.findViewById(R.id.review_list_spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                Constant.REVIEW_LIST_SPINNER);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(this);

        recyclerView = rootView.findViewById(R.id.reviewlist_recyclerView);

        load_review = rootView.findViewById(R.id.load_review);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReviewAdapter();

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();

        return rootView;
    }

    public void getCommentsFromDatabase(String option) {
        adapter.clearItems();
        adapter.notifyDataSetChanged();
        Query query = db.collection("All").document(title).collection("comments");
        if (option.equals(Constant.REVIEW_LIST_SPINNER[0])) {
            query = query.orderBy("create_date", Query.Direction.DESCENDING);
        } else if (option.equals(Constant.REVIEW_LIST_SPINNER[1])) {
            query = query.orderBy("create_date", Query.Direction.ASCENDING);
        } else if (option.equals(Constant.REVIEW_LIST_SPINNER[2])) {
            query = query.orderBy("like_count", Query.Direction.DESCENDING);
        } else if (option.equals(Constant.REVIEW_LIST_SPINNER[3])) {
            query = query.orderBy("rating", Query.Direction.DESCENDING);
        } else if (option.equals(Constant.REVIEW_LIST_SPINNER[4])) {
            query = query.orderBy("rating", Query.Direction.ASCENDING);
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() != 0) {
                        int i = 0;
                        for (final QueryDocumentSnapshot document : task.getResult()) {
                            final Review item = document.toObject(Review.class);
                            adapter.addItem(item);
                            adapter.notifyItemInserted(i++);
                            if (mainActivity != null && i > 2) {
                                load_review.setVisibility(View.VISIBLE);
                                load_review.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mainActivity.loadMoreReview(item.getItemInfo());
                                    }
                                });
                                break;
                            }
                        }
                    } else {
                        LinearLayout linearLayout = rootView.findViewById(R.id.review_list_container);
                        TextView textView = new TextView(getContext());
                        textView.setText("리뷰가 존재하지 않습니다.");
                        textView.setPadding(4, 4, 4, 4);
                        linearLayout.removeAllViews();
                        linearLayout.addView(textView);
                    }

                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d(Constant.TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getCommentsFromDatabase(Constant.REVIEW_LIST_SPINNER[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}