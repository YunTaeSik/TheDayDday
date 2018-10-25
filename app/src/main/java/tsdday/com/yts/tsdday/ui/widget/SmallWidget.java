package tsdday.com.yts.tsdday.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.AppWidgetTarget;
import com.crashlytics.android.Crashlytics;

import io.realm.Realm;
import tsdday.com.yts.tsdday.GlideApp;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.model.Couple;
import tsdday.com.yts.tsdday.ui.activity.IntentStartActivity;
import tsdday.com.yts.tsdday.ui.activity.IntroActivity;
import tsdday.com.yts.tsdday.util.DateFormat;
import tsdday.com.yts.tsdday.util.Keys;

public class SmallWidget extends AppWidgetProvider {
    private static String TAG = "SmallWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.e(TAG, "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, getRemoteView(context, appWidgetId));
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.e(TAG, "onAppWidgetOptionsChanged");
        appWidgetManager.updateAppWidget(appWidgetId, getRemoteView(context, appWidgetId));
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e(TAG, "onReceive");
        ComponentName thisWidget = new ComponentName(context, SmallWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    private static RemoteViews getRemoteView(Context context, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_small);
        try {
            Realm realm = Realm.getDefaultInstance();
            Couple couple = realm.where(Couple.class).findFirst();

            if (couple != null) {
                remoteViews.setTextViewText(R.id.text_ment, couple.getMent());
                remoteViews.setTextViewText(R.id.text_dday, DateFormat.getDdayStringFromCouple(context, couple));

                if (couple.getOneUser() != null && couple.getOneUser().getImageData() != null) {
                    remoteViews.setViewVisibility(R.id.image_one_couple_none, View.GONE);
                }
                if (couple.getTwoUser() != null && couple.getTwoUser().getImageData() != null) {
                    remoteViews.setViewVisibility(R.id.image_two_couple_none, View.GONE);
                }

                /*폰번호 클릭이벤트*/
                Intent phone = new Intent(context, IntentStartActivity.class);
                phone.putExtra(Keys.KIND, Keys.PHONE);
                phone.putExtra(Keys.NUMBER, couple.getNumber());
                PendingIntent phoneIntent = PendingIntent.getActivity(context, 1, phone, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.layout_phone, phoneIntent);

                /*SMS 클릭이벤트*/
                Intent sms = new Intent(context, IntentStartActivity.class);
                sms.putExtra(Keys.KIND, Keys.SMS);
                sms.putExtra(Keys.NUMBER, couple.getNumber());
                PendingIntent smsIntent = PendingIntent.getActivity(context, 2, sms, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.layout_sms, smsIntent);

                AppWidgetTarget oneUserTarget = new AppWidgetTarget(context, R.id.image_one_couple, remoteViews, widgetId);
                GlideApp.with(context).asBitmap().override(120, 120).circleCrop().load(couple.getOneUser().getImageData()).into(oneUserTarget);

                AppWidgetTarget twoUserTarget = new AppWidgetTarget(context, R.id.image_two_couple, remoteViews, widgetId);
                GlideApp.with(context).asBitmap().override(120, 120).circleCrop().load(couple.getTwoUser().getImageData()).into(twoUserTarget);

                AppWidgetTarget coupleBackgroundTarget = new AppWidgetTarget(context, R.id.image_couple_background, remoteViews, widgetId);
                GlideApp.with(context).asBitmap().override(480, 240).centerCrop().load(couple.getBackground()).into(coupleBackgroundTarget);

                /*전체 클릭이벤트*/
                Intent intro = new Intent(context, IntroActivity.class);
                PendingIntent introIntent = PendingIntent.getActivity(context, 0, intro, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.layout_card, introIntent);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }

        return remoteViews;
    }

}
