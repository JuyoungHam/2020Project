package com.ici.myproject73029;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ici.myproject73029.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */

public class ShowAdapter extends FundamentalAdapter{
    public ShowAdapter(){
        super();
    }

    private List<Show> mValues = new ArrayList<>();
    OnItemClickListener onItemClickListener;


    public void addItem(Show item) {
        mValues.add(item);
    }

    @Override
    public void onBindViewHolder(@NonNull FundamentalAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Show show = mValues.get(position);
        holder.textTitle.setText(show.getTitle());
        holder.textDescription.setText(show.getVenue());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public Show getItem(int position) {
        return mValues.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView venue;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text_title);
            venue = (TextView) view.findViewById(R.id.text_description);

        }

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            venue = itemView.findViewById(R.id.text_description);

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
//
//        public void setText(Show set){
//            title.setText(set.getTitle());
//            venue.setText(set.getVenue());
//        }
    }
}