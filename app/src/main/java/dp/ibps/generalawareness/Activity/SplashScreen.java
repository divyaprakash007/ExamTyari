package dp.ibps.generalawareness.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;

public class SplashScreen extends AppCompatActivity {
    private TextView taglineTV;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ConstraintLayout splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashView = findViewById(R.id.splashView);
        taglineTV = findViewById(R.id.taglineTV);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(SplashScreen.this);
            }
        }).start();

        String[] taglines = {"आसानी से सीखें", "करियर को समर्पित", "अपना भविष्य यहीं सवारें",
                "संभावनाओं को अनलॉक करें", "अपने जीवन का निर्माण करें", "सबसे उपयोगी",
                "शिक्षा सर्वोपर्य", "हमारा लक्ष्य, आपकी शिक्षा", "आपके लिए खास", "सफलता की उम्मीद में",
                "आपकी शिक्षा को समर्पित", "Daily routine app"};

        taglineTV.setText(taglines[AppUtils.getRandomNumber(0, taglines.length - 1)]);

//        if (AppPrefs.getPin(this).equals("")) {
//            intent = new Intent(SplashScreen.this, UpdateProfileActivity.class);
//        } else {
//            intent = new Intent(SplashScreen.this, HomeActivity.class);
//        }

        intent = new Intent(SplashScreen.this, HomeActivity.class);
        new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(intent);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        
    }
}