package tsdday.com.yts.tsdday.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;


import com.bumptech.glide.request.target.NotificationTarget;
import com.crashlytics.android.Crashlytics;


import io.realm.Realm;
import tsdday.com.yts.tsdday.GlideApp;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.model.Couple;
import tsdday.com.yts.tsdday.ui.activity.IntentStartActivity;
import tsdday.com.yts.tsdday.ui.activity.IntroActivity;
import tsdday.com.yts.tsdday.viewmodel.bindingAdapter.ImageBindingAdapter;

public class NotificationCreate {
    private static RemoteViews remoteViews;
    private final static int NOTICATION_ID = 111;

    public static void startAndStop(Context context) {
        boolean isNotify = SharedPrefsUtils.getBooleanPreference(context, Keys.isNotify, false);
        if (isNotify) {
            NotificationCreate.startNotify(context);
        } else {
            NotificationCreate.stopNotify(context);
        }
    }

    public static void reStrart(Context context) {
        boolean isNotify = SharedPrefsUtils.getBooleanPreference(context, Keys.isNotify, false);
        if (isNotify) {
            NotificationCreate.stopNotify(context);
            NotificationCreate.startNotify(context);
        }
    }

    public static void startNotify(Context context) {
        Intent intent = new Intent(context, IntroActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        try {
            Realm realm = Realm.getDefaultInstance();
            Couple couple = realm.where(Couple.class).findFirst();

            int notificationStyle = SharedPrefsUtils.getIntegerPreference(context, Keys.NOTIFICATION_STYLE, 0) == 0 ? R.layout.notification_layout : R.layout.notification_layout_two;
            remoteViews = new RemoteViews(context.getPackageName(), notificationStyle);
            remoteViews.setTextViewText(R.id.text_one_user_name, couple.getOneUser().getName());
            remoteViews.setTextViewText(R.id.text_two_user_name, couple.getTwoUser().getName());
            remoteViews.setTextViewText(R.id.text_couple_content, couple.getMent());
            remoteViews.setTextViewText(R.id.text_dday, DateFormat.getDdayStringFromCouple(context, couple));

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

            if (couple.getOneUser() != null && couple.getOneUser().getImageData() != null) {
                remoteViews.setViewVisibility(R.id.image_one_couple_none, View.GONE);
            }
            if (couple.getTwoUser() != null && couple.getTwoUser().getImageData() != null) {
                remoteViews.setViewVisibility(R.id.image_two_couple_none, View.GONE);
            }
            Notification notification = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                    .setSmallIcon(R.drawable.ic_love_p)
                    .setContentIntent(pendingIntent)
                    .setCustomContentView(remoteViews)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_STATUS)
                    .build();

            notification.flags = Notification.FLAG_NO_CLEAR;

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(NOTICATION_ID, notification);

            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());

            NotificationTarget oneUserTarget = new NotificationTarget(context, R.id.image_one_couple, remoteViews, notification, NOTICATION_ID);
            String oneUserImageDataPath = couple.getOneUser().getImageDataPath();
            byte[] oneUserImageData = couple.getOneUser().getImageData();

            if (oneUserImageDataPath != null && oneUserImageDataPath.length() > 0) {
                GlideApp.with(context.getApplicationContext()).asBitmap().circleCrop().load(oneUserImageDataPath).override(size).into(oneUserTarget);
            } else if (oneUserImageData != null) {
                GlideApp.with(context.getApplicationContext()).asBitmap().circleCrop().load(oneUserImageData).override(size).into(oneUserTarget);
            }


            NotificationTarget twoUserTarget = new NotificationTarget(context, R.id.image_two_couple, remoteViews, notification, NOTICATION_ID);

            String twoUserImageDataPath = couple.getTwoUser().getImageDataPath();
            byte[] twoUserImageData = couple.getTwoUser().getImageData();

            if (twoUserImageDataPath != null && twoUserImageDataPath.length() > 0) {
                GlideApp.with(context.getApplicationContext()).asBitmap().circleCrop().load(twoUserImageDataPath).override(size).into(twoUserTarget);
            } else if (twoUserImageData != null) {
                GlideApp.with(context.getApplicationContext()).asBitmap().circleCrop().load(twoUserImageData).override(size).into(twoUserTarget);
            }

            realm.close();
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }

    public static void stopNotify(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTICATION_ID);
    }

}
