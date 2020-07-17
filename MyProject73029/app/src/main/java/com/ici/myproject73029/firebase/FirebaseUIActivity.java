package com.ici.myproject73029.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ici.myproject73029.Constant;
import com.ici.myproject73029.MainActivity;
import com.ici.myproject73029.R;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.HashMap;
import java.util.Map;


public class FirebaseUIActivity extends AppCompatActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    View rootView;

    private FirebaseAuth mAuth;

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
    private Button unregister;
    private Firebase firebase;
    private FirebaseFirestore db;
    private Button profile_update;
    private FirebaseUser currentUser;
    private String nickname;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("purpose", "login");
                startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firebase_ui);

        firebase = new Firebase();
        db = firebase.startFirebase();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("profile_images");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setTitle("계정 관리");

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        logintext = findViewById(R.id.login_text);
        userProfile = findViewById(R.id.userProfile);
        profile_update = findViewById(R.id.update_profile);
        profile_update.setOnClickListener(this);

        refreshLayout = findViewById(R.id.swipeRefreshLayout_id);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setDistanceToTriggerSync(200);
        final ConstraintLayout constraintLayout = findViewById(R.id.constraintlayout_id);
        refreshLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (constraintLayout.getScrollY() == 0)
                    refreshLayout.setEnabled(true);
                else
                    refreshLayout.setEnabled(false);
            }
        });

        // Button listeners
        google_login = findViewById(R.id.google_login_button);
        google_login.setOnClickListener(this);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);
        unregister = findViewById(R.id.unregister);
        unregister.setOnClickListener(this);

        twitter_login = findViewById(R.id.twitter_login_button);
        twitter_login.setEnabled(true);

        facebook_login = findViewById(R.id.facebook_login_button);
        facebook_login.setOnClickListener(this);

        kakao_login = findViewById(R.id.kakao_login_button);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Configure Twitter Sign In
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

        provider.addCustomParameter("lang", "fr");
        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(mTwitterAuthConfig)
                .debug(true)
                .build();
        Twitter.initialize(twitterConfig);
        twitter_login.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                signInToFirebaseWithTwitterSession(result.data);
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(FirebaseUIActivity.this, R.string.twitter_exception_login_failed, Toast.LENGTH_LONG).show();
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                updateUI(null);
            }
        });


        //Configure Facebook Sign In
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
        currentUser = mAuth.getCurrentUser();
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
                            updateUserDB(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FirebaseUIActivity.this, R.string.login_failed,
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

    public void signOut() {
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

        // Twitter sign out
        TwitterCore.getInstance().getSessionManager().clearActiveSession();

        // Facebook sign out
        LoginManager facebookLoginManager = LoginManager.getInstance();
        facebookLoginManager.logOut();

        // Kakao sign out
        UserManagement kakaoLoginManager = UserManagement.getInstance();
        kakaoLoginManager.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Toast.makeText(FirebaseUIActivity.this, R.string.logout, Toast.LENGTH_SHORT).show();
            }
        });

        userProfile.setImageBitmap(null);
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

    private void updateUI(final FirebaseUser user) {
        if (user != null) {
//                Glide.with(rootView).load(user.getPhotoUrl().toString()).into(userProfile);
            google_login.setVisibility(View.GONE);
            twitter_login.setVisibility(View.GONE);
            facebook_login.setVisibility(View.GONE);
            kakao_login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
            unregister.setVisibility(View.VISIBLE);
            profile_update.setVisibility(View.VISIBLE);

            getUserProfileImage(userProfile);
            logintext.setText(user.getDisplayName() + "님, 환영합니다");

        } else {
            logintext.setText("로그인이 필요합니다");
//            userProfile.setImageBitmap(Bitmap.);
            google_login.setVisibility(View.VISIBLE);
            twitter_login.setVisibility(View.VISIBLE);
            facebook_login.setVisibility(View.VISIBLE);
            kakao_login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
            logout.setEnabled(true);
            unregister.setVisibility(View.GONE);
            profile_update.setVisibility(View.GONE);
            getUserProfileImage(userProfile);
        }
    }

    public void getUserProfileImage(final ImageView userProfile) {
        if (currentUser != null) {
            storageRef.child(currentUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(userProfile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.google_login_button) {
            google_signIn();
        } else if (i == R.id.logout) {
            signOut();
        } else if (i == R.id.facebook_login_button) {
            facebook_signIn();
        } else if (i == R.id.unregister) {
            DialogFragment confirm = new UnregisterConfirmFragment();
            confirm.show(getSupportFragmentManager(), "confirm");
        } else if (i == R.id.update_profile) {
            DialogFragment update = new ProfileUpdateFragment(storage, currentUser);
            update.show(getSupportFragmentManager(), "update");
        }
    }

    private void signInToFirebaseWithTwitterSession(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(FirebaseUIActivity.this, "로그인 성공",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUserDB(user);
                            updateUI(user);
                        } else {
                            Toast.makeText(FirebaseUIActivity.this, R.string.firebase_auth_failed,
                                    Toast.LENGTH_LONG).show();
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
                            updateUserDB(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FirebaseUIActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(FirebaseUIActivity.this, R.string.email_auth_check,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onRefresh() {
        updateUI(currentUser);
        refreshLayout.setRefreshing(false);
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
                    createUserForKakao(info.getEmail());
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("kakao", "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString());
        }
    }

    private void createUserForKakao(final String email) {
        mAuth.createUserWithEmailAndPassword(email, Constant.pwd_kakao)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createUserForKakao", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUserDB(user);
                            updateUI(user);
                        } else {
                            signInForKakao(email);
                        }
                    }
                });
    }

    private void signInForKakao(String email) {
        mAuth.signInWithEmailAndPassword(email, Constant.pwd_kakao)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(FirebaseUIActivity.this,
                                    "로그인 실패",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(FirebaseUIActivity.this,
                                    R.string.email_auth_check,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void unregister() {
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            Toast.makeText(FirebaseUIActivity.this, "탈퇴 완료", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        db.collection("Users").document(currentUser.getUid()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constant.TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constant.TAG, "Error writing document", e);
                    }
                });

        storageRef.child(currentUser.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

        updateUI(null);
    }

    public void updateUserDB(FirebaseUser user) {

        Map<String, Object> data = new HashMap<>();
        data.put("nickname", (user.getDisplayName() != null ? user.getDisplayName() : user.getEmail()));
        data.put("email", user.getEmail());

        db.collection("Users").document(user.getUid()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constant.TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constant.TAG, "Error writing document", e);
                    }
                });
    }

    public void change_profile(final FirebaseUser user, String nickname) {
        UserProfileChangeRequest request =
                new UserProfileChangeRequest.Builder().setDisplayName(nickname).build();
        user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Display name: ", user.getDisplayName());
                } else {
                    Toast.makeText(getApplicationContext(), "닉네임 변경 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateUserDB(user);
    }

}
