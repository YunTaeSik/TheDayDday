package tsdday.com.yts.tsdday.service;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.util.JobSchedulerStart;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.NotificationCreate;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;
import tsdday.com.yts.tsdday.util.ShowIntent;
import tsdday.com.yts.tsdday.util.ToastMake;

public class ServiceReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ToastMake.make(context, intent.getAction());
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(context.getString(R.string.action_notification_click_phone))) {
                ShowIntent.dial(context, intent.getStringExtra(Keys.NUMBER));
            }
        }
        JobSchedulerStart.start(context);
    }
}

