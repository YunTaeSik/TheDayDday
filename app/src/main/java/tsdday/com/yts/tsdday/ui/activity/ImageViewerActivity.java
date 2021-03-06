package tsdday.com.yts.tsdday.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.ImageViewerBinding;
import tsdday.com.yts.tsdday.model.AlbumItem;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.viewmodel.ImageViewerViewModel;

public class ImageViewerActivity extends AppCompatActivity {
    private ImageViewerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewer);

        String transName = getIntent().getStringExtra(Keys.TRANS_NAME);
        AlbumItem albumItem = getIntent().getParcelableExtra(Keys.ALBUM_ITEM);

        ImageViewerViewModel model = new ImageViewerViewModel(this, albumItem);
        binding.setModel(model);

        ViewCompat.setTransitionName(binding.imagePhoto, transName);
    }
}
