package com.ici.myproject73029.tabs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public CreateReviewFragment() {
        super();
    }

    public CreateReviewFragment(String item) {
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

        mainActivity.db.collection("All").document(item).collection("comments").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            review_title.setText(documentSnapshot.get("title").toString());
                            review_comments.setText(documentSnapshot.get("comments").toString());
                        }
                    }
                });

        builder.setView(rootView)
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Map<String, Object> comment = new HashMap<>();
                        comment.put("title", review_title.getText().toString());
                        comment.put("comments", review_comments.getText().toString());
                        comment.put("update_date", Timestamp.now());
                        comment.put("itemInfo", item);
                        if (user != null) {
                            comment.put("userId", user.getUid());
                            comment.put("writer", user.getDisplayName());
                            mainActivity.db.collection("All").document(item).collection("comments")
                                    .document(user.getUid()).set(comment, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(Constant.TAG, "DocumentSnapshot successfully written!");
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