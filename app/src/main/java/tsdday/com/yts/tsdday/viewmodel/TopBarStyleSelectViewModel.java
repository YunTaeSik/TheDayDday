package tsdday.com.yts.tsdday.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.databinding.ObservableInt;
import tsdday.com.yts.tsdday.util.JobSchedulerStart;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;

public class TopBarStyleSelectViewModel extends BaseViewModel {
    public ObservableInt mStyle = new ObservableInt(0);

    public TopBarStyleSelectViewModel(Context context) {
        super(context);
        mStyle.set(SharedPrefsUtils.getIntegerPreference(context, Keys.NOTIFICATION_STYLE, 0));
    }

    public void close() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }
    }

    public void onClickStyle(int isStyle) {
        SharedPrefsUtils.setIntegerPreference(mContext, Keys.NOTIFICATION_STYLE, isStyle);
        mStyle.set(isStyle);
        JobSchedulerStart.start(mContext);
        //   this.isOneStyle.notifyChange();
    }
}
