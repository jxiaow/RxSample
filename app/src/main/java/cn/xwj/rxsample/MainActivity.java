package cn.xwj.rxsample;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cn.xwj.base_library.base.BaseActivity;
import cn.xwj.easy.E;

public class MainActivity extends BaseActivity {

    private ListView mListView;
    private static final String[] NAMES = {"初识 RxJava", "线程调度", "操作符", "背压",
            "一段时间内的平均温度", "联想搜索", "定时轮询"};
    private static final Class[] ACTIVITIES = {RxJava2FirstActivity.class,
            RxSchedulersActivity.class, RxOperationalCharacterActivity.class,
            RxBackPressureActivity.class, RxBufferActivity.class,
            RxEditTextSearchActivity.class, PollingActivity.class
    };

    @Override
    public void init(Bundle savedInstanceState) {
        mListView = findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NAMES));
        mListView.setOnItemClickListener((parent, view, position, id) ->
                E.action(this).actionStart(ACTIVITIES[position])
        );
        setSwipeBackEnable(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
