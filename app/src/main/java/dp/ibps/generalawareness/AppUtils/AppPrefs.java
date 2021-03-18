package dp.ibps.generalawareness.AppUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPrefs {
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;


    //    User Profile methods User Name, Mobile Number, Pin Code
    public static String getUserName(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("userName", "Guest User");
    }

    public static void setUserName(Context ctx, String userName) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("userName", userName);
        mPrefsEditor.apply();
    }

    //    User Profile methods User Name, Mobile Number, Pin Code
    public static String getDOB(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("date_of_birth", "");
    }

    public static void setDOB(Context ctx, String date_of_birth) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("date_of_birth", date_of_birth);
        mPrefsEditor.apply();
    }

    public static String getMobile(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("userMobile", "");
    }

    public static void setMobile(Context ctx, String mobile) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("userMobile", mobile);
        mPrefsEditor.apply();
    }

    public static String getPin(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("userPin", "");
    }

    public static void setPin(Context ctx, String pin) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("userPin", pin);
        mPrefsEditor.apply();
    }

    public static String getVersionCode(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("app_version", "1.2.5");
    }

    public static void setVersionCode(Context ctx, String version) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("app_version", version);
        mPrefsEditor.apply();
    }
}
