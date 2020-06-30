package com.ici.myproject73029;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final ExhibitionAdapter adapter = new ExhibitionAdapter();

        for (int i = 0; i < 10; i++) {
            Exhibitions item = new Exhibitions("전시" + i, "새로운 전시입니다.");
            adapter.addItem(item);
        }

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ExhibitionAdapter.ViewHolder holder, View view, int position) {
                Exhibitions item = adapter.getItem(position);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.onItemFragmentChanged(item.getTitle());
            }
        });
        return rootView;
    }
}