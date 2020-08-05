package com.ici.myproject73029.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.FundamentalItem;

import java.util.ArrayList;
import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder>
        implements OnItemClickListener {
    ArrayList<FundamentalItem> items = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.horizontal_item, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FundamentalItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, position);
        }
    }

    public void setTags(List<Object> tag) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gridTitle;
        TextView gridDescription;
        ImageView imageView;
        View itemView = super.itemView;
        LinearLayout grid = itemView.findViewById(R.id.grid_container);


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_gird);
            gridTitle = itemView.findViewById(R.id.title_grid);
            gridDescription = itemView.findViewById(R.id.description_grid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(v, position);
                    }
                }
            });
        }

        public void setItem(FundamentalItem item) {
            gridTitle.setText(Html.fromHtml(item.getTitle()));
            if (item.getPoster() != null) {
                Glide.with(itemView).load(item.getPoster()).into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
                gridTitle.setSingleLine(false);
                gridTitle.setWidth(300);
                gridTitle.setHeight(540);
            }

        }
    }

    public void addItem(FundamentalItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<FundamentalItem> items) {
        this.items = items;
    }

    public FundamentalItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, FundamentalItem item) {
        items.set(position, item);
    }

    public void clearItems() {
        items.clear();
    }

}

