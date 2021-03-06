package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.ToggleButton;

import com.crashlytics.android.Crashlytics;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.transition.TransitionInflater;

import tsdday.com.yts.tsdday.ui.activity.AlbumLikeListActivity;
import tsdday.com.yts.tsdday.ui.activity.TopBarStyleSelectActivity;
import tsdday.com.yts.tsdday.ui.dialog.PremiumDialog;
import tsdday.com.yts.tsdday.util.JobSchedulerStart;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;
import tsdday.com.yts.tsdday.util.ShowIntent;

public class SettingViewModel extends BaseViewModel {
    public ObservableField<String> version = new ObservableField<>();

    public ObservableBoolean isNotify = new ObservableBoolean(false);
    public ObservableBoolean isTip = new ObservableBoolean(true);
    public ObservableBoolean isPremium = new ObservableBoolean(false);
    public ObservableBoolean isViewFormat = new ObservableBoolean(false);
    public ObservableBoolean isSpecialAnniversaryList = new ObservableBoolean(false);
    public ObservableBoolean isPasswordLock = new ObservableBoolean(false);
    public ObservableBoolean isFingerprintLogin = new ObservableBoolean(false);

    public ObservableBoolean visiblePassword = new ObservableBoolean(false);

    public SettingViewModel(Context context) {
        super(context);
        init();
    }

    public void init() {
        try {
            version.set(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Crashlytics.logException(e);
        }
        visiblePassword.set((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)));
        isNotify.set(SharedPrefsUtils.getBooleanPreference(mContext, Keys.isNotify, false));
        isTip.set(SharedPrefsUtils.getBooleanPreference(mContext, Keys.isTip, true));
        isSpecialAnniversaryList.set(SharedPrefsUtils.getBooleanPreference(mContext, Keys.isSpecialAnniversaryList, true));
        isPasswordLock.set(SharedPrefsUtils.getBooleanPreference(mContext, Keys.isPasswordLock, false));
        isFingerprintLogin.set(SharedPrefsUtils.getBooleanPreference(mContext, Keys.isFingerprintLogin, false));

        boolean isPremium = SharedPrefsUtils.getBooleanPreference(mContext, Keys.isPremium, false);
        boolean isRewardTime = System.currentTimeMillis() - SharedPrefsUtils.getLongPreference(mContext, Keys.isReward, 0) <= 86400000;
        this.isPremium.set(isPremium || isRewardTime);

        boolean isViewFormat = SharedPrefsUtils.getBooleanPreference(mContext, Keys.VIEW_FORMAT_IS_GRID, false);
        this.isViewFormat.set(isViewFormat);
    }

    public void onClickNotify() {
        isNotify.set(!isNotify.get());
        SharedPrefsUtils.setBooleanPreference(mContext, Keys.isNotify, isNotify.get());
        JobSchedulerStart.start(mContext);
        notifyChange();
    }

    public void onClickTip() {
        isTip.set(!isTip.get());
        SharedPrefsUtils.setBooleanPreference(mContext, Keys.isTip, isTip.get());
        notifyChange();
    }

    public void onClickSpecialAnniversaryList() {
        isSpecialAnniversaryList.set(!isSpecialAnniversaryList.get());
        SharedPrefsUtils.setBooleanPreference(mContext, Keys.isSpecialAnniversaryList, isSpecialAnniversaryList.get());
        notifyChange();
    }

    public void onClickPasswordLock() {
        isPasswordLock.set(!isPasswordLock.get());
        SharedPrefsUtils.setBooleanPreference(mContext, Keys.isPasswordLock, isPasswordLock.get());
        notifyChange();
    }

    public void onClickFingerprintLogin() {
        isFingerprintLogin.set(!isFingerprintLogin.get());
        SharedPrefsUtils.setBooleanPreference(mContext, Keys.isFingerprintLogin, isFingerprintLogin.get());
        notifyChange();
    }

    public void onClickPasswordReset() {
    }

    public void onClickAlbumLikeList() {
        Intent like = new Intent(mContext, AlbumLikeListActivity.class);
        mContext.startActivity(like);
    }

    public void onClickTopBarStyle(View view) {
        Intent topbar = new Intent(mContext, TopBarStyleSelectActivity.class);
        view.getContext().startActivity(topbar);
    }

    public void onClickViewFormat(View view) {
        Context context = view.getContext();
        if (view instanceof ToggleButton) {
            ToggleButton toggleButton = (ToggleButton) view;
            SharedPrefsUtils.setBooleanPreference(context, Keys.VIEW_FORMAT_IS_GRID, toggleButton.isChecked());
        }
    }

    public void onClickContactUs() {
        ShowIntent.emailSend(mContext);
    }

    public void onClickReview() {
        ShowIntent.reviews(mContext);
    }

    public void onClickInvite() {
        ShowIntent.invite(mContext);
    }

    public void onClickPremium() {
        PremiumDialog premiumDialog = new PremiumDialog(mContext);
        premiumDialog.show();
    }
}
