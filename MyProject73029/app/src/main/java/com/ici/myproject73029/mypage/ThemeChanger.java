package com.ici.myproject73029.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;

import java.util.Map;
import java.util.Set;

public class ThemeChanger {

    private static SharedPreferences preferences;

    public static void createPreference(Context context) {
        preferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
    }

    public static void themeChange(Activity activity, int theme) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("theme", theme);
        editor.apply();
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        int i = activity.getSharedPreferences("theme", Context.MODE_PRIVATE).getInt("theme", 1);
        Log.d("dddddddddddddddddd", i + " ");
        if (i == 1) {
            activity.setTheme(R.style.AppTheme);
        } else if (i == 2) {
            activity.setTheme(R.style.AppTheme2);
        }
    }

}
