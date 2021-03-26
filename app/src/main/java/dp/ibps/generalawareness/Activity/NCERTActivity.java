package dp.ibps.generalawareness.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.Fragments.EnglishNCERTBooks;
import dp.ibps.generalawareness.Fragments.HindiNCERTBooks;
import dp.ibps.generalawareness.R;

public class NCERTActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_c_e_r_t);
        getSupportActionBar().setTitle("NCERT Books");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("English"));
        tabLayout.addTab(tabLayout.newTab().setText("हिंदी"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount()));

    }

    class MyAdapter extends FragmentPagerAdapter {
        int totalTabs;

        public MyAdapter(FragmentManager fm, int totalTabs) {
            super(fm);
            this.totalTabs = totalTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    HindiNCERTBooks hindiNCERTBooks = new HindiNCERTBooks();
                    return hindiNCERTBooks;
                case 1:
                    EnglishNCERTBooks englishNCERTBooks = new EnglishNCERTBooks();
                    return englishNCERTBooks;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return totalTabs;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.refresh_webView:
                ProgressDialog dialog = new ProgressDialog(NCERTActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Refreshing");
                dialog.setMessage(getResources().getString(R.string.wait_message));
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(NCERTActivity.this, "Books are up to date.", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
                break;
            case R.id.share_app_webView:
                AppUtils.shareAppLink(NCERTActivity.this);
                break;
            case R.id.rate_us_webView:
                AppUtils.rateUs(NCERTActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_page_menu, menu);
        return true;
    }


}