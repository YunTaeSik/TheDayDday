package tsdday.com.yts.tsdday.viewmodel.bindingAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import tsdday.com.yts.tsdday.model.Couple;
import tsdday.com.yts.tsdday.model.User;

public class VisibilityBindingAdapter {
    @BindingAdapter({"setAlbumHeaderVisible", "isEmpty"})
    public static void setAlbumHeaderVisible(LinearLayout layout, boolean scrollDown, boolean isEmpty) {
        if (isEmpty) {
            layout.setVisibility(View.GONE);
        } else {
            if (scrollDown) {
                layout.setVisibility(View.GONE);
            } else {
                layout.setVisibility(View.VISIBLE);
            }
        }
    }

    @BindingAdapter({"setVisible"})
    public static void setVisible(ImageView view, User user) {
        if (user != null) {
            String imageDataPath = user.getImageDataPath();
            byte[] imageData = user.getImageData();
            if ((imageDataPath != null && imageDataPath.length() > 0) || imageData != null) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @BindingAdapter({"setVisible"})
    public static void setVisible(ImageView view, Couple couple) {
        if (couple != null) {
            String imageDataPath = couple.getBackgroundPath();
            byte[] imageData = couple.getBackground();
            if ((imageDataPath != null && imageDataPath.length() > 0) || imageData != null) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @BindingAdapter({"setVisible"})
    public static void setVisible(View view, boolean visible) {
        int v = visible ? View.VISIBLE : View.GONE;
        view.setVisibility(v);
    }
}
