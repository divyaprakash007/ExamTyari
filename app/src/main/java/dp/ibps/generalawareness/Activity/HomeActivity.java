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
import androidx.room.Room;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.Fragments.HomeFragment;
import dp.ibps.generalawareness.R;
import dp.ibps.generalawareness.Room.DAO.MainDAOClass;
import dp.ibps.generalawareness.Room.Model.NCERTEnglishModel;
import dp.ibps.generalawareness.Room.Model.NCERTHindiModel;

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

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: insstance Id : " + task.getResult().getToken());
                        } else {
                            Log.d(TAG, "onComplete: insstance Id : " + task.getException().getMessage());
                        }
                    }
                });

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

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConstant.ncertHindiList.size() <= 0) {
            getNCERTDBSavetoRoom(AppConstant.hindiNCERT);
        } else if (AppConstant.ncertEnglishList.size() <= 0) {
            getNCERTDBSavetoRoom(AppConstant.englishNCERT);
        } else {
            Log.d(TAG, "onResume: List sizes are from room db " + AppConstant.ncertEnglishList.size() + " " + AppConstant.ncertHindiList.size());
        }
    }

    private void getNCERTDBSavetoRoom(String tableName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(tableName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onResume();
                        }
                    }, 1000);
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            MainDAOClass mainDAOClass = Room.databaseBuilder(HomeActivity.this, MainDAOClass.class, tableName)
                                    .build();
                            if (tableName.equals(AppConstant.hindiNCERT)) {
                                mainDAOClass.mainRoomDB().deleteAllHindiNCERT();
                                AppConstant.ncertHindiList.clear();
                                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                                    NCERTHindiModel ncertHindiModel = new NCERTHindiModel("" + queryDocumentSnapshots.getDocuments().get(i).get("class"),
                                            "" + queryDocumentSnapshots.getDocuments().get(i).get("bookName"),
                                            "" + queryDocumentSnapshots.getDocuments().get(i).get("sub"),
                                            "" + queryDocumentSnapshots.getDocuments().get(i).get("url"));
                                    mainDAOClass.mainRoomDB().hindiBooksDetailsInsert(ncertHindiModel);
                                    AppConstant.ncertHindiList.add(ncertHindiModel);
                                }

                            } else {
                                mainDAOClass.mainRoomDB().deleteAllEnglishNCERT();
                                AppConstant.ncertEnglishList.clear();
                                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                                    NCERTEnglishModel ncertEnglishModel = new NCERTEnglishModel("" + queryDocumentSnapshots.getDocuments().get(i).get("class"),
                                            "" + queryDocumentSnapshots.getDocuments().get(i).get("bookName"),
                                            "" + queryDocumentSnapshots.getDocuments().get(i).get("sub"),
                                            "" + queryDocumentSnapshots.getDocuments().get(i).get("url"));
                                    mainDAOClass.mainRoomDB().englishBooksDetailsInsert(ncertEnglishModel);
                                    AppConstant.ncertEnglishList.add(ncertEnglishModel);
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onResume();
                                }
                            });
                        }
                    }.start();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AppUtils.sendErrorMessage(HomeActivity.this, AppUtils.getTodayDate(), e.getMessage(), "", "HomeActivity");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onResume();
                    }
                }, 1000);

            }
        });
    }
}

