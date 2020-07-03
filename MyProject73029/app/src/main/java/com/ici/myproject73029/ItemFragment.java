package com.ici.myproject73029;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemFragment extends Fragment {
    String title;
    String description;
    String venue;
    Timestamp endDate,startDate;
    FirebaseStorage storage;
    StorageReference storageReference;
    public ItemFragment(exhibitions item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
    }

    public ItemFragment(Show item) {
        this.title = item.getTitle();
        this.venue = item.getVenue();
        this.endDate=item.getendDate();
        this.startDate=item.getStartDate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.fragment_item, container,
                false);

        final ImageView img_poster = itemView.findViewById(R.id.item_poster);
        TextView item_title = itemView.findViewById(R.id.item_title);
        TextView item_venue = itemView.findViewById(R.id.item_venue);
        TextView item_period = itemView.findViewById(R.id.item_period);
        TextView item_description = itemView.findViewById(R.id.item_description);

        item_title.setText(title);
        item_description.setText(description);
        item_period.setText(startDate+"~"+endDate);
        String name=title;
        System.out.println(title);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference().child(name+".png");
       // gsReference=storage.getReferenceFromUrl("gs://my-project-73029-baca5.appspot.com/ShowPoster/YOU&IT.png");
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Glide.with(getContext()).load(task.getResult()).into(img_poster);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ㅠㅠㅠㅠㅠㅠㅠㅠㅠ",e.toString());
            }
        });

        return itemView;
    }
}