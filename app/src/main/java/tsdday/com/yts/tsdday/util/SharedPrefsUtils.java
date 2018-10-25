package tsdday.com.yts.tsdday.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefsUtils {

    public static String getStringPreference(Context context, String key) {                                     ///스트링값 얻어오기 위한 함수
        String value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    public static boolean setStringPreference(Context context, String key, String value) {              ///스트링값 설정하기 위한 함수
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public static int getIntegerPreference(Context context, String key, int defaultValue) {              ///인트값 불러오기 위한 함수
        int value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue);
        }
        return value;
    }

    public static boolean setIntegerPreference(Context context, String key, int value) {                ///인트값 설정하기 위한 함수
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    public static boolean setLoginPreference(Context context, String id, String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(id)) {
            SharedPreferences.Editor editor = preferences.edit();
            Set<String> myArrayList = new HashSet<String>();
            myArrayList.add(id);
            myArrayList.add(password);

            editor.putStringSet(id, myArrayList);

            return editor.commit();
        }
        return false;

    }

    public static Set<String> getLoginPreference(Context context, String id) {                                     ///스트링값 얻어오기 위한 함수
        Set<String> myArrayList = new HashSet<String>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            myArrayList = preferences.getStringSet(id, null);
        }
        return myArrayList;
    }

    public static boolean getBooleanPreference(Context context, String key, boolean defaultValue) {                                     ///스트링값 얻어오기 위한 함수
        Boolean value = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue);
        }
        return value;
    }

    public static boolean setBooleanPreference(Context context, String key, boolean value) {                ///인트값 설정하기 위한 함수
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }


    public static boolean setLongPreference(Context context, String key, long value) {              ///스트링값 설정하기 위한 함수
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    public static long getLongPreference(Context context, String key, long defaultValue) {              ///인트값 불러오기 위한 함수
        long value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue);
        }
        return value;
    }

}
