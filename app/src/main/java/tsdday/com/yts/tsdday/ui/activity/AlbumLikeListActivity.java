package tsdday.com.yts.tsdday.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import tsdday.com.yts.tsdday.BaseActivity;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.AlbumLikeListBinding;
import tsdday.com.yts.tsdday.ui.fragment.AlbumFragment;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;
import tsdday.com.yts.tsdday.viewmodel.MainViewModel;

public class AlbumLikeListActivity extends BaseActivity {
    private AlbumLikeListBinding binding;
    private MainViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_like_list);
        model = new MainViewModel(this);
        binding.setModel(model);
        if (isPrimeum()) {
            binding.adview.setVisibility(View.GONE);
        } else {
            binding.adview.loadAd(adRequest);
            binding.adview.setVisibility(View.VISIBLE);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, AlbumFragment.newInstance(true));
        fragmentTransaction.commit();


    }


    public boolean isPrimeum() {
        boolean isPremium = SharedPrefsUtils.getBooleanPreference(this, Keys.isPremium, false);
        boolean isRewardTime = System.currentTimeMillis() - SharedPrefsUtils.getLongPreference(this, Keys.isReward, 0) <= 86400000;
        return isPremium || isRewardTime;
    }

}
