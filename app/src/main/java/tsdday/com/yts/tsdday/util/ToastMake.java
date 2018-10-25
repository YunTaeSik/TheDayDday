package tsdday.com.yts.tsdday.util;

import android.content.Context;
import android.widget.Toast;

public class ToastMake {
    public static void make(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void make(Context context, int id) {
        Toast.makeText(context, context.getString(id), Toast.LENGTH_SHORT).show();
    }

    public static void error(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    public static void tip(Context context, String text) {
        boolean visible = SharedPrefsUtils.getBooleanPreference(context, Keys.isTip, true);
        if (visible) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }


}
