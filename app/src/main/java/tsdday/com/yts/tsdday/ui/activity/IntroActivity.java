package tsdday.com.yts.tsdday.ui.activity;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.IntroBinding;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;

public class IntroActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;
    private IntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro);
        MobileAds.initialize(this, getString(R.string.ad_app_id));
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.animationView.setMinProgress(0.2f);
        binding.animationView.setMaxProgress(0.57f);
        binding.animationView.playAnimation();
        moveMain();
    }

    private void moveMain() {
        boolean isFingerprintLogin = SharedPrefsUtils.getBooleanPreference(this, Keys.isFingerprintLogin, false);
        compositeDisposable.add(Single.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> {
                    if (isFingerprintLogin) {
                        startActivity(new Intent(IntroActivity.this, LockActivity.class));
                    } else {
                        startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    }
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
