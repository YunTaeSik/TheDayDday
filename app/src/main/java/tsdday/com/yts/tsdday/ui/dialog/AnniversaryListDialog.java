package tsdday.com.yts.tsdday.ui.dialog;

import android.app.Dialog;

import androidx.databinding.DataBindingUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.crashlytics.android.Crashlytics;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.databinding.AnniversaryListBinding;
import tsdday.com.yts.tsdday.interactor.CoupleInteractor;
import tsdday.com.yts.tsdday.interactor.OnDeleteItem;
import tsdday.com.yts.tsdday.model.Couple;
import tsdday.com.yts.tsdday.ui.adapter.AnniversaryAdapter;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.RecyclerItemTouchHelper;
import tsdday.com.yts.tsdday.util.ToastMake;
import tsdday.com.yts.tsdday.viewmodel.AddAnniversaryViewModel;

public class AnniversaryListDialog extends DialogFragment implements OnDeleteItem {
    private AnniversaryListBinding binding;
    private CoupleInteractor coupleInteractor;
    private Couple mCouple;
    private RecyclerItemTouchHelper helper;
    private AddAnniversaryViewModel model;

    public static AnniversaryListDialog newInstance() {
        Bundle args = new Bundle();
        AnniversaryListDialog fragment = new AnniversaryListDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCoupleInteractor(CoupleInteractor coupleInteractor) {
        this.coupleInteractor = coupleInteractor;
    }

    public void setCouple(Couple couple) {
        mCouple = couple;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_anniversary_list, container, false);
            model = new AddAnniversaryViewModel(getActivity(), mCouple);
            binding.setModel(model);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (helper == null) {
            helper = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT);
            helper.setListener(this);
            ItemTouchHelper.SimpleCallback simpleCallback = helper;
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.listAnniversary);
        }*/
        if (getActivity() != null) {
            getActivity().registerReceiver(broadcastReceiver, getIntentFilter());
        }
        ToastMake.tip(getContext(), getString(R.string.tip_anniversary));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(broadcastReceiver);
        if (coupleInteractor != null) {
            coupleInteractor.OnAnniversaryChange();
        }
    }

    @Override
    public void onDeleteItem(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        binding.listAnniversary.getAdapter().notifyItemRemoved(position);
        if (model != null) {
            try {
                AnniversaryAdapter.AnniversaryViewHolder holder = (AnniversaryAdapter.AnniversaryViewHolder) viewHolder;
                if (holder != null && holder.getModel() != null && holder.getModel().mAnniversary != null && holder.getModel().mAnniversary.get() != null) {
                    model.onDelete(holder.getModel().mAnniversary.get().getTitle(), position);
                }
            } catch (Exception e) {
                Crashlytics.logException(e);
            }
        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Keys.SEND_DELETE_ANNIVERSARY)) {
                    AlertDialogCreate alertDialogCreate = new AlertDialogCreate(getContext());
                    alertDialogCreate.deleteAnniversary(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (model != null) {
                                String title = intent.getStringExtra(Keys.TITLE);
                                int position = intent.getIntExtra(Keys.POSITION, 0);
                                model.onDelete(title, position);
                            }
                        }
                    });

                }
            }
        }
    };

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Keys.SEND_DELETE_ANNIVERSARY);
        return intentFilter;
    }

}
