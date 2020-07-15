package com.ici.myproject73029.tabs;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.firebase.UnregisterConfirmFragment;
import com.ici.myproject73029.items.FundamentalItem;
import com.ici.myproject73029.items.Review;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFragment extends Fragment implements View.OnClickListener, MainActivity.onBackPressedListener {
    String title;
    String description;
    public String venue;
    int type;
    boolean isFavorite;
    private List<Address> list = null;

    private MainActivity mainActivity;
    private Geocoder geocoder;
    private FirebaseFirestore db;
    private ImageButton make_favorite;
    private FirebaseUser user;
    private TextView favorite_count;
    private int count;

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
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_item, container,
                false);

        mainActivity = (MainActivity) getActivity();
        mainActivity.isActionBarVisible(true);
        mainActivity.setActionBarTitle(title);
        mainActivity.setActionBarOption(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        final ImageView img_poster = rootView.findViewById(R.id.item_poster);
        TextView item_title = rootView.findViewById(R.id.item_title);
        TextView item_venue = rootView.findViewById(R.id.item_venue);
        TextView item_period = rootView.findViewById(R.id.item_period);
        TextView item_description = rootView.findViewById(R.id.item_comments);
        favorite_count = rootView.findViewById(R.id.favorite_count);
        ImageButton img_map = rootView.findViewById(R.id.connect_map);
        img_map.setOnClickListener(this);
        ImageButton make_comment = rootView.findViewById(R.id.make_comment);
        make_comment.setOnClickListener(this);
        make_favorite = rootView.findViewById(R.id.make_favorite);
        make_favorite.setOnClickListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            make_favorite.setVisibility(View.GONE);
            make_comment.setVisibility(View.GONE);
        }

        geocoder = new Geocoder(getContext());

        ImageButton share_button = rootView.findViewById(R.id.share_button);
        share_button.setOnClickListener(this);

        item_title.setText(title);
        if (description != null)
            item_description.setText(Html.fromHtml(description));
        item_venue.setText(venue);

        getChildFragmentManager().beginTransaction().replace(R.id.framelayout_review_list,
                new ReviewListFragment(title)).commit();

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();
        db.collection("All")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> fieldData = document.getData();
                                if (fieldData.get("favorite_count") != null) {
                                    count = Integer.parseInt(fieldData.get("favorite_count").toString());
                                    favorite_count.setText(count + "");
                                } else {
                                    favorite_count.setText("0");
                                }
                                if (fieldData.get("poster") != null) {
                                    Glide.with(rootView).load(fieldData.get("poster").toString()).into(img_poster);
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


        user = mainActivity.mAuth.getCurrentUser();
        checkIsFavorite();

        return rootView;
    }

    private void checkIsFavorite() {
        if (user != null) {
            db.collection("Users").document(user.getUid()).collection("favorite").document(title).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    isFavorite = true;
                                    make_favorite.setColorFilter(Color.RED);
                                } else {
                                    isFavorite = false;
                                    make_favorite.clearColorFilter();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.share_button) {
            start_share();
        } else if (i == R.id.connect_map) {
            get_geoApp();
        } else if (i == R.id.make_comment) {
            make_comment();
        } else if (i == R.id.make_favorite) {
            make_favorite();
        }
    }

    private void make_favorite() {
        if (isFavorite) {
            db.collection("Users").document(user.getUid()).collection("favorite").document(title).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            isFavorite = false;
                            make_favorite.clearColorFilter();
                            add_favorite_count(-1);
                            Toast.makeText(mainActivity, "좋아요 해제", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("favorite", "좋아요 해제 실패");
                        }
                    });
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            Date currentTime = Calendar.getInstance().getTime();
            data.put("date", currentTime.toString());
            db.collection("Users").document(user.getUid()).collection("favorite").document(title).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            isFavorite = true;
                            make_favorite.setColorFilter(Color.RED);
                            add_favorite_count(1);
                            Toast.makeText(mainActivity, "좋아요 설정", Toast.LENGTH_SHORT).show();
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

    private void make_comment() {
        DialogFragment make_comment = new CreateReviewFragment(title);
        make_comment.show(getChildFragmentManager(), "comment");
    }

    private void get_geoApp() {
        try {
            list = geocoder.getFromLocationName(venue, 100);

        } catch (IOException e) {
            Log.d("Map Error", e.toString());
        }
        if (list != null) {
            if (list.size() == 0) {
                Toast.makeText(getContext(), "주소 없음", Toast.LENGTH_SHORT).show();
            } else {
                double lat = list.get(0).getLatitude();
                double lon = list.get(0).getLongitude();
                Uri location = Uri.parse(("geo:" + lat + "," + lon));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                PackageManager packageManager = getActivity().getPackageManager();
                List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(mapIntent, 0);
                boolean result = resolveInfoList.size() > 0;
                if (result) {
                    startActivity(mapIntent);
                }
            }
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

    public void add_favorite_count(final int i) {
        Map<String, Object> data = new HashMap<>();
        data.put("favorite_count", (count + i > 0 ? count + i : 0));
        db.collection("All").document(title).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                favorite_count.setText(String.valueOf(count + i > 0 ? count + i : 0));
            }
        });

    }
}