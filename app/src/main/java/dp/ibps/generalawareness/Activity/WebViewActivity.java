package dp.ibps.generalawareness.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private String url;
    private String pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialise();
        switch (getIntent().getExtras().getInt(AppConstant.webIntentKey)) {
            case 0:
                url = "file:///android_asset/about_us.html";
                pageTitle = "About Us";
                break;
            case 1:
                url = "file:///android_asset/privacy_policy.html";
                pageTitle = "Privacy Policy";
                break;
            case 2:
                url = "https://www.thehindu.com/";
                pageTitle = "The Hindu";
                break;
            case 3:
                url = "https://indianexpress.com/";
                pageTitle = "The Indian Express";
                break;
            case 4:
                url = "https://www.ndtv.com/video/live/channel/ndtv24x7";
                pageTitle = "NDTV";
                break;
            case 5:
                url = "https://timesofindia.indiatimes.com/home/headlines";
                pageTitle = "Time of India";
                break;
            case 6:
                url = "https://www.hindustantimes.com/";
                pageTitle = "Hindustan Times";
                break;
        }
        getSupportActionBar().setTitle(pageTitle);
        if (AppUtils.isNetworkAvailable(WebViewActivity.this)) {
            webView.loadUrl(url);
        } else {
            startActivity(new Intent(WebViewActivity.this, NoInternetConnection.class));
        }
    }

    private void initialise() {
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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
                webView.loadUrl(url);
                break;
            case R.id.share_app_webView:
                AppUtils.shareAppLink(WebViewActivity.this);
                break;
            case R.id.rate_us_webView:
                AppUtils.rateUs(WebViewActivity.this);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_page_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}