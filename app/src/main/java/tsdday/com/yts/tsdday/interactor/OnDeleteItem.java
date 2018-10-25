package tsdday.com.yts.tsdday.interactor;

import androidx.recyclerview.widget.RecyclerView;

public interface OnDeleteItem {
    void onDeleteItem(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
