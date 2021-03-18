package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;

public class NotificationsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<DocumentSnapshot> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        // TODO: 07-03-2021 working on this screen 
        if (AppUtils.isNetworkAvailable(NotificationsActivity.this)) {
//            db.collection("notifications").document("length").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (int i = 0; i <= Integer.parseInt("" + task.getResult().get("length")); i++) {
//                            Log.d("TAG", "onComplete: length of the list is " + task.getResult().get("length"));
//                            if (i < Integer.parseInt("" + task.getResult().get("length"))) {
//                                db.collection("notifications").document("" + i).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        try {
//                                            if (task.isSuccessful()) {
//                                                notifications.add(task.getResult());
//                                                Toast.makeText(NotificationsActivity.this, "List length " + notifications.size(), Toast.LENGTH_SHORT).show();
//                                                Log.d("TAG", "onComplete: task Length : " + task.getResult().get("date").toString());
//                                            } else {
//                                                Log.d("TAG", "onComplete: Task is failed");
//                                            }
//                                        } catch (Exception e) {
//                                            Toast.makeText(NotificationsActivity.this, "List Complete", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            } else {
//                                Toast.makeText(NotificationsActivity.this, "proceed to recycler view", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    } else {
//
//                    }
//
//                }
//            });
//

        } else {
            startActivity(new Intent(NotificationsActivity.this, NoInternetConnection.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


