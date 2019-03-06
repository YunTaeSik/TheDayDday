package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Calendar;
import java.util.List;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import io.reactivex.functions.Consumer;
import tsdday.com.yts.tsdday.model.Album;
import tsdday.com.yts.tsdday.model.RealmService;
import tsdday.com.yts.tsdday.util.DateFormat;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SendBroadcast;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;

public class AlbumListViewModel extends BaseViewModel {

    public final ObservableBoolean isScrollDown = new ObservableBoolean(false);
    public final ObservableBoolean isLoading = new ObservableBoolean(false);
    public final ObservableBoolean isEmpty = new ObservableBoolean(true);

    public final ObservableBoolean onlyLike = new ObservableBoolean(false);
    public ObservableList<Album> albumList = new ObservableArrayList<>();

    public AlbumListViewModel(Context context) {
        super(context);
    }

    public void setOnlyLike(boolean onlyLike) {
        this.onlyLike.set(onlyLike);
    }

    public void setIsScrollDown(boolean isScrollDown) {
        this.isScrollDown.set(isScrollDown);
    }

    public void startEmptyAnimation(LottieAnimationView view) {
        if (isEmpty.get()) {
            view.playAnimation();
        }
    }

    public void stopEmptyAnimation(LottieAnimationView view) {
        if (isEmpty.get()) {
            view.setFrame(100);
            view.cancelAnimation();
        }
    }

    public void listMode(View view) {
        SharedPrefsUtils.setBooleanPreference(view.getContext(), Keys.VIEW_FORMAT_IS_GRID, false);
        SendBroadcast.albumList(view.getContext());
    }

    public void gridMode(View view) {
        SharedPrefsUtils.setBooleanPreference(view.getContext(), Keys.VIEW_FORMAT_IS_GRID, true);
        SendBroadcast.albumGrid(view.getContext());
    }

    public void startAlbum(View view) {
        SendBroadcast.startAlbum(view.getContext(), DateFormat.getDateString(Calendar.getInstance()));
    }

    public void delete(int position) {
        if (albumList != null && albumList.size() > position) {
            albumList.remove(position);
            if (albumList.size() == 0) {
                isEmpty.set(true);
            }
        }
    }

    public void listLike() {
        onlyLike.set(!onlyLike.get());
        findAlbum();
    }

    public void findAlbum() {
        isLoading.set(true);
        isEmpty.set(false);
        mCompositeDisposable.add(RealmService.getAlbumList(mRealm, onlyLike.get()).subscribe(new Consumer<List<Album>>() {
            @Override
            public void accept(List<Album> albums) throws Exception {
                if (albums != null && albums.size() > 0) {
                    albumList.clear();
                    albumList.addAll(albums);
                } else {
                    isEmpty.set(true);
                }
                isLoading.set(false);
            }
        }));
    }

}
