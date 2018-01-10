package cn.xwj.rxsample;

import android.os.Bundle;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import cn.xwj.base_library.base.BaseActivity;
import cn.xwj.easy.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PollingActivity extends BaseActivity {

    private Button mBtnSimple;
    private Button mBtnAdvance;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void init(Bundle savedInstanceState) {
        mBtnAdvance = findViewById(R.id.btn_advance);
        mBtnSimple = findViewById(R.id.btn_simple);

        mBtnSimple.setOnClickListener(v -> simple());
        mBtnAdvance.setOnClickListener(v -> advance());
    }

    private void advance() {
        LogUtil.i("start advance....");
        Observable<Long> observable = Observable.just(0L).doOnComplete(() -> doWork())
                .repeatWhen(objectObservable ->
                        objectObservable.flatMap(new Function<Object, ObservableSource<Long>>() {
                            private int mRepeatCount;

                            @Override
                            public ObservableSource<Long> apply(Object o) throws Exception {
                                if (++mRepeatCount > 4) {
                                    return Observable.error(new Throwable("polling work finished"));
                                }
                                return Observable.timer(3000 + mRepeatCount * 1000, TimeUnit.MILLISECONDS);
                            }
                        })
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Long> disposableObserver = getDisposableObserver();
        mCompositeDisposable.add(disposableObserver);
        observable.subscribe(disposableObserver);
    }

    private void simple() {
        LogUtil.i("start simple....");
        Observable<Long> observable = Observable.intervalRange(0, 5, 0, 3000, TimeUnit.MILLISECONDS)
                .take(5).doOnNext(aLong -> doWork())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Long> disposableObserver = getDisposableObserver();
        mCompositeDisposable.add(disposableObserver);
        observable.subscribe(disposableObserver);
    }

    private DisposableObserver<Long> getDisposableObserver() {

        return new DisposableObserver<Long>() {

            @Override
            public void onNext(Long aLong) {
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.d("DisposableObserver onError, threadId=" + Thread.currentThread().getId() + ",reason=" + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtil.d("DisposableObserver onComplete, threadId=" + Thread.currentThread().getId());
            }
        };
    }

    private void doWork() {
        long workTime = (long) (500 + Math.random() * 500);
        try {
            LogUtil.d("doWork start,  threadId=" + Thread.currentThread().getId());
            Thread.sleep(workTime);
            LogUtil.d("doWork finished");
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_polling;
    }
}
