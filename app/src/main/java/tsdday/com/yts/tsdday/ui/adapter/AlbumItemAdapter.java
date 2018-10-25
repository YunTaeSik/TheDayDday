package tsdday.com.yts.tsdday.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.AlbumItemBinding;
import tsdday.com.yts.tsdday.databinding.EmptyBinding;
import tsdday.com.yts.tsdday.model.AlbumItem;
import tsdday.com.yts.tsdday.viewmodel.AlbumItemViewModel;

public class AlbumItemAdapter extends RecyclerView.Adapter {
    private int EMPTY_TYPE = 0;
    private int ALBUM_ITEM_TYPE = 1;

    private List<Object> mItemList;

    public AlbumItemAdapter(List<Object> itemList) {
        mItemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mItemList.get(position);
        if (item instanceof String) {
            return EMPTY_TYPE;
        } else if (item instanceof AlbumItem) {
            return ALBUM_ITEM_TYPE;
        } else {
            return EMPTY_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        if (mItemList != null) {
            return mItemList.size();
        }
        return 0;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == ALBUM_ITEM_TYPE) {
            AlbumItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_album_item, viewGroup, false);
            return new AlbumItemViewHolder(binding);
        } else {
            EmptyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_empty_album_item, viewGroup, false);
            return new EmptyViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ALBUM_ITEM_TYPE) {
            AlbumItemViewHolder holder = (AlbumItemViewHolder) viewHolder;
            Context context = holder.itemView.getContext();
            if (mItemList.get(position) instanceof AlbumItem) {
                AlbumItem albumItem = (AlbumItem) mItemList.get(position);
                AlbumItemViewModel model = new AlbumItemViewModel(context);
                model.setAlbumItem(albumItem);
                model.setPosition(position);
                holder.setViewModel(model);
            }
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        private EmptyBinding binding;

        public EmptyViewHolder(@NonNull EmptyBinding binding) {
            super(binding.getRoot());
        }
    }

    private class AlbumItemViewHolder extends RecyclerView.ViewHolder {
        private AlbumItemBinding binding;

        public AlbumItemViewHolder(@NonNull AlbumItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setViewModel(AlbumItemViewModel model) {
            binding.setModel(model);
            binding.executePendingBindings();
        }
    }


}
