package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import tsdday.com.yts.tsdday.model.Anniversary;

public class AnniversaryViewModel extends BaseViewModel {
    public ObservableField<Anniversary> mAnniversary = new ObservableField<>();

    public AnniversaryViewModel(Context context) {
        super(context);

    }

    public void setAnniversary(Anniversary anniversary) {
        mAnniversary.set(anniversary);
        notifyChange();
    }
}
