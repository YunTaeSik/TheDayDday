package tsdday.com.yts.tsdday.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import tsdday.com.yts.tsdday.R;

public class ShowIntent {

    public static void imageMultiSelect(Activity context, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(intent, requestCode);
    }

    public static void imageSelect(Activity context, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(intent, requestCode);
    }

    public static void imageCroup(Activity context, Intent data, int requestCode) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(context, R.color.white));
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.colorAccent));
        options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorAccent));
        options.setToolbarTitle(context.getString(R.string.edit_image));

        String fileName = String.valueOf(System.currentTimeMillis()) + ".png";
        UCrop.of(data.getData(), Uri.fromFile(new File(context.getCacheDir(), fileName)))
                .withOptions(options)
                .start(context, requestCode);
    }

    public static void emailSend(Context context) {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        String[] address = {context.getString(R.string.contact_email)};
        email.putExtra(Intent.EXTRA_EMAIL, address);
        email.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.contact_us));
        try {
            context.startActivity(email);
        } catch (Exception e) {
            Crashlytics.logException(e);
            ToastMake.make(context, context.getString(R.string.error_email));
        }
    }

    public static void invite(Context context) {
        Intent intent = new AppInviteInvitation.IntentBuilder(context.getString(R.string.shared_title))
                .setMessage(context.getString(R.string.shared_message))
                .setDeepLink(Uri.parse(context.getString(R.string.invitation_deep_link)))
                .setCallToActionText(context.getString(R.string.shared_call_to_action_text))
                .build();
        ((Activity) context).startActivityForResult(intent, RequestCode.invite);
    }

    public static void reviews(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=tsdday.com.yts.tsdday"));
            context.startActivity(intent);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public static void dial(Context context, String number) {
        if (number == null || number.length() == 0) {
            ToastMake.make(context, context.getString(R.string.hint_number));
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
        ((Activity) context).startActivity(intent);
    }


}
