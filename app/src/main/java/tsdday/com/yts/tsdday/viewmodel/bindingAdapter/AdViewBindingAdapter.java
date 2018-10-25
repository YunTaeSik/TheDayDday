package tsdday.com.yts.tsdday.viewmodel.bindingAdapter;

import androidx.databinding.BindingAdapter;
import android.view.View;

public class AdViewBindingAdapter {
    @BindingAdapter({"setAdView"})
    public static void setAdView(View view, boolean visible) {
     /*   boolean isPremium = SharedPrefsUtils.getBooleanPreference(view.getContext(), Keys.isPremium, false);
        boolean isRewardTime = System.currentTimeMillis() - SharedPrefsUtils.getLongPreference(view.getContext(), Keys.isReward, System.currentTimeMillis()) >= 86400000;

        if (isPremium||isRewardTime) {
            view.setVisibility(View.GONE);
        }*/
    }
}
