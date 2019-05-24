package tsdday.com.yts.tsdday.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.SettingBinding;
import tsdday.com.yts.tsdday.viewmodel.SettingViewModel;

public class SettingFragment extends Fragment {
    private SettingBinding binding;
    private SettingViewModel model;


    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new SettingViewModel(getActivity());
        binding.setModel(model);
    }
}
