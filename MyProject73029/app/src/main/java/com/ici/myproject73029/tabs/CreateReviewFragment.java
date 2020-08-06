package com.ici.myproject73029.tabs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.Show;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CreateReviewFragment extends DialogFragment implements TextWatcher {

    private String item;
    private MainActivity mainActivity;
    private EditText review_title;
    private EditText review_comments;
    private FirebaseUser user;
    private RatingBar ratingBar;
    private Map<String, Object> comment = new HashMap<>();
    private boolean isFirst = true;
    int type;
    private AlertDialog.Builder builder;
    private TextView title_length;

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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_create_review, null);
        builder = new AlertDialog.Builder(mainActivity).setView(rootView);

        user = FirebaseAuth.getInstance().getCurrentUser();

        review_title = rootView.findViewById(R.id.review_title_editText);
        title_length = rootView.findViewById(R.id.title_length);
        review_title.addTextChangedListener(this);
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

        addDeleteButton();

        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>네</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        comment.put("title", review_title.getText().toString());
                        comment.put("comments", review_comments.getText().toString());
                        comment.put("update_date", Timestamp.now());
                        comment.put("itemInfo", item);
                        comment.put("like_count", 0);
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
                                            refreshItem();
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
                }).setNegativeButton(Html.fromHtml("<font color='#000000'>아니요</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }

    private void addDeleteButton() {
        builder.setNeutralButton(Html.fromHtml("<font color='#000000'>삭제</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mainActivity.db.collection("All").document(item).collection("comments")
                        .document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            documentSnapshot.getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(mainActivity, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        refreshItem();
                                    } else {
                                        Toast.makeText(mainActivity, "삭제 도중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void refreshItem() {
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        title_length.setText(review_title.getText().length() + "/20");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = review_title.getText().length();
        title_length.setText(length + "/20");
        if (length >= 20) {
            title_length.setTextColor(getResources().getColor(R.color.colorAccent));
        } else if (length < 20) {
            title_length.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}