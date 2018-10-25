package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;

import tsdday.com.yts.tsdday.model.AlbumItem;


public class ImageViewerViewModel extends BaseViewModel {
    public AlbumItem mAlbumItem;

    public ImageViewerViewModel(Context context, AlbumItem albumItem) {
        super(context);
        this.mAlbumItem = albumItem;
        // mPath.set(path);
    }
}
