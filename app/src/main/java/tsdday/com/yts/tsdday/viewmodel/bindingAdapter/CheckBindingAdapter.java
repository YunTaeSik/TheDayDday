package tsdday.com.yts.tsdday.viewmodel.bindingAdapter;

import android.widget.ToggleButton;


import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.BindingAdapter;
import tsdday.com.yts.tsdday.util.NullFilter;

public class CheckBindingAdapter {
    @BindingAdapter({"setCheck"})
    public static void setCheck(SwitchCompat view, Boolean check) {
        view.setChecked(NullFilter.check(check));
    }

    @BindingAdapter({"setCheck"})
    public static void setCheck(ToggleButton view, Boolean check) {
        view.setChecked(NullFilter.check(check));
    }
}
