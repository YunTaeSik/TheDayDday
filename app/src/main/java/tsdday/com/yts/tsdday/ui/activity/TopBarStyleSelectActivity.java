package tsdday.com.yts.tsdday.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import tsdday.com.yts.tsdday.BaseActivity;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.TopBarStyleSelectBinding;
import tsdday.com.yts.tsdday.viewmodel.MainViewModel;
import tsdday.com.yts.tsdday.viewmodel.TopBarStyleSelectViewModel;

public class TopBarStyleSelectActivity extends BaseActivity {
    private TopBarStyleSelectBinding binding;
    private TopBarStyleSelectViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_bar_style_select);
        model = new TopBarStyleSelectViewModel(this);
        binding.setModel(model);
    }
}
