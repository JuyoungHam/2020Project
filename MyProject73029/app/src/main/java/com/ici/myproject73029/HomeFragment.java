package com.ici.myproject73029;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        String title = "전시";
        String description = "새로운 전시 입니다.";

        for (int i = 0; i < 10; i++) {
            Exhibitions item = new Exhibitions(title, description);
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