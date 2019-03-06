package tsdday.com.yts.tsdday.viewmodel.bindingAdapter;

import android.content.Context;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import tsdday.com.yts.tsdday.interactor.Interactor;
import tsdday.com.yts.tsdday.model.Album;
import tsdday.com.yts.tsdday.model.Anniversary;
import tsdday.com.yts.tsdday.ui.adapter.AlbumAdapter;
import tsdday.com.yts.tsdday.ui.adapter.AnniversaryAdapter;
import tsdday.com.yts.tsdday.ui.adapter.HomeListAdapter;
import tsdday.com.yts.tsdday.ui.adapter.AlbumItemAdapter;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;
import tsdday.com.yts.tsdday.viewmodel.CoupleViewModel;

public class SetAdapterBindingAdapter {

    @BindingAdapter({"setAlbumAdapter"})
    public static void setAlbumAdapter(RecyclerView recyclerView, List<Album> albumsList) {
        Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() != null) {
            Log.e("setAlbumAdapter", "update()");
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            Log.e("setAlbumAdapter", "create()");
            int spanCount = SharedPrefsUtils.getBooleanPreference(context, Keys.VIEW_FORMAT_IS_GRID, true) ? 2 : 1;
            AlbumAdapter mAlbumAdapter = new AlbumAdapter(context, albumsList);
            mAlbumAdapter.setHasStableIds(true);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAlbumAdapter);
        }
    }

    @BindingAdapter({"setAlbumItemAdapter"})
    public static void setAlbumItemAdapter(RecyclerView recyclerView, List<Object> itemList) {
        Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() != null) {
            Log.e("setAlbumAdapter", "update()");
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            Log.e("setAlbumAdapter", "create()");
            AlbumItemAdapter mAlbumAdapter = new AlbumItemAdapter(itemList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(mAlbumAdapter);
        }
    }


    @BindingAdapter({"setAlbumAdapter", "setInteractor"})
    public static void setAlbumAdapter(RecyclerView recyclerView, ObservableList<Album> albums, Interactor interactor) {
        Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() != null) {
            Log.e("setAlbumAdapter", "update()");
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            Log.e("setAlbumAdapter", "create()");
            AlbumAdapter mAlbumAdapter = new AlbumAdapter(context, albums);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.setAdapter(mAlbumAdapter);
        }
    }

    @BindingAdapter({"setHomeAdapter", "setModel"})
    public static void setHomeAdapter(final RecyclerView view, ObservableArrayList<Object> mAnniversaryList, CoupleViewModel model) {
        Log.e("setHomeAdapter", "setHomeAdapter");
        try {
            HomeListAdapter adapter = (HomeListAdapter) view.getAdapter();
            if (adapter != null) {
                adapter.setHomeList(mAnniversaryList);
            } else {
                adapter = new HomeListAdapter(view.getContext(), mAnniversaryList);
                adapter.setCoupleViewModel(model);
                view.setLayoutManager(new LinearLayoutManager(view.getContext()));
                view.setAdapter(adapter);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    @BindingAdapter({"setAnniversaryAdapter"})
    public static void setAnniversaryAdapter(RecyclerView view, ObservableArrayList<Anniversary> observableArrayList) {

        AnniversaryAdapter adapter = (AnniversaryAdapter) view.getAdapter();
        if (adapter != null) {
            Log.e("setAnniversaryAdapter", "update");
            adapter.setAnniversaryList(observableArrayList);
        } else {
            Log.e("setAnniversaryAdapter", "create");
            adapter = new AnniversaryAdapter(observableArrayList);
            adapter.setHasStableIds(true);
            view.setLayoutManager(new LinearLayoutManager(view.getContext()));
            view.setAdapter(adapter);
        }

    }
}

