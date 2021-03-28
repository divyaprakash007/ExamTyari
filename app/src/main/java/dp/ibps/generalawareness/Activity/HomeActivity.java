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
    private boolean clicked = true;
    private String[] dataValues = {"Class 6th",
            "Science",
            "Science",
            "https://afeias.com/wp-content/uploads/2019/04/class-6_science_english.pdf"
            ,

            "Class 6th",
            "Social Science",
            "HISTORY (Our Past-I)",
            "https://afeias.com/wp-content/uploads/2019/04/class6_History_english.pdf"
            ,

            "Class 6th",
            "Social Science",
            "GEOGRAPHY (The Earth Our Habitat)",
            "https://afeias.com/wp-content/uploads/2019/04/class6_History_english.pdf"
            ,
            "Class 6th",
            "Social Science",
            "CIVICS (Social and Political Life-I)",
            "https://afeias.com/wp-content/uploads/2019/04/Class-6-GEOGRAPHY-english.pdf"
            ,
            "Class 7th",
            "Science",
            "Science",
            "https://afeias.com/wp-content/uploads/2019/04/class7_science_english.pdf"
            ,
            "Class 7th",
            "Social Science",
            "HISTORY (Our Pal̥st-II)",
            "https://afeias.com/wp-content/uploads/2019/04/class6_history_english.pdf"
            ,
            "Class 7th",
            "Social Science",
            "GEOGRAPHY (Our Environment)",
            "https://afeias.com/wp-content/uploads/2019/04/class6_history_english.pdf"
            ,
            "Class 7th",
            "Social Science",
            "CIVICS (Social and Political Life-II)",
            "https://afeias.com/wp-content/uploads/2019/04/class7_civics_english.pdf"
            ,
            "Class 7th",
            "Science",
            "Science",
            "https://afeias.com/wp-content/uploads/2019/04/Class-8-SCIENCE-english.pdf"
            ,
            "Class 7th",
            "Social Science",
            "HISTORY (Our Pasts-III)",
            "https://afeias.com/wp-content/uploads/2019/04/class8_history_english.pdf"
            ,
            "Class 7th",
            "Social Science",
            "GEOGRAPHY (Resource and Development)",
            "https://afeias.com/wp-content/uploads/2019/04/class8_geography_english.pdf"
            ,
            "Class 7th",
            "Social Science",
            "CIVICS (Social and Political Life-III)",
            "https://afeias.com/wp-content/uploads/2019/04/class8_civics_english.pdf"
            ,
            "Class 8th",
            "Science",
            "Science",
            "https://afeias.com/wp-content/uploads/2019/04/Class-8-SCIENCE-english.pdf"
            ,
            "Class 8th",
            "Social Science",
            "HISTORY (Our Pasts-III)",
            "https://afeias.com/wp-content/uploads/2019/04/class8_history_english.pdf"
            ,
            "Class 8th",
            "Social Science",
            "GEOGRAPHY (Resource and Development)",
            "https://afeias.com/wp-content/uploads/2019/04/class8_geography_english.pdf"
            ,
            "Class 8th",
            "Social Science",
            "CIVICS (Social and Political Life-III)",
            "https://afeias.com/wp-content/uploads/2019/04/class8_civics_english.pdf"
            ,
            "Class 9th",
            "Social Science",
            "HISTORY (India and the Contemporary World-I)",
            "https://afeias.com/wp-content/uploads/2019/04/class9_history_english_.pdf"
            ,
            "Class 9th",
            "Social Science",
            "GEOGRAPHY (Contemporary India-I)",
            "https://afeias.com/wp-content/uploads/2019/04/class9_geography_english.pdf"
            ,
            "Class 9th",
            "Social Science",
            "CIVICS (Democratic Politics)",
            "https://afeias.com/wp-content/uploads/2019/04/class9_civics_english.pdf"
            ,
            "Class 9th",
            "Social Science",
            "ECONOMICS",
            "https://afeias.com/wp-content/uploads/2019/04/class9_econimics_english.pdf"
            ,
            "Class 10th",
            "Science",
            "Science",
            "https://afeias.com/wp-content/uploads/2019/04/class10_science_hindi.pdf"
            ,
            "Class 10th",
            "Social Science",
            "HISTORY (India and the Contemporary World-II)",
            "https://afeias.com/wp-content/uploads/2019/04/class10_history_english.pdf"
            ,
            "Class 10th",
            "Social Science",
            "GEOGRAPHY (Contemporary India-II)",
            "https://afeias.com/wp-content/uploads/2019/04/Class-10-GEOGRAPHY-english.pdf"
            ,
            "Class 10th",
            "Social Science",
            "CIVICS (Democratic Politics-II)",
            "https://afeias.com/wp-content/uploads/2019/04/class10_civics_english.pdf"
            ,
            "Class 10th",
            "Social Science",
            "ECONOMICS (Understanding Economic Development)",
            "https://afeias.com/wp-content/uploads/2019/04/class10_economics_english.pdf"
            ,
            "Class 11th",
            "Sociology",
            "Introducing Sociology",
            "https://afeias.com/wp-content/uploads/2019/04/class11_Introducing-Sociology.pdf"
            ,
            "Class 11th",
            "Sociology",
            "Understanding the Society",
            "https://afeias.com/wp-content/uploads/2019/04/Class-11-UNDERSTANDING-SOCIETY-english.pdf"
            ,
            "Class 11th",
            "Political Science",
            "Political Theory",
            "https://afeias.com/wp-content/uploads/2019/04/Class-11-POLITICAL-THEORY-english.pdf"
            ,
            "Class 11th",
            "Political Science",
            "Political Theory",
            "https://afeias.com/wp-content/uploads/2019/04/Class-11-POLITICAL-THEORY-english.pdf"
            ,
            "Class 11th",
            "Political Science",
            "Indian Constitution at Work",
            "https://afeias.com/wp-content/uploads/2019/04/class11_Indian-Constitution-at-Work.pdf"
            ,
            "Class 11th",
            "History",
            "Themes in World History",
            "https://afeias.com/wp-content/uploads/2019/04/class11_history_english.pdf"
            ,
            "Class 11th",
            "Geography",
            "Fundamentals of Physical Geography",
            "https://afeias.com/wp-content/uploads/2019/04/Class-11-FUNDAMENTALS-OF-PHYSICAL-GEOGRAPHYenglish.pdf"
            ,
            "Class 11th",
            "Geography",
            "India: Physical Environment",
            "https://afeias.com/wp-content/uploads/2019/04/class11_India-Physical-Environment.pdf"
            ,
            "Class 11th",
            "Economics",
            "Indian Economic Development",
            "https://afeias.com/wp-content/uploads/2019/04/class11_economics_english.pdf"
            ,
            "Class 12th",
            "History",
            "Themes in Indian History-I",
            "https://afeias.com/wp-content/uploads/2019/04/class12_Themes-in-Indian-History-I.pdf"
            ,
            "Class 12th",
            "History",
            "Themes in Indian History-II",
            "https://afeias.com/wp-content/uploads/2019/04/class12_Themes-in-Indian-History-II.pdf"
            ,
            "Class 12th",
            "History",
            "Themes in Indian History-III",
            "https://afeias.com/wp-content/uploads/2019/04/class12_Themes-in-Indian-History-III.pdf"
            ,
            "Class 12th",
            "Geography",
            "Fundamentals of Human Geography",
            "https://afeias.com/wp-content/uploads/2019/04/Class-12-FUNDAMENTALS-OF-HUMAN-GEOGRAPHY-english.pdf"
            ,
            "Class 12th",
            "Geography",
            "Fundamentals of Human Geography",
            "https://afeias.com/wp-content/uploads/2019/04/Class-12-FUNDAMENTALS-OF-HUMAN-GEOGRAPHY-english.pdf"
            ,
            "Class 12th",
            "Geography",
            "India: People and Economy",
            "https://afeias.com/wp-content/uploads/2019/04/class12_India-People-and-Economy.pdf"
            ,
            "Class 12th",
            "Sociology",
            "Indian Society",
            "https://afeias.com/wp-content/uploads/2019/04/Class-12-INDIAN-SOCIETY-english.pdf"
            ,
            "Class 12th",
            "Sociology",
            "Social Change and Development in India",
            "https://afeias.com/wp-content/uploads/2019/04/Class-12-SOCIAL-CHANGE-AND-DEVELOPMENT-IN-INDIA-english.pdf"
            ,
            "Class 12th",
            "Political Science",
            "Contemporary World Politics",
            "https://afeias.com/wp-content/uploads/2019/04/Class-12-CONTEMPORARY-WORLD-POLITICS-english.pdf"
            ,
            "Class 12th",
            "Political Science",
            "Politics in India since Independence",
            "https://afeias.com/wp-content/uploads/2019/04/class12_Politics-in-India-since-Independence.pdf"
            ,
            "Class 12th",
            "Economics",
            "Introductory Microeconomics",
            "https://afeias.com/wp-content/uploads/2019/04/Class-12-INTRODUCTORY-MICROECONOMICS-english.pdf"
            ,
            "Class 12th",
            "Economics",
            "Introductory Macroeconomics",
            "https://afeias.com/wp-content/uploads/2019/04/Class-12-INTRODUCTORY-MACROECONOMICS-english.pdf"
    };

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
        if (clicked) {
            clicked = false;
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

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clicked = true;
                }
            }, 1000);

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