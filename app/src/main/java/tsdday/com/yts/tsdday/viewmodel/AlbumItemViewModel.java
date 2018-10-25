package tsdday.com.yts.tsdday.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.ObservableField;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import tsdday.com.yts.tsdday.model.AlbumItem;
import tsdday.com.yts.tsdday.ui.activity.ImageViewerActivity;
import tsdday.com.yts.tsdday.ui.dialog.AlertDialogCreate;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SendBroadcast;

public class AlbumItemViewModel extends BaseViewModel {
    public ObservableField<AlbumItem> mAlbumItem = new ObservableField<>();
    public int position;

    public AlbumItemViewModel(Context context) {
        super(context);
    }

    public void setAlbumItem(AlbumItem albumItem) {
        mAlbumItem.set(albumItem);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void moveImageViewer(final View view) {
        Context context = view.getContext();

        mCompositeDisposable.add(Single.timer(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        hideKeyBoard(view);
                    }
                })
        );

        try {
            Intent imageViewer = new Intent(context, ImageViewerActivity.class);
            ViewCompat.setTransitionName(view, Keys.TRANS_NAME + position);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) mContext, view, ViewCompat.getTransitionName(view));

            imageViewer.putExtra(Keys.TRANS_NAME, Keys.TRANS_NAME + position);
            //imageViewer.putExtra(Keys.ALBUM_ITEM, mAlbumItem.get());
            imageViewer.putExtra(Keys.IMAGE_DATA, mAlbumItem.get().getImageData());

            context.startActivity(imageViewer, options.toBundle());
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public boolean delete() {
        final AlertDialogCreate alert = new AlertDialogCreate(mContext);
        alert.deleteAlbumItem(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SendBroadcast.deleteAlbumImage(mContext, position);
            }
        });
        return false;
    }
}
