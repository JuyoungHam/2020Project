package com.ici.myproject73029;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class Constant {
    public static final String TAG = "FirebaseLog";

    //공연 카테고리를 숫자로 표현함
    public static final int EXHIBITION = 1111;
    public static final int SHOW = 2222;
    public static final int REVIEW = 3333;
    public static final int FAVORITE = 4444;
    public static final int MYREVIEWPAGE = 5555;

    public static final int RC_SIGN_IN = 9001;

    public static final String GOOGLE_AUTH = "google";
    public static final String TWITTER_AUTH = "twitter";
    public static final String FACEBOOK_AUTH = "facebook";
    public static final String KAKAO_AUTH = "kakao";

    public static final int LOGIN_REQUEST_FROM_MYPAGE = 8921;

    public static final String pwd_kakao = "kakaopasswordformyproject73029!";

    public static final String MYFAVORITE = "favorite";
    public static final String MYPAGE = "mypage";
    public static final String MYREVIEW = "review";

    public static final String AUTHTOMYPAGE = "BACKTOMYPAGE";
    public static final String USERID = "USERID";
    public static final String USERNAME = "USERNAME";

    public static final String FAVORITE_COUNT = "favorite_count";

    public static final String[] EXHIBITION_TAGS = {"모든 장르", "어린이"};
    public static final String[] SHOW_TAGS = {"모든 장르", "어린이", "연극", "뮤지컬", "국악", "클래식"};
    public static final String[] REVIEW_LIST_SPINNER = {"최신순", "오래된 순", "좋아요 순", "별점 높은 순", "별점 낮은 순"};


    //How to get hashcode
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("MainActivity", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}
