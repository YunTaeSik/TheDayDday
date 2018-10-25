package tsdday.com.yts.tsdday.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.PreminumBinding;
import tsdday.com.yts.tsdday.interactor.OnDismiss;
import tsdday.com.yts.tsdday.viewmodel.PremiumViewModel;

public class PremiumDialog extends Dialog implements OnDismiss {
    private PreminumBinding binding;
    private PremiumViewModel model;

    public PremiumDialog(@NonNull Context context) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_premium, null, false);
        model = new PremiumViewModel(context);
        model.setOnDismiss(this);
        binding.setModel(model);
        setContentView(binding.getRoot());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void onDismiss() {
        dismiss();
    }
}
