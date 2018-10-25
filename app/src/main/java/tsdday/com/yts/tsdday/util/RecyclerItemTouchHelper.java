package tsdday.com.yts.tsdday.util;

import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.interactor.OnDeleteItem;
import tsdday.com.yts.tsdday.interactor.OnItemMoveListener;
import tsdday.com.yts.tsdday.ui.adapter.AnniversaryAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private OnDeleteItem listener;
    private OnItemMoveListener onItemMoveListener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public void setListener(OnDeleteItem listener) {
        this.listener = listener;
    }

    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        this.onItemMoveListener = onItemMoveListener;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
/*

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                if (viewHolder instanceof AlbumItemAdapter.AlbumItemViewHolder) {
                    AlbumItemAdapter.AlbumItemViewHolder holder = (AlbumItemAdapter.AlbumItemViewHolder) viewHolder;
                    if (holder.getAlbumItemModel() != null) {
                        View view = holder.getAlbumItemModel().layoutForeground;
                        holder.getAlbumItemModel().animationDelete.setMinAndMaxProgress(0.0f, 1.0f);
                        getDefaultUIUtil().onSelected(view);
                    }
                } else if (viewHolder instanceof AnniversaryAdapter.AnniversaryViewHolder) {
                    AnniversaryAdapter.AnniversaryViewHolder holder = (AnniversaryAdapter.AnniversaryViewHolder) viewHolder;
                    if (holder.getBinding() != null) {
                        View view = holder.getBinding().layoutForeground;
                        getDefaultUIUtil().onSelected(view);
                    }
                }
            } else {
                super.onSelectedChanged(viewHolder, actionState);
            }
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder != null) {
            if (viewHolder instanceof AlbumItemAdapter.AlbumItemViewHolder) {
                AlbumItemAdapter.AlbumItemViewHolder holder = (AlbumItemAdapter.AlbumItemViewHolder) viewHolder;
                if (holder.getAlbumItemModel() != null) {
                    View view = holder.getAlbumItemModel().layoutForeground;
                    holder.getAlbumItemModel().animationDelete.setMinAndMaxProgress(0.0f, 1.0f);
                    getDefaultUIUtil().clearView(view);
                }
            } else if (viewHolder instanceof AnniversaryAdapter.AnniversaryViewHolder) {
                AnniversaryAdapter.AnniversaryViewHolder holder = (AnniversaryAdapter.AnniversaryViewHolder) viewHolder;
                if (holder.getBinding() != null) {
                    View view = holder.getBinding().layoutForeground;
                    getDefaultUIUtil().clearView(view);
                }
            }
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        if (viewHolder != null) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                if (viewHolder instanceof AlbumItemAdapter.AlbumItemViewHolder) {
                    AlbumItemAdapter.AlbumItemViewHolder holder = (AlbumItemAdapter.AlbumItemViewHolder) viewHolder;
                    if (holder.getAlbumItemModel() != null) {
                        View view = holder.getAlbumItemModel().layoutForeground;
                        getDefaultUIUtil().onDrawOver(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);

                        float progress = (Math.abs(dX) / (float) view.getMeasuredWidth()) - 0.1f;
                        holder.getAlbumItemModel().animationDelete.setProgress(progress);
                        if (progress >= 0.7f) {
                            if (holder.getAlbumItemModel().textDelete.getVisibility() == View.VISIBLE) {
                                holder.getAlbumItemModel().textDelete.setVisibility(View.GONE);
                            }
                        } else {
                            if (holder.getAlbumItemModel().textDelete.getVisibility() == View.GONE) {
                                holder.getAlbumItemModel().textDelete.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else if (viewHolder instanceof AnniversaryAdapter.AnniversaryViewHolder) {
                    AnniversaryAdapter.AnniversaryViewHolder holder = (AnniversaryAdapter.AnniversaryViewHolder) viewHolder;
                    if (holder.getBinding() != null) {
                        View view = holder.getBinding().layoutForeground;
                        getDefaultUIUtil().onDrawOver(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);

                        float progress = (Math.abs(dX) / (float) view.getMeasuredWidth());
                        if (progress >= 0.5f) {
                            holder.getBinding().textDelete.setVisibility(View.VISIBLE);
                            holder.getBinding().imageDelete.setScaleX(1.2f);
                            holder.getBinding().imageDelete.setScaleY(1.2f);
                            holder.getBinding().layoutDelete.setBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.colorPrimary));
                        } else {
                            holder.getBinding().textDelete.setVisibility(View.GONE);
                            holder.getBinding().imageDelete.setScaleX(1);
                            holder.getBinding().imageDelete.setScaleY(1);
                            holder.getBinding().layoutDelete.setBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.white));
                        }
                    }
                }
            } else {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if (viewHolder != null) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                if (viewHolder instanceof AlbumItemAdapter.AlbumItemViewHolder) {
                    AlbumItemAdapter.AlbumItemViewHolder holder = (AlbumItemAdapter.AlbumItemViewHolder) viewHolder;
                    if (holder.getAlbumItemModel() != null) {
                        View view = holder.getAlbumItemModel().layoutForeground;
                        getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);

                        float progress = (Math.abs(dX) / (float) view.getMeasuredWidth()) - 0.1f;
                        holder.getAlbumItemModel().animationDelete.setProgress(progress);

                        if (progress >= 0.7f) {
                            if (holder.getAlbumItemModel().textDelete.getVisibility() == View.VISIBLE) {
                                holder.getAlbumItemModel().textDelete.setVisibility(View.GONE);
                            }
                        } else {
                            if (holder.getAlbumItemModel().textDelete.getVisibility() == View.GONE) {
                                holder.getAlbumItemModel().textDelete.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else if (viewHolder instanceof AnniversaryAdapter.AnniversaryViewHolder) {
                    AnniversaryAdapter.AnniversaryViewHolder holder = (AnniversaryAdapter.AnniversaryViewHolder) viewHolder;
                    if (holder.getBinding() != null) {
                        View view = holder.getBinding().layoutForeground;
                        getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);

                        float progress = (Math.abs(dX) / (float) view.getMeasuredWidth());
                        if (progress >= 0.5f) {
                            holder.getBinding().textDelete.setVisibility(View.VISIBLE);
                            holder.getBinding().imageDelete.setScaleX(1.2f);
                            holder.getBinding().imageDelete.setScaleY(1.2f);
                            holder.getBinding().layoutDelete.setBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.colorPrimary));
                        } else {
                            holder.getBinding().textDelete.setVisibility(View.GONE);
                            holder.getBinding().imageDelete.setScaleX(1);
                            holder.getBinding().imageDelete.setScaleY(1);
                            holder.getBinding().layoutDelete.setBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.white));
                        }
                    }
                }
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    }
*/

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null) {
            listener.onDeleteItem(viewHolder, direction, viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.e("test", "onMove");
        if (onItemMoveListener != null) {
            if (target.getAdapterPosition() != 0) {
                onItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            }
        }
        return true;

    }

}