package tsdday.com.yts.tsdday.ui.fragment;


import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tsdday.com.yts.tsdday.GlideApp;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.HomeBinding;
import tsdday.com.yts.tsdday.interactor.HomeInteractor;
import tsdday.com.yts.tsdday.interactor.Interactor;
import tsdday.com.yts.tsdday.model.Anniversary;
import tsdday.com.yts.tsdday.viewmodel.CoupleViewModel;


public class HomeFragment extends Fragment implements HomeInteractor {
    private HomeBinding binding;
    private CoupleViewModel model;
    private Interactor mInteractor;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setInteractor(Interactor mInteractor) {
        this.mInteractor = mInteractor;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new CoupleViewModel(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        model.setInteractor(this);
        binding.setModel(model);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setScrollListener();
    }

    public void startAnimation() {
        if (model != null) {
            model.findCouple(false);
        }
    }

    public void showAddAnniversary() {
        model.showAnniversaryDialog();
    }

    public CoupleViewModel getModel() {
        return model;
    }

    public void setScrollListener() {
        binding.listHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mInteractor != null) {
                    boolean isScrollDown = dy > 0;
                    mInteractor.scrollStateChange(isScrollDown);
                }
            }
        });
    }

    @Override
    public void onMoveListPosition(ObservableArrayList<Object> mAnniversaryList) {
        for (int i = 1; i < mAnniversaryList.size(); i++) {
            Object o = mAnniversaryList.get(i);
            if (o instanceof Anniversary) {
                Anniversary anniversary = (Anniversary) o;
                if (anniversary.getDday() <= 0) {
                    binding.listHome.smoothScrollToPosition(i);
                    break;
                }
            }
        }
    }
}
