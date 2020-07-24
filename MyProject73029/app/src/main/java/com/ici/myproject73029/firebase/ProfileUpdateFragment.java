package com.ici.myproject73029.firebase;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ici.myproject73029.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileUpdateFragment extends DialogFragment {

    public static final int GET_FROM_GALLERY = 1543;

    private EditText nickname;
    private FirebaseUIActivity firebaseUIActivity;
    private String nickname_text;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private ImageView profile;
    private Bitmap bitmap;

    public ProfileUpdateFragment(FirebaseStorage storage, FirebaseUser user) {
        super();
        this.nickname_text = user.getDisplayName();
        this.storage = storage;
        this.user = user;
    }

    public ProfileUpdateFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile_update, null);

        firebaseUIActivity = (FirebaseUIActivity) getActivity();

        nickname = rootView.findViewById(R.id.nickname_edittext);
        nickname.setText(nickname_text);

        profile = rootView.findViewById(R.id.image_profile);
        firebaseUIActivity.getUserProfileImage(profile);

        Button image_change = rootView.findViewById(R.id.image_change_button);
        image_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, GET_FROM_GALLERY);
            }
        });

        builder.setView(rootView)
                .setPositiveButton(Html.fromHtml("<font color='#000000'>네</font>"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                firebaseUIActivity.change_profile(user, nickname.getText().toString());
                                if (bitmap != null) {
                                    uploadProfileImage(bitmap);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY)
            if (resultCode == Activity.RESULT_OK) {
                Uri filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public void uploadProfileImage(final Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();
        StorageReference
                storageRef = storage.getReference().child("profile_images").child(user.getUid());

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

    }
}