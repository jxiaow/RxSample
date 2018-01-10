package cn.xwj.rxsample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.xwj.base_library.base.BaseActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class RxBufferActivity extends BaseActivity {

    private TextView mResult;
    private Button mBtnStart;

    private PublishSubject<Double> mPublishSubject;
    private CompositeDisposable mCompositeDisposable;
    private DisposableObserver<List<Double>> disposableObserver;


    @Override
    public void init(Bundle savedInstanceState) {
        mResult = findViewById(R.id.result);
        mBtnStart = findViewById(R.id.btn_start);

        mBtnStart.setOnClickListener(v -> start());

        mCompositeDisposable = new CompositeDisposable();
        mPublishSubject = PublishSubject.create();
        disposableObserver = new DisposableObserver<List<Double>>() {
            @Override
            public void onNext(List<Double> doubles) {
                if (doubles == null || doubles.isEmpty()) {
                    return;
                }
                double result = 0;
                for (double temperature : doubles) {
                    result += temperature;
                }
                result = result / doubles.size();
                mResult.setText("过去3秒内收到了 " + doubles.size() + "个数据，平均温度：" + result);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

    }

    private void start() {
        mResult.setText("开始计算温度...");
        mPublishSubject.interval(0,250 + (long) (250 * Math.random()), TimeUnit.MILLISECONDS)
                .map(aDouble -> Math.random() * 25 + 5)
                .buffer(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rx_buffer;
    }
}
