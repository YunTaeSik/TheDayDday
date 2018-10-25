package tsdday.com.yts.tsdday.ui.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.viewmodel.AlbumViewModel;
import tsdday.com.yts.tsdday.databinding.AlbumBinding;

public class AlbumDialog extends DialogFragment {
    private AlbumBinding binding;
    private AlbumViewModel model;

    public static AlbumDialog newInstance(String date) {
        Bundle args = new Bundle();
        args.putString(Keys.DATE, date);
        AlbumDialog fragment = new AlbumDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_album, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new AlbumViewModel(getActivity());
        model.setAlbum(getArguments().getString(Keys.DATE));
        binding.setModel(model);
        if (getActivity() != null) {
            getActivity().registerReceiver(broadcastReceiver, getIntentFilter());
        }
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroyView();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Keys.SEND_IMAGE_URLS)) {
                    if (model != null) {
                        ArrayList<String> imageUrls = intent.getStringArrayListExtra(Keys.IMAGE_URLS);
                        model.addAlbumItemList(imageUrls);
                    }
                } else if (action.equals(Keys.SEND_DATE_CHANGE)) {
                    String date = intent.getStringExtra(Keys.DATE);
                    if (model != null) {
                        model.setAlbum(date);
                    }
                } else if (action.equals(Keys.SEND_DELETE_ALBUM_IMAGE)) {
                    int position = intent.getIntExtra(Keys.POSITION, -1);
                    if (model != null) {
                        model.deleteAlbumItem(position);
                    }
                }
            }
        }
    };

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Keys.SEND_IMAGE_URLS);
        intentFilter.addAction(Keys.SEND_DATE_CHANGE);
        intentFilter.addAction(Keys.SEND_DELETE_ALBUM_IMAGE);
        return intentFilter;
    }
}
