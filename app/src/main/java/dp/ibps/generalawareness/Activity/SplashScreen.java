package dp.ibps.generalawareness.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;
import dp.ibps.generalawareness.Room.DAO.MainDAOClass;

public class SplashScreen extends AppCompatActivity {
    private TextView taglineTV;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ConstraintLayout splashView;
    private Intent intent;
    private DocumentReference docRef;
    private String deviceID_DB = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "SplashScreen";

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppConstant.ncertHindiModels = Room.databaseBuilder(SplashScreen.this, MainDAOClass.class, AppConstant.hindiNCERT)
                        .build().mainRoomDB().getHindiNCERTDetails();
                AppConstant.ncertEnglishModels = Room.databaseBuilder(SplashScreen.this, MainDAOClass.class, AppConstant.englishNCERT)
                        .build().mainRoomDB().getEnglishNCERTDetails();

                Log.d(TAG, "run: Splash Screen getting size of the list Hindi " +
                        AppConstant.ncertHindiModels.size() +
                        " List size Englih : " + AppConstant.ncertEnglishModels.size());
            }
        }).start();

        splashView = findViewById(R.id.splashView);
        taglineTV = findViewById(R.id.taglineTV);

        String[] taglines = {"आसानी से सीखें", "करियर को समर्पित", "अपना भविष्य यहीं सवारें",
                "संभावनाओं को अनलॉक करें", "अपने जीवन का निर्माण करें", "सबसे उपयोगी",
                "शिक्षा सर्वोपर्य", "हमारा लक्ष्य, आपकी शिक्षा", "आपके लिए खास", "सफलता की उम्मीद में",
                "आपकी शिक्षा को समर्पित", "Daily routine app"};

        try {
            taglineTV.setText(taglines[AppUtils.getRandomNumber(0, taglines.length - 1)]);
            // TODO: 25-03-2021 compare the date of birth with today date to show message
            if (!AppPrefs.getDOB(this).equals("")) {
                if (AppPrefs.getDOB(this).equals(AppUtils.getTodayDate())) {
                    taglineTV.setText(AppPrefs.getUserName(this) + ",\nWish you a very Happy birthday.\nGod Bless You.");
                }
            }
        } catch (Exception e) {
            taglineTV.setText(taglines[AppUtils.getRandomNumber(0, taglines.length - 1)]);
        }

        try {
            docRef = db.collection("userProfileData").document(AppPrefs.getMobile(SplashScreen.this));
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    try {
                        deviceID_DB = documentSnapshot.getString(getResources().getString(R.string.deviceId));
                        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                        if (!deviceID_DB.equals(android_id)) {
                            AppUtils.logoutUser(SplashScreen.this);
                        }
                    } catch (Exception e) {

                    }
                }
            });

        } catch (Exception e) {

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
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
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getResources().getString(R.string.wait_message), Toast.LENGTH_LONG).show();
    }
}