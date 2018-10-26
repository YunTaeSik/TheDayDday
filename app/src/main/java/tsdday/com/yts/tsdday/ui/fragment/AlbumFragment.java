package tsdday.com.yts.tsdday.ui.fragment;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.NewAlbumBinding;
import tsdday.com.yts.tsdday.interactor.Interactor;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.viewmodel.AlbumListViewModel;

public class AlbumFragment extends Fragment {
    private long mLastChangeTime = 0;
    private NewAlbumBinding binding;
    private AlbumListViewModel model;
    private Interactor mInteractor;

    public static AlbumFragment newInstance() {
        Bundle args = new Bundle();
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AlbumFragment newInstance(boolean onlyLike) {
        Bundle args = new Bundle();
        args.putBoolean(Keys.ONLYLIKE, onlyLike);
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setInteractor(Interactor mInteractor) {
        this.mInteractor = mInteractor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new AlbumListViewModel(getActivity());
        boolean onlyLike = getArguments().getBoolean(Keys.ONLYLIKE, false);
        model.setOnlyLike(onlyLike);
        model.findAlbum();
        binding.setModel(model);
        listScroll();
        if (getActivity() != null) {
            getActivity().registerReceiver(broadcastReceiver, getIntentFilter());
        }
    }

    public boolean changeTimeCheck() {
        if (System.currentTimeMillis() - mLastChangeTime < 250) {
            return true;
        }
        mLastChangeTime = System.currentTimeMillis();
        return false;
    }

    public void listScroll() {
        binding.listAlbum.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (changeTimeCheck()) {
                    return;
                }
                boolean isScrollDown = dy > 0;
                if (mInteractor != null) {
                    mInteractor.scrollStateChange(isScrollDown);
                }
                if (model != null) {
                    model.setIsScrollDown(isScrollDown);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (getActivity() != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Keys.SEND_LOTTIE_ANIMATION_ALBUM_EMPTY)) {
                    if (model != null) {
                        model.startEmptyAnimation(binding.animationView);
                    }
                } else if (action.equals(Keys.SEND_LOTTIE_ANIMATION_ALBUM_EMPTY_STOP)) {
                    if (model != null) {
                        model.stopEmptyAnimation(binding.animationView);
                    }
                } else if (action.equals(Keys.SEND_EDIT_ALBUM)) {
                    if (model != null) {
                        model.findAlbum();
                    }
                } else if (action.equals(Keys.SEND_LIKE)) {
                    if (mInteractor != null) {
                        mInteractor.likeAnimation();
                    }
                } else if (action.equals(Keys.SEND_DELETE)) {
                    int position = intent.getIntExtra(Keys.POSITION, -1);
                    if (position >= 0) {
                        if (model != null) {
                            model.delete(position);
                        }
                    }
                } else if (action.equals(Keys.SEND_ALBUM_LIST_MODE)) {
                    StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) binding.listAlbum.getLayoutManager();
                    gridLayoutManager.setSpanCount(1);
                    binding.listAlbum.setLayoutManager(gridLayoutManager);
                } else if (action.equals(Keys.SEND_ALBUM_GRID_MODE)) {
                    StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) binding.listAlbum.getLayoutManager();
                    gridLayoutManager.setSpanCount(2);
                    binding.listAlbum.setLayoutManager(gridLayoutManager);
                } else if (action.equals(Keys.SEND_ALBUM_NORMAL_MODE)) {
                } else if (action.equals(Keys.SEND_ALBUM_FAVORITE_MODE)) {
                }
            }
        }
    };

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Keys.SEND_LOTTIE_ANIMATION_ALBUM_EMPTY);
        intentFilter.addAction(Keys.SEND_LOTTIE_ANIMATION_ALBUM_EMPTY_STOP);
        intentFilter.addAction(Keys.SEND_EDIT_ALBUM);
        intentFilter.addAction(Keys.SEND_LIKE);
        intentFilter.addAction(Keys.SEND_DELETE);
        intentFilter.addAction(Keys.SEND_ALBUM_LIST_MODE);
        intentFilter.addAction(Keys.SEND_ALBUM_GRID_MODE);
        intentFilter.addAction(Keys.SEND_ALBUM_NORMAL_MODE);
        intentFilter.addAction(Keys.SEND_ALBUM_FAVORITE_MODE);
        return intentFilter;
    }

}
