package cn.xwj.base_library.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.xwj.base_library.swipeback.SwipeBackActivityHelper;
import cn.xwj.easy.util.StatusBarUtils;

/**
 * Created by xw on 2018/1/9.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private SwipeBackActivityHelper mSwipeBackActivityHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        StatusBarUtils.transparentStatusBar(this);
        mSwipeBackActivityHelper = new SwipeBackActivityHelper(this);
        init(savedInstanceState);
    }


    public SwipeBackActivityHelper getSwipeBackHelper() {
        return mSwipeBackActivityHelper;
    }

    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackActivityHelper.getSwipeBackLayout().setEnableGesture(enable);
    }

    public void init(Bundle savedInstanceState) {
    }

    public abstract int getLayoutId();
}
