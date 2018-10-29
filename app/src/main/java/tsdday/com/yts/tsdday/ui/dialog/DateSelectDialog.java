package tsdday.com.yts.tsdday.ui.dialog;


import android.app.Dialog;

import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.Calendar;
import java.util.GregorianCalendar;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.DateSelectBinding;
import tsdday.com.yts.tsdday.interactor.AddAlbumInteractor;
import tsdday.com.yts.tsdday.interactor.CoupleInteractor;
import tsdday.com.yts.tsdday.interactor.HomeInteractor;
import tsdday.com.yts.tsdday.interactor.OnDateSelectListener;
import tsdday.com.yts.tsdday.util.DateFormat;
import tsdday.com.yts.tsdday.util.SendBroadcast;

public class DateSelectDialog extends DialogFragment implements View.OnClickListener {
    private DateSelectBinding binding;
    private CoupleInteractor mCoupleInteractor;
    private HomeInteractor mHomeInteractor;
    private OnDateSelectListener onDateSelectListener;

    private String date;
    private int type;
    private Calendar selectCalendar = Calendar.getInstance();

    public static DateSelectDialog newInstance(String date) {
        Bundle args = new Bundle();
        args.putString("date", date);
        DateSelectDialog fragment = new DateSelectDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static DateSelectDialog newInstance(String date, int type) {
        Bundle args = new Bundle();
        args.putString("date", date);
        args.putInt("type", type);
        DateSelectDialog fragment = new DateSelectDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_date_select, container, false);
        if (getArguments() != null) {
            date = getArguments().getString("date");
            type = getArguments().getInt("type");
            if (date != null) {
                GregorianCalendar gregorianCalendar = DateFormat.getCalendarFromString(date);
                if (gregorianCalendar != null) {
                    selectCalendar = gregorianCalendar;
                }
            }
        }
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.date.init(selectCalendar.get(Calendar.YEAR), selectCalendar.get(Calendar.MONTH), selectCalendar.get(Calendar.DAY_OF_MONTH), null);
        binding.btnSave.setOnClickListener(this);
    }


    public void setCoupleInteractor(CoupleInteractor mCoupleInteractor) {
        this.mCoupleInteractor = mCoupleInteractor;
    }

    public void setOnDateSelectListener(OnDateSelectListener onDateSelectListener) {
        this.onDateSelectListener = onDateSelectListener;
    }

    @Override
    public void dismiss() {
        try {
            if (mCoupleInteractor != null) {
                mCoupleInteractor = null;
            }
            if (mHomeInteractor != null) {
                mHomeInteractor = null;
            }
            if (onDateSelectListener != null) {
                onDateSelectListener = null;
            }
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if (view == binding.btnSave) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(binding.date.getYear(), binding.date.getMonth(), binding.date.getDayOfMonth());
            String date = DateFormat.getDateString(calendar);

            SendBroadcast.changeDate(getContext(), date);

            if (mCoupleInteractor != null) {
                mCoupleInteractor.OnDateSelect(date, type);
            }
            if (onDateSelectListener != null) {
                onDateSelectListener.OnDateSelect(date);
            }

            dismiss();
        }
    }
}