package com.ici.myproject73029.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.Favorites;
import com.ici.myproject73029.items.FundamentalItem;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Viewholder> {
    private ArrayList<Favorites> items=new ArrayList<>();

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item,parent,false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Favorites item=items.get(position);
        holder.setItems(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView poster;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_id);
            poster = itemView.findViewById(R.id.img_favorite);
        }

        public void setItems(Favorites items) {
            title.setText(items.getTitle());
            if (items.getPoster() != null) {
                Glide.with(itemView).load(items.getPoster()).into(poster);
                poster.setVisibility(View.VISIBLE);
            }
        }
    }
    public void addItem(Favorites item) {
        items.add(item);
    }

    public void setItems(ArrayList<Favorites> items) {
        this.items = items;
    }

    public Favorites getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Favorites item) {
        items.set(position, item);
    }
}
