package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import io.realm.Realm;
import tsdday.com.yts.tsdday.interactor.OnDateClickListener;
import tsdday.com.yts.tsdday.model.Album;

public class AlbumItemHeaderViewModel extends BaseViewModel {
    private OnDateClickListener onDateClickListener;

    public ObservableField<Album> albumObservableField = new ObservableField<>();

    public AlbumItemHeaderViewModel(Context context) {
        super(context);
    }

    public void setAlbum(Album album) {
        albumObservableField.set(album);
    }

    public ObservableField<Album> getAlbumObservableField() {
        return albumObservableField;
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    public void clickDate() {
        onDateClickListener.onDateClickListener();
    }

    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                albumObservableField.get().setTitle(s.toString());
            }
        });
    }


}
