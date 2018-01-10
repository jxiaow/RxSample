package cn.xwj.rxsample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import cn.xwj.base_library.base.BaseActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulersActivity extends BaseActivity {

    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;

    private TextView mResult;

    @Override
    public void init(Bundle savedInstanceState) {
        mBtn1 = findViewById(R.id.btn_1);
        mBtn2 = findViewById(R.id.btn_2);
        mBtn3 = findViewById(R.id.btn_3);
        mResult = findViewById(R.id.result);

        mBtn1.setOnClickListener(v -> btn1());
        mBtn2.setOnClickListener(v -> btn2());
        mBtn3.setOnClickListener(v -> btn3());
    }


    private void btn3() {
        StringBuilder stringBuilder = new StringBuilder();
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            stringBuilder.append("\r\n subscribe current thread: ").append(Thread.currentThread().getName());
            emitter.onNext(11);
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .doOnNext(integer -> stringBuilder.append("\r\n accept current thread: ").append(Thread.currentThread().getName()))
                .observeOn(Schedulers.io())
                .map(integer -> {
                    stringBuilder.append("\r\n subscribe current thread: ").append(Thread.currentThread().getName());
                    return integer + " haha";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    stringBuilder.append("\r\n accept current thread: ").append(Thread.currentThread().getName());
                    mResult.setText(stringBuilder.toString());
                });
    }

    private void btn2() {
        StringBuilder stringBuilder = new StringBuilder();
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            stringBuilder.append("\r\n subscribe current thread: ").append(Thread.currentThread().getName());
            emitter.onNext(222);

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    stringBuilder.append("\r\n accept current thread: ").append(Thread.currentThread().getName());
                    mResult.setText(stringBuilder.toString());
                });
    }

    private void btn1() {
        StringBuilder stringBuilder = new StringBuilder();
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(222);
            stringBuilder.append("\r\n subscribe current thread: ").append(Thread.currentThread().getName());
            mResult.setText(stringBuilder.toString());
        }).subscribe(integer -> {
            stringBuilder.append("\r\n accept current thread: ").append(Thread.currentThread().getName());
            mResult.setText(stringBuilder.toString());
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rx_schedulers;
    }
}
