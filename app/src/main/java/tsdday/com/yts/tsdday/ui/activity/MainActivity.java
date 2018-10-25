package tsdday.com.yts.tsdday.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.functions.Consumer;
import tsdday.com.yts.tsdday.BaseActivity;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.MainViewBinding;
import tsdday.com.yts.tsdday.interactor.Interactor;
import tsdday.com.yts.tsdday.interactor.OnPremiumInspection;
import tsdday.com.yts.tsdday.ui.fragment.HomeFragment;
import tsdday.com.yts.tsdday.ui.fragment.AlbumFragment;
import tsdday.com.yts.tsdday.ui.fragment.SettingFragment;
import tsdday.com.yts.tsdday.util.Convert;
import tsdday.com.yts.tsdday.util.DateFormat;
import tsdday.com.yts.tsdday.util.JobSchedulerStart;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.RequestCode;
import tsdday.com.yts.tsdday.util.SendBroadcast;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;
import tsdday.com.yts.tsdday.util.ShowIntent;
import tsdday.com.yts.tsdday.util.ToastMake;
import tsdday.com.yts.tsdday.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Interactor, View.OnClickListener, OnPremiumInspection {
    private AlbumFragment mAlbumFragment;
    private HomeFragment mHomeFragment;
    private SettingFragment mSettingFragment;

    private String[] tags = {"ALBUM", "HOME", "SETTING"};
    private String mCurrentFragmentTag = "HOME";

    private MainViewBinding binding;
    private MainViewModel mainViewModel;
    private ArrayList<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOnPremiumInspection(this);
        initPrimeum();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new MainViewModel(this);
        binding.setModel(mainViewModel);
        binding.fabAdd.setOnClickListener(this);
        binding.navigation.setOnNavigationItemSelectedListener(this);
        binding.navigation.setSelectedItemId(R.id.navigation_home);
        setFragments();
        setAd();
        changePrimenum();
        JobSchedulerStart.start(this);
        binding.adview.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                Log.d("test", String.valueOf(i));
                super.onAdFailedToLoad(i);
            }
        });
    }

    private void setFragments() {
        mAlbumFragment = AlbumFragment.newInstance();
        mHomeFragment = HomeFragment.newInstance();
        mSettingFragment = SettingFragment.newInstance();

        mAlbumFragment.setInteractor(this);
        mHomeFragment.setInteractor(this);

        fragments.add(mAlbumFragment);
        fragments.add(mHomeFragment);
        fragments.add(mSettingFragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            fragmentTransaction.add(R.id.container, fragment, tags[i]);
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.show(fragments.get(1));
        fragmentTransaction.commit();
    }



    private void setAd() {
        binding.adview.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                fabClick();
            }

            @Override
            public void onAdClosed() {
                fabClick();
                interstitialAd.loadAd(adRequest);
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        try {
            if (compositeDisposable != null) {
                compositeDisposable.clear();
            }
            switch (menuItem.getItemId()) {
                case R.id.navigation_album:
                    moveFragment(tags[0]);
                    binding.fabAdd.show();
                    return true;
                case R.id.navigation_home:
                    moveFragment(tags[1]);
                    binding.fabAdd.show();
                    return true;
                case R.id.navigation_setting:
                    moveFragment(tags[2]);
                    binding.fabAdd.hide();
                    return true;
            }
            return false;
        } catch (Exception e) {
            Crashlytics.logException(e);
            return false;
        }
    }

    private void moveFragment(String tag) {
        if (mCurrentFragmentTag.equals(tag)) {
            if (tag.equals(tags[0])) {
            /*    if (mAlbumFragment != null) {
                    mAlbumFragment.setOnlyLove();
                }*/
            }
            return;
        }
        if (tag.equals(tags[0])) {
            SendBroadcast.startLottieAnimationEmpty(this);
        } else if (tag.equals(tags[1])) {
            mHomeFragment.startAnimation();
        }
        mCurrentFragmentTag = tag;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            String fTag = fragment.getTag();
            if (fTag != null) {
                if (fTag.equals(tag)) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.hide(fragment);
                }
            }

        }
        fragmentTransaction.commit();
    }

    public void fabClick() {
        if (mCurrentFragmentTag.equals(tags[0])) {
            SendBroadcast.startAlbum(this, DateFormat.getDateString(Calendar.getInstance()));
        } else if (mCurrentFragmentTag.equals(tags[1])) {
            if (mHomeFragment != null) {
                mHomeFragment.showAddAnniversary();
            }
        }
    }

    public void changePrimenum() {
        if (isPrimeum()) {
            binding.adview.setVisibility(View.GONE);
        }
    }

    /**
     * 0 = 가만히 있을때
     */
    @Override
    public void scrollStateChange(boolean isScrollDown) {
        if (isScrollDown) {
            binding.navigation.setVisibility(View.GONE);
            binding.fabAdd.hide();
        } else {
            binding.navigation.setVisibility(View.VISIBLE);
            binding.fabAdd.show();
        }
    }

    //좋아요 애니메이션
    @Override
    public void likeAnimation() {
        binding.animationLike.setVisibility(View.VISIBLE);
        binding.animationLike.playAnimation();
        binding.animationLike.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.animationLike.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int adClickCount = SharedPrefsUtils.getIntegerPreference(this, Keys.addClickCount, 1);
        if (adClickCount % 5 == 0 && interstitialAd != null && interstitialAd.isLoaded() && !isPrimeum()) {
            interstitialAd.show();
        } else {
            fabClick();
        }
        SharedPrefsUtils.setIntegerPreference(this, Keys.addClickCount, adClickCount + 1);
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCode.imageMultipleSelectAdd) {
                ArrayList<String> imageUrls = new ArrayList<>();
                if (data.getData() != null) {
                    imageUrls.add(data.getDataString());
                } else if (data.getClipData() != null) {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        ClipData.Item item = data.getClipData().getItemAt(i);
                        imageUrls.add(item.getUri().toString());
                    }
                }
                SendBroadcast.addAlbum(this, imageUrls);

            } else if (requestCode == RequestCode.backgroundSelect) {
                ShowIntent.imageCroup(this, data, RequestCode.backgroundCrop);

            } else if (requestCode == RequestCode.oneCoupleSelect) {
                ShowIntent.imageCroup(this, data, RequestCode.oneCoupleCrop);

            } else if (requestCode == RequestCode.twoCoupleSelect) {
                ShowIntent.imageCroup(this, data, RequestCode.twoCoupleCrop);
            } else if (requestCode == RequestCode.backgroundCrop || requestCode == RequestCode.oneCoupleCrop || requestCode == RequestCode.twoCoupleCrop) {
                if (mHomeFragment != null) {
                    compositeDisposable.add(Convert.filePathToByteArray(this, UCrop.getOutput(data).getPath()).subscribe(new Consumer<byte[]>() {
                        @Override
                        public void accept(byte[] bytes) throws Exception {
                            mHomeFragment.getModel().setCropImageData(bytes, requestCode);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ToastMake.make(getApplicationContext(),R.string.error_image);
                        }
                    }));
                }
            }
        }
    }


    @Override
    public void onPremiumInspection() {
        changePrimenum();
    }
}
