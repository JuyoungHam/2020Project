package com.ici.myproject73029;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    final public String TAG = "FirebaseLog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final ExhibitionAdapter adapter = new ExhibitionAdapter();

        String title = "전시";
        String description = "새로운 전시 입니다.";

        Firebase firebase = new Firebase();
        FirebaseFirestore db = firebase.startFirebase();

        for (int i = 0; i < 10; i++) {
            Exhibitions item = new Exhibitions(title + i, description);
            adapter.addItem(item);
        }

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ExhibitionAdapter.ViewHolder holder, View view, int position) {
                Exhibitions item = adapter.getItem(position);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.onItemFragmentChanged(item);
            }
        });
        return rootView;
    }
}