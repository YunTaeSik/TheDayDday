package tsdday.com.yts.tsdday.viewmodel;

import android.content.Context;

import androidx.databinding.ObservableField;

public class ImageViewerViewModel extends BaseViewModel {
    public byte[] imageData;

    public ImageViewerViewModel(Context context, byte[] imageData) {
        super(context);
        this.imageData = imageData;
        // mPath.set(path);
    }
}
