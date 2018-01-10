package cn.xwj.rxsample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.xwj.base_library.base.BaseActivity;
import cn.xwj.easy.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxOperationalCharacterActivity extends BaseActivity {

    private TextView mResult;
    private Button mBtnMap;
    private Button mBtnFlatMap;
    private Button mBtnConcatMap;
    private Button mBtnZip;

    @Override
    public void init(Bundle savedInstanceState) {
        mResult = findViewById(R.id.result);
        mBtnMap = findViewById(R.id.btn_map);
        mBtnFlatMap = findViewById(R.id.btn_flat_map);
        mBtnConcatMap = findViewById(R.id.btn_concat_map);
        mBtnZip = findViewById(R.id.btn_zip);

        mBtnMap.setOnClickListener(v -> map());
        mBtnFlatMap.setOnClickListener(v -> flatMap());
        mBtnConcatMap.setOnClickListener(v -> concatMap());
        mBtnZip.setOnClickListener(v -> zip());
    }

    private void zip() {

        StringBuilder stringBuilder = new StringBuilder();
        Observable<Integer> integerObservable = Observable.create(emitter -> {
            emitter.onNext(1);
            LogUtil.i("integerObservable 1");
            emitter.onNext(2);
            LogUtil.d("integerObservable 2");
            emitter.onNext(3);
            LogUtil.d("integerObservable 3");
            emitter.onNext(4);
            LogUtil.d("integerObservable 4");
            emitter.onComplete();
            LogUtil.d("integerObservable onComplete");
        });

        Observable<String> stringObservable = Observable.create(emitter -> {
            emitter.onNext("A");
            LogUtil.d("stringObservable A");
            emitter.onNext("B");
            LogUtil.d("stringObservable B");
            emitter.onNext("C");
            LogUtil.d("stringObservable C");
            emitter.onComplete();
            LogUtil.d("stringObservable onComplete");
        });

        Observable.zip(integerObservable.subscribeOn(Schedulers.newThread()),
                stringObservable.subscribeOn(Schedulers.io()), (integer, s) -> integer + s)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    stringBuilder.append("\r\n" + s);
                    mResult.setText(stringBuilder.toString());
                }, throwable -> {
                    stringBuilder.append("\r\n" + throwable.getMessage());
                    mResult.setText(stringBuilder.toString());
                });
    }

    private void concatMap() {
        StringBuilder stringBuilder = new StringBuilder();
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .concatMap(integer -> {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        list.add("flatMap " + integer);
                    }
                    Random random = new Random();
                    int i = random.nextInt(10);
                    return Observable.fromIterable(list).delay(10 + i, TimeUnit.MILLISECONDS);
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    stringBuilder.append("\r\n").append(s);
                    mResult.setText(stringBuilder.toString());
                });
    }

    private void flatMap() {
        StringBuilder stringBuilder = new StringBuilder();
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .flatMap(integer -> {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        list.add("flatMap " + integer);
                    }
                    Random random = new Random();
                    int i = random.nextInt(10);
                    return Observable.fromIterable(list).delay(10 + i, TimeUnit.MILLISECONDS);
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    stringBuilder.append("\r\n").append(s);
                    mResult.setText(stringBuilder.toString());
                });
    }

    private void map() {
        StringBuilder stringBuilder = new StringBuilder();
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            stringBuilder.append("\r\n emitter: ").append(Thread.currentThread().getName());
            emitter.onNext(11);
            emitter.onNext(22);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(integer -> {
                    stringBuilder.append("\r\n map: ").append(Thread.currentThread().getName());
                    return integer + "  map";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    stringBuilder.append("\r\n accept : ").append(Thread.currentThread().getName());
                    stringBuilder.append("\r\n").append(s);
                    mResult.setText(stringBuilder.toString());
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rx_operational_character;
    }
}
