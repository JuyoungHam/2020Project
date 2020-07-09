package com.ici.myproject73029.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.internal.firebase_auth.zzff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.MultiFactor;
import com.google.firebase.auth.MultiFactorInfo;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.ici.myproject73029.R;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.List;


public class FirebaseUIActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    View rootView;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google_login;
    private Button logout;
    private TwitterLoginButton twitter_login;
    private LoginButton facebook_login;
    private TextView logintext;
    private ImageView userProfile;
    private CallbackManager facebook_login_manager;
    private boolean isLoggedIn;
    private com.kakao.usermgmt.LoginButton kakao_login;
    private ISessionCallback kakaoSessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_firebase_ui);
        logintext = findViewById(R.id.login_text);
        userProfile = findViewById(R.id.userProfile);

        // Button listeners
        google_login = findViewById(R.id.google_login_button);
        google_login.setOnClickListener(this);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        twitter_login = findViewById(R.id.twitter_login_button);
        twitter_login.setOnClickListener(this);

        facebook_login = findViewById(R.id.facebook_login_button);
        facebook_login.setOnClickListener(this);

        kakao_login = findViewById(R.id.kakao_login_button);
        kakao_login.setOnClickListener(this);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Configure Twitter Sign In
        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);


        //Configure Facebook Sign In
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();
        facebook_login.setReadPermissions("email", "public_profile");
        facebook_login_manager = CallbackManager.Factory.create();
        facebook_signIn();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();


        //Configure Kakao Sign In
        kakaoSessionCallback = new KakaoSessionCallback();
        Session.getCurrentSession().addCallback(kakaoSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        this.rootView = parent;
        return super.onCreateView(parent, name, context, attrs);
    }


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }

        twitter_login.onActivityResult(requestCode, resultCode, data);
        facebook_login_manager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // kakao session
        Session.getCurrentSession().removeCallback(kakaoSessionCallback);
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FirebaseUIActivity.this, "login failed",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void google_signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void google_revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            logintext.setText(user.getDisplayName() + "님, 환영합니다");
            Glide.with(rootView).load(user.getPhotoUrl().toString()).into(userProfile);
            google_login.setVisibility(View.GONE);
            twitter_login.setVisibility(View.GONE);
            facebook_login.setVisibility(View.GONE);
            kakao_login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
        } else {
            logintext.setText("로그인이 필요합니다");
            Glide.with(rootView).load(R.drawable.ic_baseline_perm_identity_24).into(userProfile);
            google_login.setVisibility(View.VISIBLE);
            twitter_login.setVisibility(View.VISIBLE);
            facebook_login.setVisibility(View.VISIBLE);
            kakao_login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.google_login_button) {
            google_signIn();
        } else if (i == R.id.logout) {
            signOut();
        } else if (i == R.id.twitter_login_button) {
            twitter_signIn();
        } else if (i == R.id.facebook_login_button) {
            facebook_signIn();
        }
    }

    private void twitter_signIn() {

        TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(FirebaseUIActivity.this, "Signed in to twitter successful", Toast.LENGTH_LONG).show();
                signInToFirebaseWithTwitterSession(result.data);
                twitter_login.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(FirebaseUIActivity.this, "Login failed. No internet or No Twitter app found on your phone", Toast.LENGTH_LONG).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                updateUI(null);
            }
        });
    }


    private void signInToFirebaseWithTwitterSession(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(FirebaseUIActivity.this, "Signed in firebase twitter successful",
                                Toast.LENGTH_LONG).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(FirebaseUIActivity.this, "Auth firebase twitter failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void facebook_signIn() {
        facebook_login.registerCallback(facebook_login_manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FirebaseUIActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private class KakaoSessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        Log.d("kakao", errorResult.getErrorMessage());
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("kakao", errorResult.getErrorMessage());
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("kakao", errorResult.getErrorMessage());
                }

                @Override
                public void onSuccess(final MeV2Response result) {
                    final UserAccount info = result.getKakaoAccount();
                    FirebaseUser user = new FirebaseUser() {
                        @NonNull
                        @Override
                        public String getUid() {
                            return info.getDisplayId();
                        }

                        @NonNull
                        @Override
                        public String getProviderId() {
                            return null;
                        }

                        @Override
                        public boolean isAnonymous() {
                            return false;
                        }

                        @Nullable
                        @Override
                        public List<String> zza() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public List<? extends UserInfo> getProviderData() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public FirebaseUser zza(@NonNull List<? extends UserInfo> list) {
                            return null;
                        }

                        @Override
                        public FirebaseUser zzb() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public FirebaseApp zzc() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public String getDisplayName() {
                            return result.getNickname();
                        }

                        @Nullable
                        @Override
                        public Uri getPhotoUrl() {
                            return Uri.parse("");
                        }

                        @Nullable
                        @Override
                        public String getEmail() {
                            return info.getEmail();
                        }

                        @Nullable
                        @Override
                        public String getPhoneNumber() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public String zzd() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public zzff zze() {
                            return null;
                        }

                        @Override
                        public void zza(@NonNull zzff zzff) {

                        }

                        @NonNull
                        @Override
                        public String zzf() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public String zzg() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public FirebaseUserMetadata getMetadata() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public MultiFactor getMultiFactor() {
                            return null;
                        }

                        @Override
                        public void zzb(List<MultiFactorInfo> list) {

                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {

                        }

                        @Override
                        public boolean isEmailVerified() {
                            return false;
                        }
                    };
                    updateUI(user);
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("kakao", "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString());
        }
    }


}
