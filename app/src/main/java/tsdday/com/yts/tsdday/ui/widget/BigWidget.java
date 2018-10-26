package tsdday.com.yts.tsdday.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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

public class BigWidget extends AppWidgetProvider {
    private static String TAG = "BigWidget";

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
        ComponentName thisWidget = new ComponentName(context, BigWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    private static RemoteViews getRemoteView(Context context, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_big);
        try {
            Realm realm = Realm.getDefaultInstance();
            Couple couple = realm.where(Couple.class).findFirst();

            remoteViews.setTextViewText(R.id.text_one_user_name, couple.getOneUser().getName());
            remoteViews.setTextViewText(R.id.text_two_user_name, couple.getTwoUser().getName());
            remoteViews.setTextViewText(R.id.text_ment, couple.getMent());
            remoteViews.setTextViewText(R.id.text_dday, DateFormat.getDdayStringFromCouple(context, couple));
            remoteViews.setTextViewText(R.id.text_start_date, couple.getStartDate());

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

            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());

            AppWidgetTarget oneUserTarget = new AppWidgetTarget(context, R.id.image_one_couple, remoteViews, widgetId);
            String oneUserImageDataPath = couple.getOneUser().getImageDataPath();
            byte[] oneUserImageData = couple.getOneUser().getImageData();
            if (oneUserImageDataPath != null && oneUserImageDataPath.length() > 0) {
                GlideApp.with(context).asBitmap().circleCrop().load(oneUserImageDataPath).override(size).into(oneUserTarget);
            } else if (oneUserImageData != null) {
                GlideApp.with(context).asBitmap().circleCrop().load(oneUserImageData).override(size).into(oneUserTarget);
            }

            AppWidgetTarget twoUserTarget = new AppWidgetTarget(context, R.id.image_two_couple, remoteViews, widgetId);
            String twoUserImageDataPath = couple.getTwoUser().getImageDataPath();
            byte[] twoUserImageData = couple.getTwoUser().getImageData();
            if (twoUserImageDataPath != null && twoUserImageDataPath.length() > 0) {
                GlideApp.with(context).asBitmap().circleCrop().load(twoUserImageDataPath).override(size).into(twoUserTarget);
            } else if (twoUserImageData != null) {
                GlideApp.with(context).asBitmap().circleCrop().load(twoUserImageData).override(size).into(twoUserTarget);
            }

            AppWidgetTarget coupleBackgroundTarget = new AppWidgetTarget(context, R.id.image_couple_background, remoteViews, widgetId);
            String coupleBackgroundPath = couple.getBackgroundPath();
            byte[] coupleBackground = couple.getBackground();
            if (coupleBackgroundPath != null && coupleBackgroundPath.length() > 0) {
                GlideApp.with(context).asBitmap().override(480, 240).centerCrop().load(coupleBackgroundPath).into(coupleBackgroundTarget);
            } else if (coupleBackground != null) {
                GlideApp.with(context).asBitmap().override(480, 240).centerCrop().load(coupleBackground).into(coupleBackgroundTarget);
            }


            /*전체 클릭이벤트*/
            Intent intro = new Intent(context, IntroActivity.class);
            PendingIntent introIntent = PendingIntent.getActivity(context, 0, intro, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.layout_card, introIntent);

        } catch (Exception e) {
            Crashlytics.logException(e);
        }

        return remoteViews;
    }

}
