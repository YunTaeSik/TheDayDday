package tsdday.com.yts.tsdday.ui.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.AnniversaryBinding;
import tsdday.com.yts.tsdday.model.Anniversary;

public class AnniversaryAdapter extends RecyclerView.Adapter {
    public ObservableArrayList<Anniversary> anniversaryList;

    public AnniversaryAdapter(ObservableArrayList<Anniversary> anniversaryList) {
        this.anniversaryList = anniversaryList;
    }

    public void setAnniversaryList(ObservableArrayList<Anniversary> anniversaryList) {
        this.anniversaryList = anniversaryList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        if (anniversaryList != null) {
            return anniversaryList.get(position).getTitle().hashCode();
        }
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        AnniversaryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_anniversary, viewGroup, false);
        return new AnniversaryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Anniversary anniversary = anniversaryList.get(position);
        AnniversaryViewHolder holder = (AnniversaryViewHolder) viewHolder;
     //   holder.binding.setVariable(BR.model, anniversary);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (anniversaryList != null) {
            return anniversaryList.size();
        }
        return 0;
    }

    public class AnniversaryViewHolder extends RecyclerView.ViewHolder {
        private AnniversaryBinding binding;

        public AnniversaryViewHolder(AnniversaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public AnniversaryBinding getBinding() {
            return binding;
        }

    }

}
