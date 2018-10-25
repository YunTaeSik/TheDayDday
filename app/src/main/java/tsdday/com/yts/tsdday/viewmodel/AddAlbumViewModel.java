package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.net.Uri;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import tsdday.com.yts.tsdday.interactor.AddAlbumInteractor;
import tsdday.com.yts.tsdday.interactor.OnDateClickListener;
import tsdday.com.yts.tsdday.model.Album;
import tsdday.com.yts.tsdday.model.AlbumItem;
import tsdday.com.yts.tsdday.model.RealmService;
import tsdday.com.yts.tsdday.ui.activity.MainActivity;
import tsdday.com.yts.tsdday.util.Convert;

public class AddAlbumViewModel extends BaseViewModel {
    public ObservableField<Album> albumObservableField = new ObservableField<>();
    public final ObservableBoolean albumsLoading = new ObservableBoolean(false);
    public final ObservableBoolean isDeleting = new ObservableBoolean(false);
    public ObservableInt newState = new ObservableInt();


    private AddAlbumInteractor mAddAlbumInteractor;
    public OnDateClickListener onDateClickListener;



    public AddAlbumViewModel(Context context) {
        super(context);
    }
    public ObservableField<Album> getAlbumObservableField() {
        return albumObservableField;
    }

    public void setAddAlbumInteractor(AddAlbumInteractor addAlbumInteractor) {
        //////
        mAddAlbumInteractor = addAlbumInteractor;
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    public String getDate() {
        return albumObservableField.get().getDate();
    }


    public void changeDate(String date) {
        if (date == null && mRealm == null) {
            return;
        }
        albumsLoading.set(true);
        if (newState.get() != 0) {
            newState.set(0);
        }
        /*Single<Album> getAlbum = RealmService.getAlbum(mRealm, date);
        if (getAlbum != null) {
            mCompositeDisposable.add(getAlbum
                    .subscribe(new Consumer<Album>() {
                        @Override
                        public void accept(final Album result) {
                            albumsLoading.set(false);
                            if (result.isValid()) {
                                albumObservableField.set(result);
                            }
                        }
                    }));
        }*/

    }

    public void close() {
        mAddAlbumInteractor.closeAddAlbum();
    }

    public void addAlbumItem() {
        mAddAlbumInteractor.addAlbumItem();
    }

    public void addAlbumItem(final ArrayList<String> imageUrls) {
        for (String url : imageUrls) {
            mCompositeDisposable.add(Convert.contentUriToByteArray(mContext, Uri.parse(url)).subscribe(new Consumer<byte[]>() {
                @Override
                public void accept(byte[] bytes) throws Exception {
                    AlbumItem albumItem = new AlbumItem();
                    albumItem.setImageData(bytes);
                    mRealm.beginTransaction();
             //       albumObservableField.get().addItems(albumItem);
                    mRealm.commitTransaction();
                    albumObservableField.notifyChange();
                }
            }));
        }
    }

    public void deleteAlbumItem(final int position) {
        isDeleting.set(true);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    //albumObservableField.get().getItems().get(position).deleteFromRealm();
                    isDeleting.set(false);
                } catch (Exception e) {
                    Crashlytics.logException(e);
                }
            }
        });
    }

    public void onItemMove(final int fromPosition, final int toPosition) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               // Collections.swap(albumObservableField.get().getItems(), fromPosition, toPosition);
            }
        });
    }

    @BindingAdapter({"setScrollState"})
    public static void setScrollState(FloatingActionButton btn, int newState) {

    }

    @BindingAdapter({"setOnTouchListener"})
    public static void setOnTouchListener(View view, final Context context) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).hideKeyboard();
                }
                return false;
            }
        });

    }
}
