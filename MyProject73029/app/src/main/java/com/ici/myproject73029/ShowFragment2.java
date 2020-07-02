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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowFragment2 extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_show_list, container, false);

        recyclerView = rootView.findViewById(R.id.show_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final ShowAdapter adapter = new ShowAdapter();

        Firebase firebase = new Firebase();
        FirebaseFirestore db = firebase.startFirebase();
        {
            db.collection("Show").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Show item = document.toObject(Show.class);
                                    adapter.addItem(item);
                                }
                            } else {
                                Log.d(Constant.TAG, "Error getting documents: ", task.getException());
                            }

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();
                            adapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(ExhibitionAdapter.ViewHolder holder, View view, int position) {
                                    Show item = adapter.getItem(position);
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