package com.ici.myproject73029.mypage;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Review;

import java.util.Date;

public class MyReviewFragment extends Fragment implements MainActivity.onBackPressedListener,
        View.OnClickListener {
    String title;
    String comments;
    String itemInfo;
    int type;
    Date create_date;
    float rating;
    private MainActivity mainActivity;
    private ImageView review_item_poster;
    private TextView review_item_title;
    private TextView review_item_description;
    private FirebaseFirestore db;

    public MyReviewFragment(Review item) {
        this.title = item.getTitle();
        this.comments = item.getComments();
        this.itemInfo = item.getItemInfo();
        this.rating = item.getRating();
        this.create_date = item.getCreate_date();
        type = Constant.REVIEW;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container,
                false);
        mainActivity = (MainActivity) getActivity();

        TextView item_title = itemView.findViewById(R.id.item_title);
        TextView item_comments = itemView.findViewById(R.id.item_comments);
        review_item_poster = itemView.findViewById(R.id.review_item_poster);
        review_item_title = itemView.findViewById(R.id.review_item_title);
        review_item_description = itemView.findViewById(R.id.review_item_description);
        final RatingBar ratingBar = itemView.findViewById(R.id.item_rating_bar);
        final TextView date_textView = itemView.findViewById(R.id.create_date);
        ratingBar.setRating(rating);
        CharSequence date = DateFormat.format("yyyy-MM-dd hh:mm", create_date);
        date_textView.setText(date + " 작성");
        CardView cardView = itemView.findViewById(R.id.review_item_container);
        cardView.setOnClickListener(this);
        Button update = itemView.findViewById(R.id.review_update_button);
        update.setOnClickListener(this);
        Button delete = itemView.findViewById(R.id.review_delete_button);
        delete.setOnClickListener(this);

        item_title.setText(title);
        item_comments.setText(comments);

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();

        db.collection("All").document(itemInfo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    review_item_title.setText(result.get("title").toString());
                    if (result.get("description") != null) {
                        review_item_description.setText(Html.fromHtml(result.get("description").toString()));
                        review_item_description.setVisibility(View.VISIBLE);
                    }
                    if (result.get("poster") != null) {
                        review_item_poster.setVisibility(View.VISIBLE);
                        Glide.with(getContext()).load(result.get("poster").toString()).into(review_item_poster);
                    }
                }
            }
        });

        return itemView;
    }

    private void gotoItem() {
        db.collection("All").document(itemInfo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    Review item = result.toObject(Review.class);
                    item.setType(Constant.MYREVIEWPAGE);
                    mainActivity.onItemFragmentChanged(item);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mainActivity, "DB에서 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBack() {
        Log.d("Other", "onBack()");
        mainActivity.setOnBackPressedListener(null);
        if (type == Constant.REVIEW) {
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.myReviewPage).commit();
        } else if (type == Constant.FAVORITE) {
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.favoritePage).commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity) context).setOnBackPressedListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.review_item_container) {
            gotoItem();
        } else if (i == R.id.review_update_button) {
            update_comment();
        } else if (i == R.id.review_delete_button) {
            delete_comment();
        }
    }

    private void update_comment() {
        DialogFragment make_comment = new UpdateReviewFragment(itemInfo);
        make_comment.show(getChildFragmentManager(), "comment");
    }

    private void delete_comment() {
        DialogFragment make_comment = new ReviewDeleteConfirmFragment(itemInfo);
        make_comment.show(getChildFragmentManager(), "comment");
    }
}