package tsdday.com.yts.tsdday.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import android.os.Build;

import androidx.transition.TransitionInflater;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import tsdday.com.yts.tsdday.BaseActivity;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.interactor.CoupleInteractor;
import tsdday.com.yts.tsdday.interactor.HomeInteractor;
import tsdday.com.yts.tsdday.model.Couple;
import tsdday.com.yts.tsdday.model.RealmService;
import tsdday.com.yts.tsdday.ui.activity.MainActivity;
import tsdday.com.yts.tsdday.ui.dialog.AnniversaryListDialog;
import tsdday.com.yts.tsdday.ui.dialog.CoupleCreateDialog;
import tsdday.com.yts.tsdday.ui.dialog.DateSelectDialog;
import tsdday.com.yts.tsdday.util.DateFormat;
import tsdday.com.yts.tsdday.util.PermissionCheck;
import tsdday.com.yts.tsdday.util.RequestCode;
import tsdday.com.yts.tsdday.util.ShowIntent;
import tsdday.com.yts.tsdday.util.ToastMake;

public class CoupleViewModel extends BaseViewModel implements CoupleInteractor {
    public int EDIT_ONE_NAME = 0;
    public int EDIT_TWO_NAME = 2;
    public int EDIT_COUPLE_MENT = 4;
    public int EDIT_COPLE_NUMBER = 5;

    public int DATE_START_DATE_TYPE = 100;
    public int DATE_ONE_USER_BIRTH_TYPE = 200;
    public int DATE_TWO_USER_BIRTH_TYPE = 300;


    private CoupleCreateDialog coupleCreateDialog;
    private HomeInteractor interactor;

    public ObservableField<Couple> mCouple = new ObservableField<>();
    public final ObservableBoolean loadingViewShowing = new ObservableBoolean(true);
    public final ObservableBoolean emptyViewShowing = new ObservableBoolean(false);

    public ObservableArrayList<Object> mAnniversaryList = new ObservableArrayList<>();

    public CoupleViewModel(Context context) {
        super(context);
        findCouple(false);
    }

    public void setInteractor(HomeInteractor interactor) {
        this.interactor = interactor;
    }

    public void findCouple(final boolean hasCreate) {
        if (RealmService.getCouple(mRealm, hasCreate) != null) {
            if (mCouple.get() == null) {
                try {
                    Single<Couple> coupleSingle = RealmService.getCouple(mRealm, hasCreate);
                    if (coupleSingle != null) {
                        mCompositeDisposable.add(coupleSingle.subscribe(new Consumer<Couple>() {
                            @Override
                            public void accept(Couple couple) throws Exception {
                                if (couple != null && couple.isValid()) {
                                    mCouple.set(couple);
                                    OnAnniversaryChange();
                                }
                            }
                        }));
                    }
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    ToastMake.make(mContext, mContext.getString(R.string.error_default));
                }
            } else {
                OnAnniversaryChange();
            }
        } else {
            loadingViewShowing.set(false);
            emptyViewShowing.set(true);
            notifyChange();
        }
    }


    public void setAnniversaryList() {
        Log.e("setAnniversaryList", "setAnniversaryList");
        if (mRealm != null && mCouple != null && mCouple.get() != null) {
            mAnniversaryList.clear();
            mAnniversaryList.add(mCouple.get());
            mAnniversaryList.addAll(RealmService.getAnniversaryList(mRealm, mContext, mCouple.get(), false));
            loadingViewShowing.set(false);
            emptyViewShowing.set(mAnniversaryList.isEmpty());
        }
    }

    public void onTextChanged(final CharSequence text, final int type) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (type == EDIT_ONE_NAME) {
                    mCouple.get().getOneUser().setName(text.toString());
                } else if (type == EDIT_TWO_NAME) {
                    mCouple.get().getTwoUser().setName(text.toString());
                } else if (type == EDIT_COUPLE_MENT) {
                    mCouple.get().setMent(text.toString());
                } else if (type == EDIT_COPLE_NUMBER) {
                    mCouple.get().setNumber(text.toString());
                }
            }
        });
    }

    public void onClickStartOne() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    mCouple.get().setStartOne(!mCouple.get().getStartOne());
                    notifyChange();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void onClickDate(String date, int type) {
        if (mContext instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) mContext;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            DateSelectDialog dialog = DateSelectDialog.newInstance(date, type);
            dialog.setCoupleInteractor(this);
            dialog.show(fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK), null);
        }
    }

    public void onClickImage(final int requestCode) {
        PermissionCheck.imageCheck(mContext, new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ShowIntent.imageSelect(((Activity) mContext), requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                ToastMake.error(mContext, mContext.getString(R.string.error_permission));

            }
        });
    }

    public void onClickMove() {
        if (interactor != null) {
            interactor.onMoveListPosition(mAnniversaryList);
        }
    }

    public void showCreateCouple() {
        try {
            if (mContext instanceof MainActivity) {
                MainActivity activity = (MainActivity) mContext;
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                coupleCreateDialog = CoupleCreateDialog.newInstance();
                coupleCreateDialog.setModel(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    coupleCreateDialog.setEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.slide_bottom));
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(android.R.id.content, coupleCreateDialog)
                        .commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAnniversaryDialog() {
        if (mContext instanceof MainActivity) {
            MainActivity activity = (MainActivity) mContext;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            AnniversaryListDialog anniversaryListDialog = AnniversaryListDialog.newInstance();
            anniversaryListDialog.setCoupleInteractor(this);
            anniversaryListDialog.setCouple(mCouple.get());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                anniversaryListDialog.setEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.slide_bottom));
            }
            try {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(android.R.id.content, anniversaryListDialog)
                        .commit();
            } catch (Exception e) {
                Crashlytics.logException(e);
                ToastMake.make(mContext, mContext.getString(R.string.error_default));
            }
        }
    }


    public void setCropImageData(final byte[] bytes, final int requestCode) {
        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (requestCode == RequestCode.backgroundCrop) {
                        mCouple.get().setBackground(bytes);
                    } else if (requestCode == RequestCode.oneCoupleCrop) {
                        mCouple.get().getOneUser().setImageData(bytes);
                    } else if (requestCode == RequestCode.twoCoupleCrop) {
                        mCouple.get().getTwoUser().setImageData(bytes);
                    }
                    notifyChange();
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            ToastMake.make(mContext, R.string.error_image);
        }
    }

    public void setCropImageData(final String imageDataPath, final int requestCode) {
        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (requestCode == RequestCode.backgroundCrop) {
                        mCouple.get().setBackgroundPath(imageDataPath);
                    } else if (requestCode == RequestCode.oneCoupleCrop) {
                        mCouple.get().getOneUser().setImageDataPath(imageDataPath);
                    } else if (requestCode == RequestCode.twoCoupleCrop) {
                        mCouple.get().getTwoUser().setImageDataPath(imageDataPath);
                    }
                    notifyChange();
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            ToastMake.make(mContext, R.string.error_image);
        }
    }

    @Override
    public void OnDateSelect(final String date, final int type) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (type == DATE_ONE_USER_BIRTH_TYPE) {
                    mCouple.get().getOneUser().setBirth(date);
                } else if (type == DATE_TWO_USER_BIRTH_TYPE) {
                    mCouple.get().getTwoUser().setBirth(date);
                } else if (type == DATE_START_DATE_TYPE) {
                    mCouple.get().setStartDate(date);
                }
                notifyChange();
            }
        });
    }

    @Override
    public void OnAnniversaryChange() {
        setAnniversaryList();
        notifyChange();
    }

    @BindingAdapter({"setDday"})
    public static void setDday(TextView view, Couple couple) {
        if (couple != null) {
            view.setText(DateFormat.getDdayStringFromCouple(view.getContext(), couple));
        }
    }
}
