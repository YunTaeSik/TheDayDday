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
        binding.animationView.playAnimation();
        moveMain();
    }

    private void moveMain() {
        compositeDisposable.add(Single.timer(4, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        startActivity(new Intent(IntroActivity.this, MainActivity.class));
                        finish();
                    }
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
