package com.ici.myproject73029;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ExhibitionAdapter adapter = new ExhibitionAdapter();

        adapter.addItem(new Exhibitions("전시1", "전시1입니다"));
        adapter.addItem(new Exhibitions("전시2", "새로운 전시입니다."));
        adapter.addItem(new Exhibitions("전시1", "전시1입니다"));
        adapter.addItem(new Exhibitions("전시2", "새로운 전시입니다."));
        adapter.addItem(new Exhibitions("전시1", "전시1입니다"));
        adapter.addItem(new Exhibitions("전시2", "새로운 전시입니다."));
        adapter.addItem(new Exhibitions("전시1", "전시1입니다"));
        adapter.addItem(new Exhibitions("전시2", "새로운 전시입니다."));
        adapter.addItem(new Exhibitions("전시1", "전시1입니다"));
        adapter.addItem(new Exhibitions("전시2", "새로운 전시입니다."));
        adapter.addItem(new Exhibitions("전시1", "전시1입니다"));
        adapter.addItem(new Exhibitions("전시2", "새로운 전시입니다."));

        recyclerView.setAdapter(adapter);

    }
}