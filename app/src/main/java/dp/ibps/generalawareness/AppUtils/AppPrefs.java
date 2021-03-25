package dp.ibps.generalawareness.AppUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dp.ibps.generalawareness.R;

public class AppPrefs {
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;

    //    User Profile methods User Name, Mobile Number, Pin Code
    public static String getUserName(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString(ctx.getResources().getString(R.string.userName), "Guest User");
    }

    public static void setUserName(Context ctx, String userName) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString(ctx.getResources().getString(R.string.userName), userName);
        mPrefsEditor.apply();
    }

    //    User Profile methods User Name, Mobile Number, Pin Code
    public static String getDOB(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString(ctx.getResources().getString(R.string.date_of_birth), "");
    }

    public static void setDOB(Context ctx, String date_of_birth) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString(ctx.getResources().getString(R.string.date_of_birth), date_of_birth);
        mPrefsEditor.apply();
    }

    public static String getMobile(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString(ctx.getResources().getString(R.string.userMobile), "");
    }

    public static void setMobile(Context ctx, String mobile) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString(ctx.getResources().getString(R.string.userMobile), mobile);
        mPrefsEditor.apply();
    }

    public static String getPin(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString(ctx.getResources().getString(R.string.userPin), "");
    }

    public static void setPin(Context ctx, String pin) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString(ctx.getResources().getString(R.string.userPin), pin);
        mPrefsEditor.apply();
    }

    public static String getVersionCode(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString(ctx.getResources().getString(R.string.app_version), "1.2.5");
    }

    public static void setVersionCode(Context ctx, String version) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString(ctx.getResources().getString(R.string.app_version), version);
        mPrefsEditor.apply();
    }

    public static String getProfileImage(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString(ctx.getResources().getString(R.string.profileImg), "");
    }

    public static void setProfileImage(Context ctx, String profileImage) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString(ctx.getResources().getString(R.string.profileImg), profileImage);
        mPrefsEditor.apply();
    }

    public static String getLastUsedDate(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString(ctx.getResources().getString(R.string.lastDate), "");
    }

    public static void setLastUsedDate(Context ctx, String lastUsedDate) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString(ctx.getResources().getString(R.string.lastDate), lastUsedDate);
        mPrefsEditor.apply();
    }
}
