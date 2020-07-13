package com.ici.myproject73029.tabs;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.MapActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.FundamentalItem;
import com.ici.myproject73029.items.Show;

import java.util.Map;

public class ItemFragment extends Fragment implements View.OnClickListener, MainActivity.onBackPressedListener {
    String title;
    String description;
    public String venue;
    int type;
    private MainActivity mainActivity;

    public ItemFragment(FundamentalItem item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
        if (item.getVenue() != null) {
            this.venue = item.getVenue();
        }
        this.type = item.getType();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.fragment_item, container,
                false);

        mainActivity = (MainActivity) getActivity();
        mainActivity.isActionBarVisible(true);
        mainActivity.setActionBarTitle(title);
        mainActivity.setActionBarOption(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        final ImageView img_poster = itemView.findViewById(R.id.item_poster);
        TextView item_title = itemView.findViewById(R.id.item_title);
        TextView item_venue = itemView.findViewById(R.id.item_venue);
        TextView item_period = itemView.findViewById(R.id.item_period);
        TextView item_description = itemView.findViewById(R.id.item_comments);
        ImageView img_map = itemView.findViewById(R.id.map);
        img_map.setImageResource(R.mipmap.ic_launcher_foreground);
        ImageButton share_button = itemView.findViewById(R.id.share_button);
        share_button.setOnClickListener(this);

        item_title.setText(title);
        if (description != null)
            item_description.setText(Html.fromHtml(description));
        item_venue.setText(venue);

        img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra("venue", venue);
                startActivity(intent);
            }
        });


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

        return itemView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.share_button) {
            start_share();
        }
    }

    private void start_share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + (description != null ? description : ""));
        sendIntent.setType("text/*");
        Intent shareIntent = Intent.createChooser(sendIntent, "공유하기");
        startActivity(shareIntent);
    }

    @Override
    public void onBack() {
        Log.d("Other", "onBack()");
        mainActivity.setOnBackPressedListener(null);
        if (type == Constant.EXHIBITION) {
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.exhibitionTab).commit();
        } else if (type == Constant.SHOW) {
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.showTab).commit();
        } else if (type == Constant.FAVORITE) {
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.favoritePage).commit();
        }
    }

    // Fragment 호출 시 반드시 호출되는 오버라이드 메소드입니다.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity) context).setOnBackPressedListener(this);
    }
}