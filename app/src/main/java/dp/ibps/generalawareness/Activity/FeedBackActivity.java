package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.R;

public class FeedBackActivity extends AppCompatActivity {
    private FirebaseFirestore fs_db;

    // TODO: 23-02-2021 design Update feedback screen also look for unique ticket for user and close issue with proper reply.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initialise();

        // TODO: 23-02-2021 on button press call sendFeedBack method.
        sendFeedBack();

    }

    private void sendFeedBack() {
        String mobile = "" + AppPrefs.getMobile(this);
        DocumentReference documentReference = fs_db.collection("userFeedBack").document(mobile);

        Map<String, Object> user = new HashMap<>();

        user.put("fName", "" + AppPrefs.getUserName(this));
        user.put("message", "User's Feedback Message has been saved.");
        user.put("mobile", mobile);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FeedBackActivity.this, "Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FeedBackActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initialise() {
        fs_db = FirebaseFirestore.getInstance();

    }
}