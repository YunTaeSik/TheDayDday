package tsdday.com.yts.tsdday.util;

import com.google.android.material.snackbar.Snackbar;
import android.view.View;

public class SnackbarMake {

    public static void make(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}
