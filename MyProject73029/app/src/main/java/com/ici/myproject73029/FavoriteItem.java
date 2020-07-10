package com.ici.myproject73029;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ici.myproject73029.firebase.Firebase;
import com.ici.myproject73029.items.Favorites;

import java.util.Map;


public class FavoriteItem extends Fragment {
    String title,poster;

    public FavoriteItem(Favorites item){
        this.poster=item.getPoster();
        this.title=item.getTitle();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup view=(ViewGroup)inflater.inflate(R.layout.fragment_favorite_item,container,false);
        final TextView txt_title=view.findViewById(R.id.title_favorite);
        final ImageView img_poster=view.findViewById(R.id.img_favorite);
        txt_title.setText(title);
        if(title!=null) {
            Log.d("즐겨찾기", title);
        }else{
            Log.d("즐겨찾기2","결과 x");
        }

        Firebase firebase=new Firebase();
        FirebaseFirestore ffs=firebase.startFirebase();
        ffs.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        Map<String,Object> map=snapshot.getData();
                        if(map.get("poster")!=null){
                            Glide.with(view).load(map.get("poster")).into(img_poster);
                            img_poster.setVisibility(View.VISIBLE);
                        }else{
                            img_poster.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error",e.toString());
            }
        });

        return view;

    }
}