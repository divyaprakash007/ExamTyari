package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.Fragments.HomeFragment;
import dp.ibps.generalawareness.R;
import okhttp3.internal.Version;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private ImageView profileImg;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private boolean isBackPressed = false;
    private FirebaseFirestore fb;
    private DocumentReference docRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog dialog;
    // TODO: 23-02-2021 Quiz Screen Layout Design
    // TODO: 23-02-2021 mock test screen layout design
    // TODO: 23-02-2021 6 Newspapers Open in WebView
    // TODO: 23-02-2021 Open NCERT Pdf books
    // TODO: 25-02-2021 check if user has not used app from last 7 days
    // TODO: 25-02-2021 fetch news on Air rss and also update firebase db onSuccess or if Internet is connected then get AIR Urls from fb db

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        initialise();
        lastUsingDate();

        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        FragmentManager fragmentManager = getSupportFragmentManager();

        //get fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //set new fragment in fragment_container (FrameLayout)
        fragmentTransaction.replace(R.id.frameLayout, new HomeFragment());
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update:
                        if (AppUtils.isNetworkAvailable(HomeActivity.this)) {
                            startActivity(new Intent(HomeActivity.this, UpdateProfileActivity.class));
                        } else {
                            startActivity(new Intent(HomeActivity.this, NoInternetConnection.class));
                        }
                        break;
                    case R.id.about_us:
                        Intent intent_aboutUs = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent_aboutUs.putExtra(AppConstant.webIntentKey, 0);
                        startActivity(intent_aboutUs);
                        break;
                    case R.id.share_app:
                        AppUtils.shareAppLink(HomeActivity.this);
                        break;
                    case R.id.rate_us:
                        AppUtils.rateUs(HomeActivity.this);
                        break;
//                    case R.id.feedback:
//                        // TODO: 21-03-2021 Need to be active in Next Version
//                        startActivity(new Intent(HomeActivity.this, FeedBackActivity.class));
//                        break;
                    case R.id.privacy_policy:
                        Intent intent_privacyPolicy = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent_privacyPolicy.putExtra(AppConstant.webIntentKey, 1);
                        startActivity(intent_privacyPolicy);
                        break;
                    case R.id.version_code:
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle(getResources().getString(R.string.app_version_msg) + AppPrefs.getVersionCode(HomeActivity.this));
                        builder.setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                        break;
                }
                return true;
            }
        });

        if (!AppPrefs.getLastUsedDate(HomeActivity.this).equalsIgnoreCase(AppUtils.getTodayDate())) {
            try {
                AppUtils.checkVersionUpdate(HomeActivity.this, false);
            } catch (Exception e) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppUtils.checkVersionUpdate(HomeActivity.this, false);
                    }
                }, 60000);
            }
        }

        if (AppPrefs.getProfileImage(HomeActivity.this).length() <= 20) {
            setProfileImage();
        } else {
            byte[] decodedString = Base64.decode(AppPrefs.getProfileImage(HomeActivity.this), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(HomeActivity.this)
                    .load(decodedByte)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(profileImg);
        }

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    private void setProfileImage() {
        String firstChar = "" + AppPrefs.getUserName(HomeActivity.this).charAt(0);
        try {
            docRef = db.collection("userImageData").document(firstChar.toLowerCase());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    try {
                        String imageUrl = documentSnapshot.getString("" + AppUtils.getRandomNumber(0, 3));
                        Glide.with(HomeActivity.this)
                                .load(imageUrl)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(profileImg);
                        AppPrefs.setProfileImage(HomeActivity.this, AppUtils.getByteArrayFromImageURL(imageUrl));
                    } catch (Exception e) {
                        Glide.with(HomeActivity.this)
                                .load(R.mipmap.logo)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(profileImg);
                    }
                }
            });

        } catch (Exception e) {
            Glide.with(HomeActivity.this)
                    .load(R.mipmap.logo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(profileImg);

        }

    }

    private void initialise() {
        fb = FirebaseFirestore.getInstance();
        navigationView = findViewById(R.id.nav_View);
        dialog = new ProgressDialog(HomeActivity.this);
        dialog.setTitle(getResources().getString(R.string.loading_message));
        dialog.setMessage(getResources().getString(R.string.wait_message));
        dialog.setCancelable(false);
        dialog.show();
        View hView = navigationView.getHeaderView(0);
        profileImg = hView.findViewById(R.id.profile_image);
        TextView userName = hView.findViewById(R.id.user_name);
        TextView userMobile = hView.findViewById(R.id.user_mobile_number);
        userName.setText(AppPrefs.getUserName(HomeActivity.this));
        userMobile.setText(AppPrefs.getMobile(HomeActivity.this));
    }

    private void lastUsingDate() {
        if (!AppUtils.getTodayDate().equalsIgnoreCase(AppPrefs.getLastUsedDate(HomeActivity.this))) {
            try {
                DocumentReference documentReference = fb.collection("userProfileData").document(AppPrefs.getMobile(this));
                Map<String, Object> user = new HashMap<>();
                user.put("lastUsedDate", "" + AppUtils.getTodayDate());
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppPrefs.setLastUsedDate(HomeActivity.this, AppUtils.getTodayDate());
                        Log.d(getResources().getString(R.string.tag), "lastUsingDate: " + AppUtils.getTodayDate() + " Pref Date " + AppPrefs.getLastUsedDate(HomeActivity.this));
                    }
                });
            } catch (Exception e) {
                Toast.makeText(this, "Please login to enjoy more features in App.", Toast.LENGTH_LONG).show();
                AppUtils.logoutUser(HomeActivity.this);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.notifications:
                startActivity(new Intent(HomeActivity.this, NotificationsActivity.class));
                break;
            case R.id.check_update:
                AppUtils.checkVersionUpdate(HomeActivity.this, true);
                break;
            case R.id.settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            finish();
        }
        isBackPressed = true;
        Toast.makeText(this, "Please press again to Exit.", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
    }
}