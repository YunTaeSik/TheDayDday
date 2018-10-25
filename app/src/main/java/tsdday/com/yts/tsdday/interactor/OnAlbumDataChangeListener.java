package tsdday.com.yts.tsdday.interactor;

public interface OnAlbumDataChangeListener {
    void notifyDataSetChanged();

    void notifyItemRangeRemoved(int startPosition, int endPosition);

    void notifyItemRangeInserted(int startPosition, int endPosition);

    void notifyItemRangeChanged(int startPosition, int endPosition);
}
