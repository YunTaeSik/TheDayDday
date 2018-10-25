package tsdday.com.yts.tsdday.ui.dialog;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.AnniversaryAddBinding;
import tsdday.com.yts.tsdday.viewmodel.AddAnniversaryViewModel;

public class AnniversaryAddDialog extends DialogFragment {
    private AnniversaryAddBinding binding;
    public AddAnniversaryViewModel model;

    public static AnniversaryAddDialog newInstance() {
        Bundle args = new Bundle();
        AnniversaryAddDialog fragment = new AnniversaryAddDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setModel(AddAnniversaryViewModel model) {
        this.model = model;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_anniversary_add, container, false);
        binding.setModel(model);
        return binding.getRoot();
    }
}
