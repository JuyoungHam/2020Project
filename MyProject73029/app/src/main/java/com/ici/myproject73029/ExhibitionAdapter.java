package com.ici.myproject73029;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExhibitionAdapter extends RecyclerView.Adapter<ExhibitionAdapter.ViewHolder>
        implements OnItemClickListener {
    ArrayList<exhibitions> items = new ArrayList<>();
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
        exhibitions item = items.get(position);
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
        TextView textTitle;
        TextView textDescription;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textDescription = itemView.findViewById(R.id.text_description);

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

        public void setItem(exhibitions item) {
            textTitle.setText(item.getTitle());
            textDescription.setText(item.getDescription());
        }
    }

    public void addItem(exhibitions item) {
        items.add(item);
    }

    public void setItems(ArrayList<exhibitions> items) {
        this.items = items;
    }

    public exhibitions getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, exhibitions item) {
        items.set(position, item);
    }
}
