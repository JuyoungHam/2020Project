package com.ici.myproject73029;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShowAdapter extends ArrayAdapter<Show> {

    ArrayList<Show> items;

    public ShowAdapter(@NonNull Context context, @NonNull List<Show> objects) {
        super(context, 0, objects);
    }

    @Nullable
    @Override
    public Show getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            //convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.showitem,parent,false);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.showitem, null);
        }
        Show show = (Show) getItem(position);
        TextView title = convertView.findViewById(R.id.txtTitle);
        TextView venue = convertView.findViewById(R.id.txtVenue);

        title.setText(show.getTitle());
        venue.setText(show.getVenue());

        return convertView;
    }
}
