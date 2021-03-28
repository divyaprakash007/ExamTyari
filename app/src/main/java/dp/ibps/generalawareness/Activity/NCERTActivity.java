package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

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

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFragment(new HindiNCERTBooks(), "हिंदी");
        adapter.addFragment(new EnglishNCERTBooks(), "English");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);


//        tabLayout.addTab(tabLayout.newTab().setText("English"));
//        tabLayout.addTab(tabLayout.newTab().setText("हिंदी"));
//
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),
//                2));
//        viewPager.setCurrentItem(0);

    }

    class MainAdapter extends FragmentPagerAdapter {
        private ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            arrayList.add(title);
            fragmentList.add(fragment);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
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
//                dialog.show();
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