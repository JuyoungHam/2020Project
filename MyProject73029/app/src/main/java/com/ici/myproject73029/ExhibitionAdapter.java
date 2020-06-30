package com.ici.myproject73029;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExhibitionAdapter extends RecyclerView.Adapter<ExhibitionAdapter.ViewHolder> {

    ArrayList<Exhibitions> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exhibitions item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textDescription = itemView.findViewById(R.id.text_description);
        }

        public void setItem(Exhibitions item) {
            textTitle.setText(item.getTitle());
            textDescription.setText(item.getDescription());
        }
    }

    public void addItem(Exhibitions item) {
        items.add(item);
    }

    public void setItems(ArrayList<Exhibitions> items) {
        this.items = items;
    }

    public Exhibitions getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Exhibitions item) {
        items.set(position, item);
    }
}
