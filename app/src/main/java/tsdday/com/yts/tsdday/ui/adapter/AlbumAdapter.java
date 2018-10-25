package tsdday.com.yts.tsdday.ui.adapter;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.AlbumListItemBinding;
import tsdday.com.yts.tsdday.model.Album;
import tsdday.com.yts.tsdday.util.DateFormat;
import tsdday.com.yts.tsdday.viewmodel.AlbumViewModel;

public class AlbumAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Album> mAlbumList;

    public AlbumAdapter(Context context, List<Album> albumList) {
        mContext = context;
        mAlbumList = albumList;
    }

    @Override
    public long getItemId(int position) {
        if (mAlbumList != null) {
            return DateFormat.getStringToTime(mAlbumList.get(position).getDate());
            // return mAlbumList.get(position).getDate().hashCode();
        }
        return position;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        AlbumListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_album, viewGroup, false);
        return new AlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mAlbumList.get(position).isValid()) {
            AlbumViewHolder holder = (AlbumViewHolder) viewHolder;
            Album album = mAlbumList.get(position);
            AlbumViewModel albumViewModel = new AlbumViewModel(mContext);
            albumViewModel.setAlbum(album);
            albumViewModel.setPosition(position);
            holder.setViewModel(albumViewModel);
        }
    }

    @Override
    public int getItemCount() {
        if (mAlbumList != null) {
            return mAlbumList.size();
        }
        return 0;
    }


    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        private AlbumListItemBinding binding;

        public AlbumViewHolder(AlbumListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setViewModel(AlbumViewModel viewModel) {
            binding.setModel(viewModel);
        }


    }

}
