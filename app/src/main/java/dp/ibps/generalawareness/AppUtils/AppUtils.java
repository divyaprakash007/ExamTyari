package dp.ibps.generalawareness.AppUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

import dp.ibps.generalawareness.Activity.HomeActivity;
import dp.ibps.generalawareness.R;

public class AppUtils {

    public static void checkVersionUpdate(Context context, boolean progressStatus) {
        ProgressDialog dialogStatus = new ProgressDialog(context);
        if (progressStatus) {
            dialogStatus.setTitle("Please wait");
            dialogStatus.setMessage("Looking for updates.");
            dialogStatus.setCancelable(false);
            dialogStatus.show();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("metaData").document("AppDetails").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (dialogStatus.isShowing()) {
                    dialogStatus.dismiss();
                }
                if (task.isSuccessful()) {
                    String versionName;
                    try {
                        PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        versionName = (pinfo.versionName).trim();
                    } catch (Exception e) {
                        versionName = (AppPrefs.getVersionCode(context)).trim();
                    }

                    if (!versionName.equals("" + task.getResult().get("Version"))) {
                        Log.d("TAG", "onComplete: Version details : " + versionName + " Version Details : " + task.getResult().get("Version"));
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Version Update : " + task.getResult().get("Version"));
                        builder.setMessage(context.getResources().getString(R.string.app_name) + " has a new Version Update.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    } else {
                        if (progressStatus)
                            Toast.makeText(context, "You have latest version.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static int getRandomNumber(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static void rateUs(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + "com.android.chrome")));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=dp.ibps.generalawareness")));
        }
    }

    public static void shareAppLink(Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareMessage = "Download this Application for Free Mock Tests https://play.google.com/store/apps/details?id=dp.ibps.generalawareness&hl=en";
        String shareSubject = context.getResources().getString(R.string.app_name);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        context.startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }

    public static void logoutUser(Context context) {
        AppPrefs.setUserName(context, "");
        AppPrefs.setMobile(context, "");
        AppPrefs.setPin(context, "");
    }


}
