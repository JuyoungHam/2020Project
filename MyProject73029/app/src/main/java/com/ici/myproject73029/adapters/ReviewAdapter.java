package com.ici.myproject73029.adapters;

import android.net.Uri;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.Review;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>
        implements OnItemClickListener {
    ArrayList<Review> items = new ArrayList<>();
    OnItemClickListener onItemClickListener;
    int type;
    int item_limit = 10;
    private FirebaseStorage storage;
    private FirebaseFirestore db;

    public ReviewAdapter() {
        super();
    }

    public ReviewAdapter(int i) {
        super();
        this.type = i;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.review, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView info;
        TextView title;
        TextView comments;
        LinearLayout container = itemView.findViewById(R.id.review_item_container);
        ImageView profile;
        private final RatingBar ratingBar;
        private final TextView date;
        final ImageButton like;
        private final TextView review_item_like_count;
        String writer;
        private String item_info;
        private FirebaseUser user;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            info = itemView.findViewById(R.id.review_item_info);
            title = itemView.findViewById(R.id.review_item_title);
            comments = itemView.findViewById(R.id.review_item_comments);
            profile = itemView.findViewById(R.id.review_item_profile);
            ratingBar = itemView.findViewById(R.id.review_rating_bar);
            date = itemView.findViewById(R.id.review_item_date);
            like = itemView.findViewById(R.id.review_item_like);
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) like.setOnClickListener(this);
            review_item_like_count = itemView.findViewById(R.id.review_item_like_count);

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
            user = FirebaseAuth.getInstance().getCurrentUser();
            title.setText(item.getTitle());
            comments.setText(item.getComments());
            if (type == Constant.MYREVIEWPAGE) {
                info.setText(item.getItemInfo());
                profile.setVisibility(View.GONE);
                like.setEnabled(false);
            } else {
                info.setText(item.getWriter());
            }
            if (item.getRating() != 0) {
                ratingBar.setRating(item.getRating());
            }
            StorageReference storageRef = storage.getReference().child("profile_images");
            if (item.getUserId() != null) {
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
            CharSequence format = DateFormat.format("yyyy-MM-dd hh:mm", item.getCreate_date());
            date.setText(format + " 작성");
            review_item_like_count.setText(item.get_number_who_liked() + "");
            writer = item.getUserId();
            item_info = item.getItemInfo();
            isLiked();
        }

        private void isLiked() {
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                db = FirebaseFirestore.getInstance();
                db.collection("All").document(item_info).collection("comments")
                        .whereEqualTo("userId", writer).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                ArrayList<String> list = (ArrayList<String>) document.get("who_liked");
                                if (list != null) {
                                    if (!list.contains(user.getUid())) {
                                        like.setImageResource(R.drawable.unfavorited);
                                    } else {
                                        like.setImageResource(R.drawable.favorited);
                                    }
                                } else {
                                    like.setImageResource(R.drawable.unfavorited);
                                }
                            }
                        }
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.review_item_like) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        make_favorite();
                    }
                }).start();
            }
        }

        private void make_favorite() {
            user = FirebaseAuth.getInstance().getCurrentUser();
            db = FirebaseFirestore.getInstance();
            db.collection("All").document(item_info).collection("comments")
                    .whereEqualTo("userId", writer).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    ArrayList<String> list = (ArrayList<String>) document.get("who_liked");
                                    if (list != null) {
                                        final int count = list.size();
                                        if (!list.contains(user.getUid())) {
                                            like.setImageResource(R.drawable.favorited);
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("who_liked", FieldValue.arrayUnion(user.getUid()));
                                            data.put("like_count", count + 1);
                                            document.getReference().set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    review_item_like_count.setText(String.valueOf(count + 1));
                                                }
                                            });
                                        } else {
                                            like.setImageResource(R.drawable.unfavorited);
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("who_liked", FieldValue.arrayRemove(user.getUid()));
                                            data.put("like_count", count - 1);
                                            document.getReference().set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    review_item_like_count.setText(String.valueOf(count - 1));
                                                }
                                            });
                                        }
                                    } else {
                                        like.setImageResource(R.drawable.favorited);
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("who_liked", Arrays.asList(user.getUid()));
                                        data.put("like_count", 1);
                                        document.getReference().set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                review_item_like_count.setText(String.valueOf(1));
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("like", e.toString());
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

