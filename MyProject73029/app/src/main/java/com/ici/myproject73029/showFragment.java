package com.ici.myproject73029;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A fragment representing a list of Items.
 */
public class showFragment extends Fragment {
    Activity activity;
    private FirebaseFirestore db;
    private List list;
    TextView title,venue;

    public showFragment(){
        list=new ArrayList<>();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
//        ValueEventListener valueEventListener=new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list=new ArrayList<Show>();
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Log.i("ShowTest",dataSnapshot.getValue().toString());
//                    Show show=dataSnapshot.getValue(Show.class);
//                    list.add(show);
//                }
//                Log.d("ShowTest",list.toString());
//                RecyclerView recyclerView=getView().findViewById(R.id.list);
//                MyItemRecyclerViewAdapter adapter=new MyItemRecyclerViewAdapter(getActivity(),list);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        data.addValueEventListener(valueEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_show_list, container, false);
        title=view.findViewById(R.id.txtTitle);

        db = FirebaseFirestore.getInstance();
        db.collection("Show").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list = new ArrayList<Show>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String,Object> map=document.getData();
                        Log.d("ShowTest", document.getId() + " => " + document.getData());
                        Show show = document.toObject(Show.class);
                      //  title.setText(document.getData().toString());
                        list.add(show);
                        Log.i("List",map.toString());
                    }
                    RecyclerView recyclerView = view.findViewById(R.id.list);
                    MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("ShowTest", "Error getting documents: ", task.getException());
                }
                //adapter.addAll(list);
            }
        });
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Show");
//        final DatabaseReference data=reference.child("title");
//        recyclerView=view.findViewById(R.id.list);
//        title=view.findViewById(R.id.txtTitle);
//        venue=view.findViewById(R.id.txtVenue);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                showList=new ArrayList<>();
//                showList.clear();
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Show show=dataSnapshot.getValue(Show.class);
//                    showList.add(show);
//                }
//                adapter=new MyItemRecyclerViewAdapter(getContext(),showList);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                if(showList.isEmpty()){
//                    title.setVisibility(view.VISIBLE);
//                    venue.setVisibility(view.VISIBLE);
//                }else{
//                    title.setVisibility(view.GONE);
//                    venue.setVisibility(view.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return view;
    }
}