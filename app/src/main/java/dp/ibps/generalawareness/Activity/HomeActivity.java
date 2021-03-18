package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
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
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private boolean isBackPressed = false;
    private FirebaseFirestore fb;
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

        navigationView = findViewById(R.id.nav_View);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update:
                        startActivity(new Intent(HomeActivity.this, UpdateProfileActivity.class));
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
                    case R.id.feedback:
                        startActivity(new Intent(HomeActivity.this, FeedBackActivity.class));
                        break;
                    case R.id.privacy_policy:
                        Intent intent_privacyPolicy = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent_privacyPolicy.putExtra(AppConstant.webIntentKey, 1);
                        startActivity(intent_privacyPolicy);
                        break;
                    case R.id.version_code:
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle("Your App Version : " + AppPrefs.getVersionCode(HomeActivity.this));
                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
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

    private void initialise() {
        fb = FirebaseFirestore.getInstance();


    }

    private void lastUsingDate() {
        try {
            DocumentReference documentReference = fb.collection("userData").document(AppPrefs.getMobile(this));
            Map<String, Object> user = new HashMap<>();
            user.put("lastUsedDate", "" + AppUtils.getTodayDate());
            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Please login to enjoy more features in App.", Toast.LENGTH_LONG).show();
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