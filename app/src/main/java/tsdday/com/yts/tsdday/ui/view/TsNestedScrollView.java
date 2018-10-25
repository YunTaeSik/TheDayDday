package tsdday.com.yts.tsdday.ui.view;

import android.content.Context;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class TsNestedScrollView extends NestedScrollView {
    private int mState = RecyclerView.SCROLL_STATE_IDLE;

    public interface NestedScrollViewScrollStateListener {
        void onNestedScrollViewStateChanged(int state);
    }


    public void setScrollListener(NestedScrollViewScrollStateListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    private NestedScrollViewScrollStateListener mScrollListener;

    public TsNestedScrollView(Context context) {
        super(context);
    }

    public TsNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TsNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void stopNestedScroll() {
        dispatchScrollState(RecyclerView.SCROLL_STATE_IDLE);
        super.stopNestedScroll();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        dispatchScrollState(RecyclerView.SCROLL_STATE_DRAGGING);
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }


    @Override
    public boolean startNestedScroll(int axes) {
        dispatchScrollState(RecyclerView.SCROLL_STATE_DRAGGING);
        return super.startNestedScroll(axes);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        dispatchScrollState(RecyclerView.SCROLL_STATE_DRAGGING);
        return super.startNestedScroll(axes, type);
    }

    private void dispatchScrollState(int state) {
        if (mScrollListener != null && mState != state) {
            mScrollListener.onNestedScrollViewStateChanged(state);
            mState = state;
        }
    }

}

