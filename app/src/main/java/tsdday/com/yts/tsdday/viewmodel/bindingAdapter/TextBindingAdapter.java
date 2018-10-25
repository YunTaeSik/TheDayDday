package tsdday.com.yts.tsdday.viewmodel.bindingAdapter;

import android.content.Context;

import androidx.databinding.BindingAdapter;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.model.Anniversary;

public class TextBindingAdapter {

    @BindingAdapter({"setAnniversaryTextColor"})
    public static void setAnniversaryTextColor(TextView view, Anniversary anniversary) {
        Context context = view.getContext();
        if (anniversary != null) {
            int dday = anniversary.getDday();
            if (dday > 0) {
                view.setTextColor(ContextCompat.getColor(context, R.color.gray_dark));
            } else if (dday == 0) {
                view.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            } else {
                view.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        }

    }
}
