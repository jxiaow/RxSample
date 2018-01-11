package cn.xwj.rxsample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import cn.xwj.base_library.base.BaseActivity;
import cn.xwj.easy.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RetryActivity extends BaseActivity {

    private Button mBtnRetryWhen;

    private static final String MSG_WAIT_SHORT = "wait_short";
    private static final String MSG_WAIT_LONG = "wait_long";

    private static final String[] MSG_ARRAY = new String[]{
            MSG_WAIT_SHORT,
            MSG_WAIT_SHORT,
            MSG_WAIT_LONG,
            MSG_WAIT_LONG
    };
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private int mMsgIndex;

    @Override
    public void init(Bundle savedInstanceState) {
        mBtnRetryWhen = findViewById(R.id.btn_retry);
        mBtnRetryWhen.setOnClickListener(v -> retry());
    }

    private void retry() {
        Observable<String> stringObservable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
            int msgLen = MSG_ARRAY.length;
            doWork();
            if (mMsgIndex < msgLen) {
                emitter.onError(new Throwable(MSG_ARRAY[mMsgIndex]));
                mMsgIndex++;
            } else {
                emitter.onNext("work success");
                emitter.onComplete();
            }
        }).retryWhen(throwableObservable -> throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            int mRetryCount;

            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                String message = throwable.getMessage();
                int waitTime = 0;
                switch (message) {
                    case MSG_WAIT_LONG:
                        waitTime = 4000;
                        break;
                    case MSG_WAIT_SHORT:
                        waitTime = 2000;
                        break;
                }

                LogUtil.d("发生错误，尝试等待时间=" + waitTime + ",当前重试次数=" + mRetryCount);
                mRetryCount++;
                return waitTime > 0 && mRetryCount <= 4 ? Observable.timer(waitTime, TimeUnit.MILLISECONDS)
                        : Observable.error(throwable);
            }
        }));

        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {

            @Override
            public void onNext(String value) {
                LogUtil.d("DisposableObserver onNext=" + value);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d("DisposableObserver onError=" + e);
            }

            @Override
            public void onComplete() {
                LogUtil.d("DisposableObserver onComplete");
            }
        };
        mCompositeDisposable.add(disposableObserver);
        stringObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
    }

    public void doWork() {
        int workTime = (int) (500 + Math.random() * 500);
        try {
            LogUtil.i("doWork start ...");
            Thread.sleep(workTime);
            LogUtil.i("doWork finish ...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_retry;
    }


}
