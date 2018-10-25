package tsdday.com.yts.tsdday.model.diffutil;

import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import tsdday.com.yts.tsdday.model.Album;

public class AlbumDiffUtil extends DiffUtil.Callback {
    private Context mContext;

    private List<Album> mOldAlbumList;
    private List<Album> mNewAlbumList;

    public AlbumDiffUtil(Context context, List<Album> oldAlbumList, List<Album> newAlbumList) {
        this.mContext = context;
        this.mOldAlbumList = oldAlbumList;
        this.mNewAlbumList = newAlbumList;
    }

    @Override
    public int getOldListSize() {
        return mOldAlbumList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewAlbumList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldAlbumList.get(oldItemPosition).equals(mNewAlbumList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Album oldAlbum = mOldAlbumList.get(oldItemPosition);
        Album newAlbum = mNewAlbumList.get(newItemPosition);
        return oldAlbum.getTitle().equals(newAlbum.getTitle()) && oldAlbum.isLike == newAlbum.isLike;
    }


}
