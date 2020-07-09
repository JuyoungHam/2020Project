package com.ici.myproject73029;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;

public class MyPage extends AppCompatActivity {
    private static final int REQUEST_CODE=0;
    private ImageView profile;
    private TextView id;
    private Button favorite,review;
    private FavoriteFragment frgFavorite;
    View frg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        profile=findViewById(R.id.img_profile);
        id=findViewById(R.id.txt_id);
        favorite=findViewById(R.id.btnFavorite);
        review=findViewById(R.id.btnReview);
        frg=findViewById(R.id.frag_main);
        final Login_main loginMain=new Login_main();

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frgFavorite=new FavoriteFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_main
                        , frgFavorite).commit();
                frg.setVisibility(View.GONE);
            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            id.setText(user.getUid());
        }else{
            id.setText("로그인을 해주세요");
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                try {
                    InputStream is=getContentResolver().openInputStream(data.getData());
                    Bitmap img= BitmapFactory.decodeStream(is);
                    is.close();
                    profile.setImageBitmap(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"사진 선택 취소",Toast.LENGTH_SHORT).show();
            }
        }
    }
}