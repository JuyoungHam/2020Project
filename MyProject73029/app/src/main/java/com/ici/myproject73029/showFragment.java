package com.ici.myproject73029;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class showFragment extends AppCompatActivity {
    private TextView title, venue;
    //private DatabaseReference reference;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fragment);
        title = findViewById(R.id.txtTitle);
        venue = findViewById(R.id.txtVenue);
        db = FirebaseFirestore.getInstance();

        db.collection("Show").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList list = new ArrayList<String>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("ShowTest", document.getId() + " => " + document.getData());
                        Show show = document.toObject(Show.class);
                        list.add(list);
                    }
                    ListView listView = findViewById(R.id.listView);
                    ShowAdapter adapter = new ShowAdapter(getApplicationContext(), list);
                    listView.setAdapter(adapter);
                } else {
                    Log.d("ShowTest", "Error getting documents: ", task.getException());
                }
            }
        });
        // adapter.addAll(list);
    }
}