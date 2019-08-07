package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;

import androidx.databinding.ObservableField;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;

import tsdday.com.yts.tsdday.BaseActivity;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.interactor.OnDismiss;
import tsdday.com.yts.tsdday.ui.activity.MainActivity;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;
import tsdday.com.yts.tsdday.util.ToastMake;

public class PremiumViewModel extends BaseViewModel implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    private AdRequest adRequest;
    private OnDismiss onDismiss;
    private BillingFlowParams billingFlowParams;

    public ObservableField<String> mPrice = new ObservableField<>("3000");

    public PremiumViewModel(Context context) {
        super(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) mContext;
            mRewardedVideoAd = activity.mRewardedVideoAd;
            adRequest = activity.adRequest;
            mRewardedVideoAd.setRewardedVideoAdListener(this);
        }
        init();

    }

    public void setOnDismiss(OnDismiss onDismiss) {
        this.onDismiss = onDismiss;
    }

    private void init() {
        List skuList = new ArrayList<>();
        skuList.add("premium_upgrade");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(),
                (responseCode, skuDetailsList) -> {
                    if (skuDetailsList != null) {
                        for (SkuDetails skuDetails : skuDetailsList) {
                            String sku = skuDetails.getSku();
                            String price = skuDetails.getPrice();
                            if ("premium_upgrade".equals(sku)) {
                                billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetails)
                                        // .setSku("premium_upgrade")
                                        //.setType(BillingClient.SkuType.INAPP) // SkuType.SUB for subscription
                                        .build();
                                mPrice.set(price);
                            }
                        }
                    }
                });
    }

    public void onClickPrimenum() {
        if (mBillingClient == null) {
            ToastMake.make(mContext,R.string.error_default);
            return;
        }
        mBillingClient.launchBillingFlow((BaseActivity) mContext, billingFlowParams);
   /*     if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).changePrimenum();
        }*/
        if (onDismiss != null) {
            onDismiss.onDismiss();
        }
    }

    public void onClickVideoAd() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }


    /**
     * 광고 리스너
     */
    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        mRewardedVideoAd.loadAd(mContext.getString(R.string.reward_ad_id), adRequest);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        SharedPrefsUtils.setLongPreference(mContext, Keys.isReward, System.currentTimeMillis());
        if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).changePrimenum();
        }
        if (onDismiss != null) {
            onDismiss.onDismiss();
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
