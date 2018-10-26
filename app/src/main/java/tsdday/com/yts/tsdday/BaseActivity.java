package tsdday.com.yts.tsdday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.inputmethod.InputMethodManager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import tsdday.com.yts.tsdday.interactor.OnPremiumInspection;
import tsdday.com.yts.tsdday.ui.dialog.AlbumDialog;
import tsdday.com.yts.tsdday.ui.dialog.DateSelectDialog;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SendBroadcast;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;

public class BaseActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    public BillingClient mBillingClient;

    public FragmentTransaction fragmentTransaction;
    public CompositeDisposable compositeDisposable;
    public GlideRequests glide;
    public InputMethodManager inputMethodManager;

    public AdRequest adRequest;
    public InterstitialAd interstitialAd;
    public RewardedVideoAd mRewardedVideoAd;

    public OnPremiumInspection onPremiumInspection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setBillingClient();
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        compositeDisposable = new CompositeDisposable();
        glide = GlideApp.with(this);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        adRequest = new AdRequest.Builder().addTestDevice(getString(R.string.test_device)).addTestDevice("23010E79EC3D4C1D630FCBDDD4DC524D").build();
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.screen_ad_id));
        interstitialAd.loadAd(adRequest);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.loadAd(getString(R.string.reward_ad_id), adRequest);

        registerReceiver(broadcastReceiver, getIntentFilter());
    }

    public void setOnPremiumInspection(OnPremiumInspection onPremiumInspection) {
        this.onPremiumInspection = onPremiumInspection;
    }

    public void initPrimeum() {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (purchasesResult != null && purchasesResult.getPurchasesList() != null) {
            for (Purchase purchase : purchasesResult.getPurchasesList()) {
                if (purchase.getSku().equals("premium_upgrade")) {
                    SharedPrefsUtils.setBooleanPreference(getApplicationContext(), Keys.isPremium, true);
                    if (onPremiumInspection != null) {
                        onPremiumInspection.onPremiumInspection();
                    }
                }
            }
        }
        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP,
                new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(@BillingClient.BillingResponse int responseCode,
                                                          List<Purchase> purchasesList) {
                        if (responseCode == BillingClient.BillingResponse.OK
                                && purchasesList != null) {
                            for (Purchase purchase : purchasesList) {
                                if (purchase.getSku().equals("premium_upgrade")) {
                                    SharedPrefsUtils.setBooleanPreference(getApplicationContext(), Keys.isPremium, true);
                                    if (onPremiumInspection != null) {
                                        onPremiumInspection.onPremiumInspection();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public boolean isPrimeum() {
        boolean isPremium = SharedPrefsUtils.getBooleanPreference(this, Keys.isPremium, false);
        boolean isRewardTime = System.currentTimeMillis() - SharedPrefsUtils.getLongPreference(this, Keys.isReward, 0) <= 86400000;
        return isPremium || isRewardTime;
    }


    public void startFragmentDialog(DialogFragment dialogFragment, int transitionId) {
        try {
            String fragmentTag = dialogFragment.getClass().getSimpleName();

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogFragment.setEnterTransition(android.transition.TransitionInflater.from(this).inflateTransition(transitionId));
            }
            fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setReorderingAllowed(true)
                    .addToBackStack(fragmentTag)
                    .replace(android.R.id.content, dialogFragment)
                    .commit();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public void hideKeyboard() {
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Keys.SEND_CLOSE)) {
                    onBackPressed();
                } else if (action.equals(Keys.SEND_HIDE_KEYBOARD)) {
                    hideKeyboard();
                } else if (action.equals(Keys.SEND_START_ALBUM)) {
                    SendBroadcast.stopLottieAnimationAlbumEmpty(getApplicationContext());

                    String date = intent.getStringExtra(Keys.DATE);
                    startFragmentDialog(AlbumDialog.newInstance(date), android.R.transition.slide_bottom);
                } else if (action.equals(Keys.SEND_START_DATE_DIALOG)) {
                    String date = intent.getStringExtra(Keys.DATE);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DateSelectDialog dialog = DateSelectDialog.newInstance(date);
                    dialog.show(fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK), null);
                }
            }
        }
    };

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Keys.SEND_CLOSE);
        intentFilter.addAction(Keys.SEND_HIDE_KEYBOARD);
        intentFilter.addAction(Keys.SEND_START_ALBUM);
        intentFilter.addAction(Keys.SEND_START_DATE_DIALOG);
        return intentFilter;
    }


    private void setBillingClient() {
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }


    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        //   ToastMake.make(this, "결제 시도");
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getSku().equals("premium_upgrade")) {
                    SharedPrefsUtils.setBooleanPreference(this, Keys.isPremium, true);
                    if (onPremiumInspection != null) {
                        onPremiumInspection.onPremiumInspection();
                    }
                }
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }


}
