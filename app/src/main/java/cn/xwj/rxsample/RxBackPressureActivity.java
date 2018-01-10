package cn.xwj.rxsample;

import android.os.Bundle;
import android.widget.Button;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import cn.xwj.base_library.base.BaseActivity;
import cn.xwj.easy.util.LogUtil;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxBackPressureActivity extends BaseActivity {

    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;
    private Button mBtn5;
    private Button mBtn6;
    private Button mBtn7;


    @Override
    public void init(Bundle savedInstanceState) {
        mBtn1 = findViewById(R.id.btn_1);
        mBtn1.setOnClickListener(v -> btn1());

        mBtn2 = findViewById(R.id.btn_2);
        mBtn2.setOnClickListener(v -> btn2());

        mBtn3 = findViewById(R.id.btn_3);
        mBtn3.setOnClickListener(v -> btn3());

        mBtn4 = findViewById(R.id.btn_4);
        mBtn4.setOnClickListener(v -> btn4());

        mBtn5 = findViewById(R.id.btn_5);
        mBtn5.setOnClickListener(v -> btn5());

        mBtn6 = findViewById(R.id.btn_6);
        mBtn6.setOnClickListener(v -> btn6());

        mBtn7 = findViewById(R.id.btn_7);
        mBtn7.setOnClickListener(v -> btn7());

    }

    private void btn7() {
        Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {
            for (int i = 0; i < 1000; i++) {
                LogUtil.i("emit " + i);
                emitter.onNext(i);
            }
            LogUtil.i("emit complete");
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.i("onSubscribe ");
                        s.request(128);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.i("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.i("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.i("onComplete");
                    }
                });

    }

    private void btn6() {
        Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {
            for (int i = 0; i < 1000; i++) {
                LogUtil.i("emit " + i);
                emitter.onNext(i);
            }
            LogUtil.i("emit complete");
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.i("onSubscribe ");
                        s.request(128);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.i("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.i("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.i("onComplete");
                    }
                });

    }

    private void btn5() {

        Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {
            for (int i = 0; i < 1000; i++) {
                LogUtil.i("emit " + i);
                emitter.onNext(i);
            }
            LogUtil.i("emit complete");
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.i("onSubscribe ");
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.i("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.i("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.i("onComplete");
                    }
                });


    }

    private void btn4() {
        Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {

            for (int i = 0; i < 129; i++) {
                LogUtil.i("emit " + i);
                emitter.onNext(i);
            }
            LogUtil.i("emit complete");
            emitter.onComplete();

        }, BackpressureStrategy.ERROR).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.i("onSubscribe ");
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.i("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.i("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.i("onComplete");
                    }
                });
    }

    private void btn3() {
        Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {
            LogUtil.i("emit 1");
            emitter.onNext(1);
            LogUtil.i("emit 2");
            emitter.onNext(2);
            LogUtil.i("emit 3");
            emitter.onNext(3);
            LogUtil.i("emit complete");
            emitter.onComplete();
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.i("onSubscribe ");
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.i("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.i("onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.i("onComplete");
                    }
                });
    }

    private void btn2() {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            int i = 0;
            while (true) {
                i++;
                emitter.onNext(i);
            }
        }).filter(integer -> integer % 100 == 0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> LogUtil.i("i: " + integer));
    }

    private void btn1() {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            int i = 0;
            while (true) {
                i++;
                emitter.onNext(i);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> LogUtil.i("i: " + integer));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rx_backpressure;
    }
}
