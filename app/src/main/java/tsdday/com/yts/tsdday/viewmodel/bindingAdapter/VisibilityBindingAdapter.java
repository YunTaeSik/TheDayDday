package tsdday.com.yts.tsdday.viewmodel.bindingAdapter;

import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;

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
}
