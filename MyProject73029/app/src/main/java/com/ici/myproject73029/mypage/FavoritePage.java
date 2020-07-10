package com.ici.myproject73029.mypage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class FavoritePage extends Fragment {


    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_favorite, container, false);

        recyclerView = rootView.findViewById(R.id.favorite_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final FundamentalAdapter adapter = new FundamentalAdapter();

        Firebase firebase = new Firebase();
        FirebaseFirestore db = firebase.startFirebase();
        {
            db.collection("Users").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Exhibition item = document.toObject(Exhibition.class);
                                    adapter.addItem(item);
                                }
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


        return rootView;
    }
}