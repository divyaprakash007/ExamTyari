package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;

public class SplashScreen extends AppCompatActivity {
    private TextView taglineTV;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ConstraintLayout splashView;
    private Intent intent;
    private DocumentReference docRef;
    private String deviceID_DB = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(SplashScreen.this);
            }
        }).start();

        splashView = findViewById(R.id.splashView);
        taglineTV = findViewById(R.id.taglineTV);

        String[] taglines = {"आसानी से सीखें", "करियर को समर्पित", "अपना भविष्य यहीं सवारें",
                "संभावनाओं को अनलॉक करें", "अपने जीवन का निर्माण करें", "सबसे उपयोगी",
                "शिक्षा सर्वोपर्य", "हमारा लक्ष्य, आपकी शिक्षा", "आपके लिए खास", "सफलता की उम्मीद में",
                "आपकी शिक्षा को समर्पित", "Daily routine app"};

        taglineTV.setText(taglines[AppUtils.getRandomNumber(0, taglines.length - 1)]);
        try {
            docRef = db.collection("userData").document(AppPrefs.getMobile(SplashScreen.this));
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    deviceID_DB = documentSnapshot.getString("deviceId");
                    String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    if (!deviceID_DB.equals(android_id)) {
                        AppUtils.logoutUser(SplashScreen.this);
                    }

                }
            });

        } catch (Exception e) {
            Toast.makeText(SplashScreen.this, "Session Logout.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("TAG", "onStart: Unique User name " + AppPrefs.getUserName(this));
        if (!AppUtils.isNetworkAvailable(SplashScreen.this)) {
            intent = new Intent(SplashScreen.this, NoInternetConnection.class);
        } else if (AppPrefs.getMobile(this).equals("")) {
            intent = new Intent(SplashScreen.this, LoginActivity.class);
        } else {
            intent = new Intent(SplashScreen.this, HomeActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 2500);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
    }
}