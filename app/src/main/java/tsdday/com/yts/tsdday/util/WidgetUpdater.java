package tsdday.com.yts.tsdday.util;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import tsdday.com.yts.tsdday.ui.widget.BigWidget;

public class WidgetUpdater {

    public static void update(Context context) {
        Intent bigWidget = new Intent(context, BigWidget.class);
        bigWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(bigWidget);
    }
}
