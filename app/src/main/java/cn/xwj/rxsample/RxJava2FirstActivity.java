package cn.xwj.rxsample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import cn.xwj.base_library.base.BaseActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxJava2FirstActivity extends BaseActivity {

    private TextView mResult;
    private Button mBtn1;
    private Button mBtn2;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mResult = findViewById(R.id.result);
        mBtn1 = findViewById(R.id.btn_1);
        mBtn2 = findViewById(R.id.btn_2);
        mBtn1.setOnClickListener(v -> btn1());
        mBtn2.setOnClickListener(v -> btn2());
    }

    public void btn1() {
        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onNext(4);
            emitter.onComplete();
            emitter.onNext(5);
//            emitter.onError(new IllegalArgumentException("onError"));
        });
        Observer<Integer> observer = new Observer<Integer>() {
            StringBuilder mStringBuilder = new StringBuilder();

            @Override
            public void onSubscribe(Disposable d) {
                mStringBuilder.append("onSubscribe: ").append(d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                mStringBuilder.append("\r\n").append("onNext: ").append(integer);
            }

            @Override
            public void onError(Throwable e) {
                mStringBuilder.append("\r\n").append("onError: ").append(e);
                mResult.setText(mStringBuilder.toString());
            }

            @Override
            public void onComplete() {
                mStringBuilder.append("\r\n").append("onComplete");
                mResult.setText(mStringBuilder.toString());
            }
        };
        observable.subscribe(observer);
    }

    public void btn2() {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onError(new IllegalArgumentException("onError"));
            emitter.onNext(4);
//            emitter.onComplete();
        }).subscribe(new Observer<Integer>() {

            StringBuilder mStringBuilder = new StringBuilder();

            @Override
            public void onSubscribe(Disposable d) {
                mStringBuilder.append("onSubscribe: ").append(d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                mStringBuilder.append("\r\n").append("onNext: ").append(integer);
            }

            @Override
            public void onError(Throwable e) {
                mStringBuilder.append("\r\n").append("onError: ").append(e);
                mResult.setText(mStringBuilder.toString());
            }

            @Override
            public void onComplete() {
                mStringBuilder.append("\r\n").append("onComplete");
                mResult.setText(mStringBuilder.toString());
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_rx_java2_first;
    }
}
