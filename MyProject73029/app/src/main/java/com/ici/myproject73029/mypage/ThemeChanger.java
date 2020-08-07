package com.ici.myproject73029.mypage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;

import java.util.Map;
import java.util.Set;

public class ThemeChanger {

    private static int sTheme;

    public static void themeChange(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        if (sTheme == 1) {
            activity.setTheme(R.style.AppTheme);
        } else if (sTheme == 2) {
            activity.setTheme(R.style.AppTheme2);
        }
    }

}
