package com.ici.myproject73029.mypage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.Review;

import java.util.HashMap;
import java.util.Map;

public class UpdateReviewFragment extends DialogFragment {

    private String item;
    private MainActivity mainActivity;
    private EditText review_title;
    private EditText review_comments;
    private FirebaseUser user;
    private RatingBar ratingBar;
    private Map<String, Object> comment = new HashMap<>();
    private boolean isFirst = false;

    public UpdateReviewFragment() {
        super();
    }

    public UpdateReviewFragment(String item) {
        super();
        this.item = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_create_review, null);

        user = FirebaseAuth.getInstance().getCurrentUser();

        review_title = rootView.findViewById(R.id.review_title_editText);
        review_comments = rootView.findViewById(R.id.review_comments_editText);
        ratingBar = rootView.findViewById(R.id.item_rating_bar);

        mainActivity.db.collection("All").document(item).collection("comments").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot result) {
                        if (result.exists()) {
                            review_title.setText(result.get("title").toString());
                            review_comments.setText(result.get("comments").toString());
                            if (result.get("rating") != null) {
                                double rating = (double) result.get("rating");
                                ratingBar.setRating((float) rating);
                            }
                        }
                    }
                });

        builder.setView(rootView)
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        comment.put("title", review_title.getText().toString());
                        comment.put("comments", review_comments.getText().toString());
                        comment.put("update_date", Timestamp.now());
                        comment.put("itemInfo", item);
                        if (ratingBar.getRating() != 0) {
                            comment.put("rating", ratingBar.getRating());
                        }
                        if (user != null) {
                            comment.put("userId", user.getUid());
                            comment.put("writer", user.getDisplayName());
                            mainActivity.db.collection("All").document(item).collection("comments")
                                    .document(user.getUid()).set(comment, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(Constant.TAG, "DocumentSnapshot successfully written!");
                                            mainActivity.db.collection("All").document(item).collection("comments")
                                                    .document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    Review review = task.getResult().toObject(Review.class);
                                                    mainActivity.onItemFragmentChanged(review);
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(Constant.TAG, "Error writing document", e);
                                        }
                                    });
                        }

                    }
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        return builder.create();
    }

}