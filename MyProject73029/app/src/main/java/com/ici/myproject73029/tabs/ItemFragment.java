package com.ici.myproject73029.tabs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.R;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Exhibition;
import com.ici.myproject73029.items.Show;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ItemFragment extends Fragment implements View.OnClickListener {
    String title;
    String description;
    public String venue;
    int type;
    private List<Address> list=null;

    public ItemFragment(Exhibition item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
        type = Constant.EXHIBITION;
    }

    public ItemFragment(Show item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.venue = item.getVenue();
        type = Constant.SHOW;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.fragment_item, container,
                false);

        final ImageView img_poster = itemView.findViewById(R.id.item_poster);
        TextView item_title = itemView.findViewById(R.id.item_title);
        TextView item_venue = itemView.findViewById(R.id.item_venue);
        TextView item_period = itemView.findViewById(R.id.item_period);
        TextView item_description = itemView.findViewById(R.id.item_comments);
        ImageView img_map = itemView.findViewById(R.id.map);
        img_map.setImageResource(R.mipmap.ic_launcher_foreground);
        ImageButton share_button = itemView.findViewById(R.id.share_button);
        share_button.setOnClickListener(this);
        final Geocoder geocoder=new Geocoder(getContext());


        img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    list=geocoder.getFromLocationName(venue,100);

                } catch (IOException e) {
                    Log.d("Map Error",e.toString());
                }
                if(list!=null){
                    if(list.size()==0){
                        Toast.makeText(getContext(),"주소 없음",Toast.LENGTH_SHORT).show();
                    }else{
                        double lat=list.get(0).getLatitude();
                        double lon=list.get(0).getLongitude();
                        Uri location=Uri.parse(("geo:"+lat+","+lon));
                        Intent mapIntent=new Intent(Intent.ACTION_VIEW,location);
                        PackageManager packageManager=getActivity().getPackageManager();
                        List<ResolveInfo> resolveInfoList=packageManager.queryIntentActivities(mapIntent,0);
                        boolean result=resolveInfoList.size()>0;
                        if(result){
                            startActivity(mapIntent);
                        }
                    }
                }
            }
        });

        item_title.setText(title);
        if (description != null)
            item_description.setText(Html.fromHtml(description));
        item_venue.setText(venue);


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

}