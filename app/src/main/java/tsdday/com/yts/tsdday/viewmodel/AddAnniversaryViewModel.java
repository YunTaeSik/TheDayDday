package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import android.os.Build;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import androidx.transition.TransitionInflater;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import tsdday.com.yts.tsdday.BaseActivity;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.interactor.OnDateSelectListener;
import tsdday.com.yts.tsdday.model.Anniversary;
import tsdday.com.yts.tsdday.model.Couple;
import tsdday.com.yts.tsdday.model.RealmService;
import tsdday.com.yts.tsdday.ui.dialog.AnniversaryAddDialog;
import tsdday.com.yts.tsdday.ui.dialog.DateSelectDialog;
import tsdday.com.yts.tsdday.util.ToastMake;

public class AddAnniversaryViewModel extends BaseViewModel implements OnDateSelectListener {
    public ObservableArrayList<Anniversary> anniversaries = new ObservableArrayList<>();
    private Couple mCouple;

    public final ObservableBoolean loadingViewShowing = new ObservableBoolean(true);

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public final ObservableBoolean isRepeatYear = new ObservableBoolean(false);
    public final ObservableBoolean isRepeatMonth = new ObservableBoolean(false);


    public AddAnniversaryViewModel(Context context, Couple couple) {
        super(context);
        mCouple = couple;
        findAddedAnniversary();
    }

    public void initData() {
        title.set("");
        date.set("");
        isRepeatYear.set(false);
        isRepeatMonth.set(false);
    }

    public void findAddedAnniversary() {
        anniversaries.clear();
        anniversaries.addAll(RealmService.getAnniversaryList(mRealm, mContext, mCouple, true));
        loadingViewShowing.set(false);
    }

    public void showAddAnniversary() {
        initData();
        if (mContext != null && mContext instanceof BaseActivity) {
            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
            AnniversaryAddDialog anniversaryAddDialog = AnniversaryAddDialog.newInstance();
            anniversaryAddDialog.setModel(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                anniversaryAddDialog.setEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode));
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setReorderingAllowed(true)
                    .addToBackStack(null)
                    .add(android.R.id.content, anniversaryAddDialog)
                    .commit();
        }
    }

    public void onTextChanged(final CharSequence text) {
        this.title.set(text.toString());
    }

    public void onClickDate() {
        if (mContext instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) mContext;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            DateSelectDialog dialog = DateSelectDialog.newInstance(date.get());
            dialog.setOnDateSelectListener(this);
            dialog.show(fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK), null);
        }
    }

    public void onSave(View view) {
        if (title.get() == null || title.get().isEmpty()) {
            ToastMake.error(mContext, mContext.getString(R.string.error_title));
        } else if (date.get() == null || date.get().isEmpty()) {
            ToastMake.error(mContext, mContext.getString(R.string.error_date));
        } else {
            Anniversary anniversary = new Anniversary();
            anniversary.setTitle(title.get());
            anniversary.setDate(date.get());
            anniversary.setAdded(true);
            anniversary.setRepeatYear(isRepeatYear.get());
            anniversary.setRepeatMonth(isRepeatMonth.get());
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(anniversary);
            mRealm.commitTransaction();
            close(view);
            findAddedAnniversary();
        }
    }

    public void onDelete(final String title, int position) {
        anniversaries.remove(position);
        notifyChange();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.where(Anniversary.class).equalTo("title", title).findAll().deleteAllFromRealm();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                }
            }
        });
    }


    @Override
    public void OnDateSelect(String date) {
        this.date.set(date);
        notifyChange();
    }
}
