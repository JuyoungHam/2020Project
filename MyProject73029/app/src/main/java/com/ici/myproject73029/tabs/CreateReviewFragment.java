package com.ici.myproject73029.tabs;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.Show;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateReviewFragment extends DialogFragment {

    private String item;
    private MainActivity mainActivity;
    private EditText review_title;
    private EditText review_comments;
    private FirebaseUser user;
    private RatingBar ratingBar;
    private Map<String, Object> comment = new HashMap<>();
    private boolean isFirst = true;
    int type;

    public CreateReviewFragment() {
        super();
    }

    public CreateReviewFragment(String item, int type) {
        super();
        this.item = item;
        this.type = type;
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
                            if (result.get("create_date") != null)
                                isFirst = false;
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
                        if (isFirst) {
                            comment.put("create_date", Timestamp.now());
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
                                            mainActivity.db.collection("All").document(item).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (type == Constant.EXHIBITION) {
                                                        Exhibition item = task.getResult().toObject(Exhibition.class);
                                                        mainActivity.onItemFragmentChanged(item);
                                                    } else if (type == Constant.SHOW) {
                                                        Show item = task.getResult().toObject(Show.class);
                                                        mainActivity.onItemFragmentChanged(item);
                                                    }
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