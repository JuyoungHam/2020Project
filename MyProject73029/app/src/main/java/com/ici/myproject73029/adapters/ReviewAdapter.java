package com.ici.myproject73029.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.Review;
import com.ici.myproject73029.tabs.ReviewListFragment;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>
        implements OnItemClickListener {
    ArrayList<Review> items = new ArrayList<>();
    OnItemClickListener onItemClickListener;
    int type;
    int item_limit = 10;
    private FirebaseStorage storage;
    private View itemView;

    public ReviewAdapter() {
        super();
    }

    public ReviewAdapter(int i) {
        super();
        this.type = i;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.review, parent, false);
        storage = FirebaseStorage.getInstance();

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        if (items.size() > item_limit) {
            return item_limit;
        } else {
            return items.size();
        }
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
        TextView info;
        TextView title;
        TextView comments;
        LinearLayout container = itemView.findViewById(R.id.review_item_container);
        ImageView profile;
        private final RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            info = itemView.findViewById(R.id.review_item_info);
            title = itemView.findViewById(R.id.review_item_title);
            comments = itemView.findViewById(R.id.review_item_comments);
            profile = itemView.findViewById(R.id.review_item_profile);
            ratingBar = itemView.findViewById(R.id.review_rating_bar);

            container.setVisibility(View.VISIBLE);

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
            if (type == Constant.MYREVIEWPAGE) {
                info.setText(item.getItemInfo());
                profile.setVisibility(View.GONE);
            } else {
                info.setText(item.getWriter());
            }
            if (item.getRating() != 0) {
                ratingBar.setRating(item.getRating());
            }
            StorageReference storageRef = storage.getReference().child("profile_images");
            storageRef.child(item.getUserId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(itemView).load(uri).circleCrop().into(profile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
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

    public void clearItems() {
        items.clear();
    }
}

