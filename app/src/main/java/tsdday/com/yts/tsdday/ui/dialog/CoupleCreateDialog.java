package tsdday.com.yts.tsdday.ui.dialog;

import android.app.Dialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.CoupleCreateBinding;
import tsdday.com.yts.tsdday.util.NotificationCreate;
import tsdday.com.yts.tsdday.util.WidgetUpdater;
import tsdday.com.yts.tsdday.viewmodel.CoupleViewModel;

public class CoupleCreateDialog extends DialogFragment {
    private CoupleCreateBinding binding;
    private CoupleViewModel model;

    public static CoupleCreateDialog newInstance() {
        Bundle args = new Bundle();
        CoupleCreateDialog fragment = new CoupleCreateDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setModel(CoupleViewModel model) {
        this.model = model;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_couple_create, container, false);
            binding.setModel(model);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (model != null) {
            model.findCouple(true);
        }
    }

    @Override
    public void onDestroyView() {
        NotificationCreate.reStrart(getActivity());
        WidgetUpdater.update(getActivity());
        if (model != null) {
            model.OnAnniversaryChange();
        }
        super.onDestroyView();
    }
}
