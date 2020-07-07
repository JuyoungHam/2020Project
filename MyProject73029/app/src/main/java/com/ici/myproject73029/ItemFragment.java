package com.ici.myproject73029;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.Show;

import java.util.Map;

public class ItemFragment extends Fragment {
    String title;
    String description;
    String venue;
    Timestamp endDate, startDate;
    int type;

    public ItemFragment(Exhibition item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
        type = Constant.EXHIBITION;
    }

    public ItemFragment(Show item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
        type = Constant.SHOW;

        //수빈씨 이거 오류 났는데 함수 정의 부분이 없어요!
//        this.endDate=item.getendDate();
//        this.startDate=item.getStartDate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.fragment_item, container,
                false);

        final ImageView img_poster = itemView.findViewById(R.id.item_poster);
        TextView item_title = itemView.findViewById(R.id.item_title);
        TextView item_venue = itemView.findViewById(R.id.item_venue);
        TextView item_period = itemView.findViewById(R.id.item_period);
        TextView item_description = itemView.findViewById(R.id.item_description);

        item_title.setText(title);
        if (description != null)
            item_description.setText(Html.fromHtml(description));

        Firebase firebase = new Firebase();
        FirebaseFirestore db = firebase.startFirebase();
        db.collection("All")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> fieldData = document.getData();
                                if (fieldData.get("poster") != null) {
                                    Glide.with(itemView).load(fieldData.get("poster").toString()).into(img_poster);
                                    img_poster.setVisibility(View.VISIBLE);
                                } else {
                                    img_poster.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Log.d(Constant.TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });

        //오류나서 임시로 잠궜어요
//        item_period.setText(startDate+"~"+endDate);
//        String name=title;

        return itemView;
    }
}