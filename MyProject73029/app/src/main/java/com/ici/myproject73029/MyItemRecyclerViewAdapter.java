package com.ici.myproject73029;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ici.myproject73029.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<Show> mValues=new ArrayList<>();

    public MyItemRecyclerViewAdapter(Context context, List<Show> mValues) {
        if(mValues==null){
            mValues=new ArrayList<>();
        }else {
            this.mValues = mValues;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Show show=mValues.get(position);
        holder.title.setText(show.getTitle());
        holder.venue.setText(show.getVenue());
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView title;
        public TextView venue;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.txtTitle);
            venue = (TextView) view.findViewById(R.id.txtVenue);

        }
//
//        public void setText(Show set){
//            title.setText(set.getTitle());
//            venue.setText(set.getVenue());
//        }
    }
}