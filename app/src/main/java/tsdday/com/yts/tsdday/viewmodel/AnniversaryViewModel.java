package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import tsdday.com.yts.tsdday.model.Anniversary;
import tsdday.com.yts.tsdday.util.SendBroadcast;

public class AnniversaryViewModel extends BaseViewModel {
    public ObservableField<Anniversary> mAnniversary = new ObservableField<>();

    private int position;

    public AnniversaryViewModel(Context context) {
        super(context);

    }

    public void setAnniversary(Anniversary anniversary) {
        mAnniversary.set(anniversary);
        notifyChange();
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean delete(View view) {
        Anniversary anniversary = mAnniversary.get();
        if (anniversary != null) {
            SendBroadcast.deleteAnniversary(view.getContext(), anniversary.getTitle(), position);
        }
        return true;
    }
}
