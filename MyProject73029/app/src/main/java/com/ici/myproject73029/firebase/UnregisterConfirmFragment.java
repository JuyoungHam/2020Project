package com.ici.myproject73029.firebase;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.ici.myproject73029.R;

public class UnregisterConfirmFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("탈퇴를 진행하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseUIActivity firebaseUIActivity = (FirebaseUIActivity) getActivity();
                        firebaseUIActivity.unregister();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}