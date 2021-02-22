package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.R;

public class UpdateProfileActivity extends AppCompatActivity {

    private String android_id;
    private FirebaseFirestore fs_db;
    private static final String TAG = "UpdateProfileActivity";
    /*
    User Name
    User Mobile number
    User Email
    User Area Pin Code (Check if User belong from Bareilly District the Show own Interstetial ads on Server update.)
    User Date of Birth (Enable Happy Birthday Message with User Profile Name on Splash Screen if Today match with Birthday)
    User device ID
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        initialise();
        Log.d(TAG, "onCreate: name : " + AppPrefs.getUserName(this) + " Mobile : " + AppPrefs.getMobile(this)
                + " DeviceId : " + android_id + " Pin Code : " + AppPrefs.getPin(this));
        try {
            updateProfileDB();
        } catch (Exception e) {
            Log.d(TAG, "onCreate: Exception " + " Mobile " + AppPrefs.getMobile(this));
        }

    }

    private void updateProfileDB() {
        fs_db = FirebaseFirestore.getInstance();
        String mobile = "" + AppPrefs.getMobile(this);
        DocumentReference documentReference = fs_db.collection("userData").document(mobile);

        Map<String, Object> user = new HashMap<>();

        user.put("fName", "" + AppPrefs.getUserName(this));
        user.put("deviceId", "" + android_id);
        user.put("mobile", mobile);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialise() {
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

    }
}