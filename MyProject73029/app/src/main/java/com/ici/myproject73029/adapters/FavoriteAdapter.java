package com.ici.myproject73029.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.FavoriteItem;
import com.ici.myproject73029.items.FundamentalItem;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<FavoriteItem> items=new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item,parent,false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FavoriteItem item=items.get(position);

    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView poster;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.txt_id);
            poster=itemView.findViewById(R.id.img_favorite);

        }
        public void setItems(FavoriteItem items) {
            title.setText(items.getTitle());
            if (items.getPoster() != null) {
                Glide.with(itemView).load(items.getPoster()).into(poster);
                poster.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(FavoriteItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<FavoriteItem> items) {
        this.items = items;
    }

    public FavoriteItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, FavoriteItem item) {
        items.set(position, item);
    }
}
