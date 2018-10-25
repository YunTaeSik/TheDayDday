package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableBoolean;

public class MainViewModel extends BaseViewModel {

    public ObservableBoolean showLikeAnimation = new ObservableBoolean(false);

    public MainViewModel(Context context) {
        super(context);
    }

    public void setLike(boolean isLike) {
        this.showLikeAnimation.set(isLike);
    }
}
