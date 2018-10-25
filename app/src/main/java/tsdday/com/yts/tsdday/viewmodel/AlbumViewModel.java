package tsdday.com.yts.tsdday.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.appcompat.widget.AppCompatImageView;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.interactor.Interactor;
import tsdday.com.yts.tsdday.interactor.OnDateSelectListener;
import tsdday.com.yts.tsdday.model.Album;
import tsdday.com.yts.tsdday.model.AlbumItem;
import tsdday.com.yts.tsdday.model.RealmService;
import tsdday.com.yts.tsdday.ui.dialog.AlertDialogCreate;
import tsdday.com.yts.tsdday.ui.dialog.DateSelectDialog;
import tsdday.com.yts.tsdday.util.Convert;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.RequestCode;
import tsdday.com.yts.tsdday.util.SendBroadcast;
import tsdday.com.yts.tsdday.util.ShowIntent;
import tsdday.com.yts.tsdday.util.ToastMake;

public class AlbumViewModel extends BaseViewModel {
    public final int EDIT_TITLE_TYPE = 100;
    public final int EDIT_CONENT_TYPE = 101;
    private int position;

    public ObservableField<Album> album = new ObservableField<>(new Album());
    public ObservableBoolean isLike = new ObservableBoolean(false);

    public ObservableArrayList<Object> itemList = new ObservableArrayList<>();
    public ObservableBoolean isLoading = new ObservableBoolean(false);

    public AlbumViewModel(Context context) {
        super(context);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setAlbum(String date) {
        isLoading.set(true);
        mCompositeDisposable.add(RealmService.getAlbum(mRealm, date).subscribe(new Consumer<Album>() {
            @Override
            public void accept(Album value) throws Exception {
                album.set(value);
                isLike.set(value.isLike());
                setRealmToItemList(value);
                isLoading.set(false);
            }
        }));
    }

    public void setAlbum(Album album) {
        isLoading.set(true);
        this.album.set(album);
        isLike.set(album.isLike());
        setRealmToItemList(album);
        isLoading.set(false);
    }


    private void setRealmToItemList(final Album album) {
        isLoading.set(true);
        itemList.clear();
        List<Object> items = new ArrayList<>();
        if (album != null) {
            if (album.getItems() != null && album.getItems().size() > 0) {
                List<AlbumItem> albumItems = new ArrayList<>();
                try {
                    albumItems = mRealm.copyFromRealm(album.getItems());
                } catch (Exception e) {

                    items.addAll(album.getItems());
                }
                items.addAll(albumItems);
            } else {
                items.add(Keys.EMPTY);
            }
        } else {
            items.add(Keys.EMPTY);
        }
        itemList.addAll(items);
        isLoading.set(false);
    }

    private void addItemList(ArrayList<AlbumItem> albumItems) {
        if (itemList.size() == 1 && itemList.get(0) instanceof String) {
            itemList.remove(0);
        }
        itemList.addAll(albumItems);
    }

    public void onTextChange(CharSequence charSequence, int type) {
        String text = charSequence.toString();
        if (type == EDIT_TITLE_TYPE) {
            album.get().setTitle(text);
        } else if (type == EDIT_CONENT_TYPE) {
            album.get().setContent(text);
        }
    }

    public void saveAlbum(final View view) {

        final Album album = this.album.get();
        if (album.getTitle() == null || album.getTitle().length() == 0) {
            ToastMake.make(view.getContext(), R.string.hint_title);
            return;
        }
        isLoading.set(true);
        RealmService.saveAlbum(mRealm, album, new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(album);
                isLoading.set(false);
                SendBroadcast.editAlbum(view.getContext());
                close(view);
            }
        });
    }

    public void addAlbumItem() {
        ShowIntent.imageMultiSelect((Activity) mContext, RequestCode.imageMultipleSelectAdd);
    }

    public void addAlbumItemList(ArrayList<String> imageUrls) {
        isLoading.set(true);
        final ArrayList<AlbumItem> albumItems = new ArrayList<>();
        mCompositeDisposable.add(Convert.contentUriToFilePath(mContext, imageUrls).subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onNext(String path) {
                AlbumItem albumItem = new AlbumItem();
                albumItem.setImageDataPath(path);
                albumItems.add(albumItem);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                album.get().getItems().addAll(albumItems);
                addItemList(albumItems);
                isLoading.set(false);
            }
        }));
    }

    public void deleteAlbumItem(int position) {
        if (album.get() != null) {
            Album album = this.album.get();
            List<AlbumItem> albumItems = album.getItems();
            if (albumItems != null) {
                albumItems.remove(position);
                itemList.remove(position);
            }
            if (itemList.size() == 0) {
                itemList.add(Keys.EMPTY);
            }

        }
    }

    public void startDateDialog(View view) {
        SendBroadcast.startDateDialog(view.getContext(), album.get().getDate());
    }

    public void cardClick(View view) {
        SendBroadcast.startAlbum(view.getContext(), album.get().getDate());
    }


    public void likeClick(final View view) {

        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            Drawable drawable = imageView.getDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (drawable instanceof AnimatedVectorDrawable) {
                    AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                    animatedVectorDrawable.start();
                }
            } else {
                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    AnimatedVectorDrawableCompat animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    animatedVectorDrawableCompat.start();
                }
            }
        }
        mCompositeDisposable.add(Single.timer(300, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Album newAlbum = album.get();
                        newAlbum.setLike(!newAlbum.isLike());
                        String date = newAlbum.getDate();
                        boolean like = newAlbum.isLike();
                        RealmService.like(mRealm, date, like);

                        isLike.set(like);
                        isLike.notifyChange();
                        if (like) {
                            SendBroadcast.likeAlbum(view.getContext(), position);
                        }
                    }
                }));


    }


    public void deleteClick() {
        AlertDialogCreate alert = new AlertDialogCreate(mContext);
        alert.delete(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RealmService.deleteAlbum(mRealm, album.get().getDate());
                SendBroadcast.deleteAlbum(mContext, position);
            }
        });
    }

}
