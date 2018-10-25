package tsdday.com.yts.tsdday.ui.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.ViewGroup;


import tsdday.com.yts.tsdday.BR;
import tsdday.com.yts.tsdday.R;

import tsdday.com.yts.tsdday.databinding.AnniversaryBinding;
import tsdday.com.yts.tsdday.databinding.CoupleHeaderBinding;

import tsdday.com.yts.tsdday.model.Anniversary;
import tsdday.com.yts.tsdday.model.Couple;

import tsdday.com.yts.tsdday.viewmodel.CoupleViewModel;

public class HomeListAdapter extends RecyclerView.Adapter {
    private int HEADER_TYPE = 1;
    private int DAY_TYPE = 2;

    private ObservableArrayList<Object> mHomeList;
    private CoupleViewModel mCoupleViewModel;
    private Context mContext;

    public HomeListAdapter(Context context, ObservableArrayList<Object> homeList) {
        mContext = context;
        if (homeList != null) {
            mHomeList = homeList;
        }
    }

    public void setCoupleViewModel(CoupleViewModel mCoupleViewModel) {
        this.mCoupleViewModel = mCoupleViewModel;
    }

    public void setHomeList(ObservableArrayList<Object> mHomeList) {
        this.mHomeList = mHomeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mHomeList.get(position);
        if (item instanceof Couple) {
            return HEADER_TYPE;
        }
        return DAY_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == HEADER_TYPE) {
            CoupleHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_couple_header, viewGroup, false);
            return new CopleHeaderViewHolder(binding);
        } else {
            AnniversaryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_anniversary, viewGroup, false);
            return new AnniversaryViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == HEADER_TYPE) {
            Object item = mHomeList.get(position);
            if (item instanceof Couple) {
                CopleHeaderViewHolder holder = (CopleHeaderViewHolder) viewHolder;
                holder.setViewModel(mCoupleViewModel);
            }
        } else if (viewType == DAY_TYPE) {
            Object item = mHomeList.get(position);
            if (item instanceof Anniversary) {
                Anniversary anniversary = (Anniversary) item;
                AnniversaryViewHolder holder = (AnniversaryViewHolder) viewHolder;
                holder.binding.setVariable(BR.model, anniversary);
                holder.binding.executePendingBindings();
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mHomeList != null) {
            return mHomeList.size();
        }
        return 0;
    }

    private class CopleHeaderViewHolder extends RecyclerView.ViewHolder {
        private CoupleHeaderBinding binding;

        public CopleHeaderViewHolder(CoupleHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setViewModel(CoupleViewModel model) {
            binding.setModel(model);
            binding.executePendingBindings();
        }
    }

    private class AnniversaryViewHolder extends RecyclerView.ViewHolder {
        private AnniversaryBinding binding;

        public AnniversaryViewHolder(AnniversaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
