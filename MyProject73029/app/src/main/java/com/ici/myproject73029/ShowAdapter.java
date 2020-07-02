package com.ici.myproject73029;

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
public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder>
        implements OnItemClickListener {

    private List<Show> mValues = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    public void setItems(ArrayList<Show> items) {
        this.mValues = items;
    }

    public void addItem(Show item) {
        mValues.add(item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Show show = mValues.get(position);
        holder.title.setText(show.getTitle());
        holder.venue.setText(show.getVenue());
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onItemClick(ExhibitionAdapter.ViewHolder holder, View view, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(holder, view, position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public Show getItem(int position) {
        return mValues.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView title;
        public TextView venue;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.text_title);
            venue = (TextView) view.findViewById(R.id.text_description);

        }
//
//        public void setText(Show set){
//            title.setText(set.getTitle());
//            venue.setText(set.getVenue());
//        }
    }
}