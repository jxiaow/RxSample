package cn.xwj.base_library.swipeback;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import cn.xwj.base_library.R;

import static android.arch.lifecycle.Lifecycle.Event;

/**
 * @author Yrom
 */
public class SwipeBackActivityHelper implements LifecycleObserver {
    private Lifecycle mLifecycle;
    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(LifecycleOwner owner) {
        if (owner == null || !(owner instanceof Activity)) {
            throw new IllegalArgumentException("owner must be instanceof Activity");
        }
        this.mLifecycle = owner.getLifecycle();
        this.mLifecycle.addObserver(this);
        this.mActivity = (Activity) owner;
        onActivityCreate();
    }


    @SuppressWarnings("deprecation")
    private void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(
                R.layout.swipeback_layout, null);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Utils.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {
            }
        });
    }

    @OnLifecycleEvent(Event.ON_CREATE)
    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    @OnLifecycleEvent(Event.ON_DESTROY)
    public void onDestroy() {
        if (mLifecycle != null) {
            this.mLifecycle.removeObserver(this);
        }
    }
}
