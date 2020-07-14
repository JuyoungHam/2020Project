package com.ici.myproject73029.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ici.myproject73029.R;
import com.ici.myproject73029.items.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>
        implements OnItemClickListener {
    ArrayList<Review> items = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review item = items.get(position);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView creator;
        TextView title;
        TextView comments;
        LinearLayout linearLayout = itemView.findViewById(R.id.list_container);

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            creator = itemView.findViewById(R.id.review_item_creator);
            title = itemView.findViewById(R.id.title_list);
            comments = itemView.findViewById(R.id.description_list);

            linearLayout.setVisibility(View.VISIBLE);

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

        public void setItem(Review item) {
            title.setText(item.getTitle());
            comments.setText(item.getComments());
        }
    }

    public void addItem(Review item) {
        items.add(item);
    }

    public void setItems(ArrayList<Review> items) {
        this.items = items;
    }

    public Review getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Review item) {
        items.set(position, item);
    }
}

