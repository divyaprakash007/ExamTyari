package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;

public class UpdateProfileActivity extends AppCompatActivity {

    private String android_id;
    private FirebaseFirestore fs_db;
    private static final String TAG = "UpdateProfileActivity";
    private Button updateProfileBtn;
    private TextInputEditText userNameEt, userMobileEt, userPinEt;

    /*
    User Name
    User Mobile number
    User Area Pin Code (Check if User belong from Bareilly District the Show own Interstetial ads on Server update.)
    User Date of Birth (Enable Happy Birthday Message with User Profile Name on Splash Screen if Today match with Birthday)
    User device ID
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setTitle("Update Profile");
        initialise();
        // TODO: 21-03-2021 NextVersion Update user dob all other things with date are tested okay

    }

    private void updateProfileDB() {
        fs_db = FirebaseFirestore.getInstance();
        String mobile = "" + AppPrefs.getMobile(this);
        DocumentReference documentReference = fs_db.collection("userProfileData").document(mobile);

        Map<String, Object> user = new HashMap<>();
        user.put("fName", "" + AppPrefs.getUserName(this));
        user.put("deviceId", "" + android_id);
        user.put("mobile", mobile);
        user.put("pinCode", AppPrefs.getPin(UpdateProfileActivity.this));

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AppPrefs.setProfileImage(UpdateProfileActivity.this, "");
                Toast.makeText(UpdateProfileActivity.this, "Profile Updated.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateProfileActivity.this, HomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Server Error!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateProfileActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void initialise() {
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        userNameEt = findViewById(R.id.user_name_et);
        userMobileEt = findViewById(R.id.user_mobile_et);
        userPinEt = findViewById(R.id.user_pin_et);
        updateProfileBtn = findViewById(R.id.update_profile_btn);
        userMobileEt.setText("+91" + AppPrefs.getMobile(this));

        userPinEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6)
                    AppUtils.hideKeyboard(UpdateProfileActivity.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileBtn.setEnabled(false);
                if (userNameEt.getText().toString().trim().length() < 3) {
                    Toast.makeText(UpdateProfileActivity.this, "Please enter a valid name", Toast.LENGTH_LONG).show();
                } else if (userPinEt.getText().toString().trim().length() < 6) {
                    Toast.makeText(UpdateProfileActivity.this, "Please enter 6 digit valid PIN Code", Toast.LENGTH_LONG).show();
                } else {
                    AppPrefs.setPin(UpdateProfileActivity.this, userPinEt.getText().toString().trim());
                    AppPrefs.setUserName(UpdateProfileActivity.this, userNameEt.getText().toString().trim());
                    updateProfileDB();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateProfileBtn.setEnabled(true);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please update profile.", Toast.LENGTH_LONG).show();
    }
}