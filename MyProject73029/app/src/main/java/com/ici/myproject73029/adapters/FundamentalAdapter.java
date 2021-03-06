package com.ici.myproject73029.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.FundamentalItem;
import com.ici.myproject73029.tabs.ExhibitionTab;

import java.util.ArrayList;
import java.util.List;

public class FundamentalAdapter extends RecyclerView.Adapter<FundamentalAdapter.ViewHolder>
        implements OnItemClickListener {
    ArrayList<FundamentalItem> items = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    private boolean isGrid;
    public ChipGroup chipGroup;

    public void setIsGrid(boolean grid) {
        isGrid = grid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item, parent, false);

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
        TextView listTitle;
        TextView listDescription;
        TextView gridTitle;
        TextView gridDescription;
        ImageView imageView;
        View itemView = super.itemView;
        LinearLayout grid = itemView.findViewById(R.id.grid_container);
        LinearLayout list = itemView.findViewById(R.id.list_container);


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            listTitle = itemView.findViewById(R.id.title_list);
            listDescription = itemView.findViewById(R.id.description_list);
            list.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
            chipGroup = itemView.findViewById(R.id.grid_list_chip_group);

            if (isGrid) {
                imageView = itemView.findViewById(R.id.image_gird);
                gridTitle = itemView.findViewById(R.id.title_grid);
                gridDescription = itemView.findViewById(R.id.description_grid);
                list.setVisibility(View.GONE);
                grid.setVisibility(View.VISIBLE);
            }

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
            listTitle.setText(Html.fromHtml(item.getTitle()));
            listDescription.setText((item.getDescription() != null ?
                    Html.fromHtml(item.getDescription()) : ""));
            if (isGrid) {
                gridTitle.setText(Html.fromHtml(item.getTitle()));
//                gridDescription.setText((item.getDescription() != null ?
//                        Html.fromHtml(item.getDescription()) : ""));
                if (item.getPoster() != null) {
                    Glide.with(itemView).load(item.getPoster()).into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                }
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

