package com.ici.myproject73029.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.Firebase;

public class HomeTab extends Fragment {
    private MainActivity mainActivity;
    private FirebaseFirestore db;

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.isActionBarVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_home, container, false);

        LinearLayout list_new_items = rootView.findViewById(R.id.list_new_items);
        LinearLayout list_popular_items = rootView.findViewById(R.id.list_popular_items);
        LinearLayout list_expected_items = rootView.findViewById(R.id.list_expected_items);

        mainActivity = (MainActivity) getActivity();

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();

        fullfillList(list_new_items, "start_date");

        return rootView;
    }

    private void fullfillList(final LinearLayout l, String field) {
        db.collection("All").orderBy(field).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        for (int i = 0; i < 5; i++) {

                        }
                    }
                }
            }
        });
    }
}