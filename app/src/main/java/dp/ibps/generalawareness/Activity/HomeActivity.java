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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.Fragments.HomeFragment;
import dp.ibps.generalawareness.R;

public class HomeActivity extends AppCompatActivity {
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_View);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        getSupportFragmentManager().beginTransaction().add(R.id.nav_View,HomeFragment.newInstance(),null).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.update:
                        startActivity(new Intent(HomeActivity.this, UpdateProfileActivity.class));
                        break;
                    case R.id.about_us:
                        Intent intent_abousUs = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent_abousUs.putExtra(AppConstant.webIntentKey, 0);
                        startActivity(intent_abousUs);
                        break;
                    case R.id.share_app:
//                        Share app link using default apps
                        AppUtils.shareAppLink(HomeActivity.this);
                        break;
                    case R.id.rate_us:
                        // Switch user to PlayStore (Chrome, App)
                        AppUtils.rateUs(HomeActivity.this);
                        break;
                    case R.id.feedback:
                        // TODO: 13-02-2021 switch to feedback screen and store user's feedback with date mobile number and proper message. 
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
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

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
                // TODO: 15-02-2021 Check for play store version update fetched from Firebase 
                break;
            case R.id.settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}