package cn.xwj.base_library.swipeback;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.FrameLayout;

import cn.xwj.easy.util.LogUtil;

import static android.arch.lifecycle.Lifecycle.Event;

public class SwipeBackHelper implements LifecycleObserver {
    private Lifecycle mLifecycle;
    private Activity mActivity;
    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackHelper(LifecycleOwner owner) {
        if (owner == null || !(owner instanceof Activity)) {
            throw new IllegalArgumentException("owner must be instanceof Activity");
        }
        this.mLifecycle = owner.getLifecycle();
        this.mLifecycle.addObserver(this);
        this.mActivity = (Activity) owner;
        onActivityCreate();
    }

    public void setSwipeBackEnable(boolean enable) {
        if(getSwipeBackLayout() != null){
            getSwipeBackLayout().setEnableGesture(enable);
        }


    }
    public void setEdgeTrackingFlags(int edge){
        if(getSwipeBackLayout() != null){
            getSwipeBackLayout().setEdgeTrackingFlags(edge);
        }
    }



    @SuppressWarnings("deprecation")
    private void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = new SwipeBackLayout(mActivity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mSwipeBackLayout.setLayoutParams(layoutParams);
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
