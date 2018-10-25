package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.databinding.BaseObservable;

import com.android.billingclient.api.BillingClient;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;
import tsdday.com.yts.tsdday.BaseActivity;
import tsdday.com.yts.tsdday.util.SendBroadcast;

public class BaseViewModel extends BaseObservable {
    private long mLastClickTime = 0;

    public Context mContext;
    public Realm mRealm = null;
    public CompositeDisposable mCompositeDisposable;
    public BillingClient mBillingClient;

    public BaseViewModel(Context context) {
        mContext = context;
        mRealm = Realm.getDefaultInstance();
        if (context instanceof BaseActivity) {
            mCompositeDisposable = ((BaseActivity) context).compositeDisposable;
            mBillingClient = ((BaseActivity) context).mBillingClient;
        }
    }

    public boolean clickTimeCheck() {
        if (System.currentTimeMillis() - mLastClickTime < 500) {
            return true;
        }
        mLastClickTime = System.currentTimeMillis();
        return false;
    }

    public void hideKeyBoard(View view) {
        SendBroadcast.hideKeyboard(view.getContext());
    }

    public void close(View view) {
        SendBroadcast.close(view.getContext());
        hideKeyBoard(view);
    }


}
