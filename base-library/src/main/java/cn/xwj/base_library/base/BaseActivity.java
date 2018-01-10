package cn.xwj.base_library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.xwj.base_library.swipeback.SwipeBackHelper;
import cn.xwj.easy.util.LogUtil;
import cn.xwj.easy.util.StatusBarUtils;

/**
 * Created by xw on 2018/1/9.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private SwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        StatusBarUtils.transparentStatusBar(this);
        mSwipeBackHelper = new SwipeBackHelper(this);
        init(savedInstanceState);
    }

    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackHelper.setSwipeBackEnable(enable);
    }

    public SwipeBackHelper getSwipeBackHelper() {
        return mSwipeBackHelper;
    }

    public abstract void init(Bundle savedInstanceState);
    public abstract int getLayoutId();
}
