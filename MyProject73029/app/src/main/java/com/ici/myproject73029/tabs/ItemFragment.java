package com.ici.myproject73029.tabs;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.ici.myproject73029.databinding.FragmentItemBinding;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.FundamentalItem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class ItemFragment extends Fragment implements View.OnClickListener,
        MainActivity.onBackPressedListener, SwipeRefreshLayout.OnRefreshListener {
    String title;
    String description;
    public String venue;
    int type;
    boolean isFavorite;
    String url;
    String poster;
    Timestamp start_date;
    Timestamp end_date;
    FundamentalItem item;
    private List<Address> list = null;

    private MainActivity mainActivity;
    private Geocoder geocoder;
    private FirebaseFirestore db;
    private ImageButton make_favorite;
    private FirebaseUser user;
    private TextView favorite_count;
    private SwipeRefreshLayout refreshLayout;
    private ViewGroup rootView;
    private ImageView img_poster;
    private ReviewListFragment listFragment;
    private ChipGroup chipGroup;
    private TextView rating_score;
    private FrameLayout review_list;

    public ItemFragment(FundamentalItem item) {
        this.item = item;
        this.title = item.getTitle();
        this.description = item.getDescription();
        if (item.getVenue() != null) {
            this.venue = item.getVenue();
        }
        this.type = item.getType();
        this.url = item.getUrl();
        this.poster = item.getPoster();
        this.start_date = item.getStart_date();
        this.end_date = item.getEnd_date();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_item, container,
//                false);
        FragmentItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item, container,
                false);
        rootView = (ViewGroup) binding.getRoot();

        mainActivity = (MainActivity) getActivity();
        mainActivity.isActionBarVisible(true);
        mainActivity.setActionBarTitle(title);
        mainActivity.setActionBarOption(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        img_poster = rootView.findViewById(R.id.item_poster);
        img_poster.setOnClickListener(this);
        TextView item_title = rootView.findViewById(R.id.item_title);
        TextView item_venue = rootView.findViewById(R.id.item_venue);
        TextView item_period = rootView.findViewById(R.id.item_period);
        TextView item_description = rootView.findViewById(R.id.item_description);
        favorite_count = rootView.findViewById(R.id.favorite_count);
        ImageButton img_map = rootView.findViewById(R.id.connect_map);
        img_map.setOnClickListener(this);
        LinearLayout set_map = rootView.findViewById(R.id.set_map);
        ImageButton make_comment = rootView.findViewById(R.id.make_comment);
        make_comment.setOnClickListener(this);
        make_favorite = rootView.findViewById(R.id.make_favorite);
        make_favorite.setOnClickListener(this);
        final ScrollView scrollView2 = rootView.findViewById(R.id.item_scroll_view);
        refreshLayout = rootView.findViewById(R.id.swipeRefreshLayout_item);
        refreshLayout.setDistanceToTriggerSync(200);
        refreshLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView2.getScrollY() == 0)
                    refreshLayout.setEnabled(true);
                else
                    refreshLayout.setEnabled(false);
            }
        });
        refreshLayout.setOnRefreshListener(this);
        chipGroup = rootView.findViewById(R.id.item_chip_group);
        rating_score = rootView.findViewById(R.id.rating_score);
        item_description.setVisibility(View.GONE);
        item_venue.setVisibility(View.GONE);
        item_period.setVisibility(View.GONE);
        ImageButton go_to_url = rootView.findViewById(R.id.go_to_url);

        TextView item=rootView.findViewById(R.id.item_title);
        Typeface tf=Typeface.createFromAsset(getContext().getAssets(),"font/Cafe24Oneprettynight.ttf");
        item.setTypeface(tf);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            make_favorite.setEnabled(false);
            make_favorite.setImageResource(R.drawable.favorited);
            rootView.findViewById(R.id.set_comment).setVisibility(View.GONE);
            rootView.findViewById(R.id.space_comment).setVisibility(View.GONE);
        }

        geocoder = new Geocoder(getContext());
        if (venue != null) {
            try {
                list = geocoder.getFromLocationName(venue, 100);

            } catch (IOException e) {
                Log.d("Map Error", e.toString());
            }
            if (list != null) {
                set_map.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.space_map).setVisibility(View.VISIBLE);
            }
        }

        if (url != null) {
            go_to_url.setOnClickListener(this);
            rootView.findViewById(R.id.set_url).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.space_url).setVisibility(View.VISIBLE);
        }

        ImageButton share_button = rootView.findViewById(R.id.share_button);
        share_button.setOnClickListener(this);

        item_title.setText(Html.fromHtml(title));
        if (description != null) {
            item_description.setText(Html.fromHtml(description));
            item_description.setVisibility(View.VISIBLE);
        }
        if (venue != null) {
            item_venue.setText(venue);
            item_venue.setVisibility(View.VISIBLE);
        }
        if (start_date != null && end_date != null) {
            SimpleDateFormat sfd = new SimpleDateFormat("yy년 MM월 dd일", Locale.getDefault());
            item_period.setText(sfd.format(start_date.toDate()) + " ~ " + sfd.format(end_date.toDate()));
            item_period.setVisibility(View.VISIBLE);
        }

        Firebase firebase = new Firebase();
        db = firebase.startFirebase();
        getItemFromDatabase();

        user = mainActivity.mAuth.getCurrentUser();
        checkIsFavorite();

        review_list = rootView.findViewById(R.id.framelayout_review_list);
        listFragment = new ReviewListFragment(title, mainActivity);
        getChildFragmentManager().beginTransaction().replace(R.id.framelayout_review_list,
                listFragment).commit();

//        updateComments();

        return rootView;
    }

    private void getItemFromDatabase() {
        db.collection("All")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> fieldData = document.getData();
                                if (fieldData.get(Constant.FAVORITE_COUNT) != null) {
                                    int count =
                                            Integer.parseInt(fieldData.get(Constant.FAVORITE_COUNT).toString());
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

                                ArrayList<String> tags = (ArrayList<String>) document.get("tag");
                                if (tags != null) {
                                    for (String tag : tags) {
                                        Chip chip = new Chip(getContext());
                                        chip.setText(tag);
                                        chip.setChipIcon(mainActivity.resources.getDrawable(R.drawable.ic_iconfinder_icon_hashtag));
                                        chip.setChipIconSize(50);
                                        chip.setChipBackgroundColorResource(R.color.colorAccent);
                                        chip.setTextColor(Color.WHITE);
                                        chipGroup.addView(chip);
                                    }
                                }
                                getAverageRates(document.getReference());
                            }
                        } else {
                            Log.d(Constant.TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });
    }

    private void getAverageRates(DocumentReference reference) {
        reference.collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                double rates = 0;
                int count = 0;
                for (DocumentSnapshot document : task.getResult()) {
                    if (document.get("rating") != null && (double) document.get("rating") > 0) {
                        rates += (double) document.get("rating");
                        count += 1;
                    }
                }
                double avg = rates / count;
                if (count > 0) rating_score.setText(String.format("%.1f", avg));
                else rating_score.setText(null);
            }
        });
    }

    public void updateComments() {
        listFragment.getCommentsFromDatabase(Constant.REVIEW_LIST_SPINNER[0]);
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
                                    make_favorite.setImageResource(R.drawable.favorited);
                                } else {
                                    isFavorite = false;
                                    make_favorite.setImageResource(R.drawable.unfavorited);
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
        } else if (i == R.id.go_to_url) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } else if (i == R.id.item_poster) {
            mainActivity.loadMultiMediaWeb(item, poster);
        }
    }

    private void make_favorite() {
        if (isFavorite) {
            db.collection("Users").document(user.getUid()).collection("favorite").document(title).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            isFavorite = false;
                            add_favorite_count(-1);
                            make_favorite.setImageResource(R.drawable.unfavorited);
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
                            add_favorite_count(1);
                            make_favorite.setImageResource(R.drawable.favorited);
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
        DialogFragment make_comment = new CreateReviewFragment(title, type);
        make_comment.show(getChildFragmentManager(), "comment");
    }

    private void get_geoApp() {
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
        } else if (type == Constant.MYREVIEWPAGE) {
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.myReviewPage).commit();
        } else if (type == -1) {
            mainActivity.getSupportActionBar().hide();
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, mainActivity.homeTab).commit();
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
        db.collection("All").document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> list = (ArrayList<String>) document.get("who_liked");
                    int count = 0;
                    if (list != null) {
                        count = list.size();
                    }
                    Map<String, Object> data = new HashMap<>();
                    data.put(Constant.FAVORITE_COUNT, (count + i > 0 ? count + i : 0));
                    if (i > 0) {
                        data.put("who_liked", FieldValue.arrayUnion(user.getUid()));
                    } else {
                        data.put("who_liked", FieldValue.arrayRemove(user.getUid()));
                    }
                    final int finalCount = count;
                    db.collection("All").document(title).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            favorite_count.setText(String.valueOf(finalCount + i > 0 ? finalCount + i : 0));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        updateItems();
        updateComments();
        checkIsFavorite();
        DocumentReference reference =
                db.collection("All").document(title);
        getAverageRates(reference);
        refreshLayout.setRefreshing(false);
    }

    private void updateItems() {
        db.collection("All")
                .whereEqualTo("title", title)
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(Constant.TAG, "Listen error", e);
                            return;
                        }

                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                Log.d(Constant.TAG, change.getDocument().getId());
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(Constant.TAG, "Data fetched from " + source);
                        }

                    }
                });
    }

}