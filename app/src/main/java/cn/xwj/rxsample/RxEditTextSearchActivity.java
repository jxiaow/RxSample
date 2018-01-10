package cn.xwj.rxsample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import cn.xwj.base_library.base.BaseActivity;
import cn.xwj.easy.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RxEditTextSearchActivity extends BaseActivity {
    private EditText mETSearch;
    private TextView mResult;

    private PublishSubject<String> mPublishSubject;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void init(Bundle savedInstanceState) {
        mETSearch = findViewById(R.id.et_search);
        mResult = findViewById(R.id.result);

        mETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                startSearch(s.toString());
            }
        });


        mCompositeDisposable = new CompositeDisposable();
        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String result) {
                mResult.setText(result);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        mCompositeDisposable.add(disposableObserver);

        mPublishSubject = PublishSubject.create();
        mPublishSubject.debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .filter(s -> s.length() > 0)
                .switchMap(s -> {
            LogUtil.i("switchMap " + Thread.currentThread().getName());
            return Observable.create((ObservableOnSubscribe<String>) emitter -> {
                LogUtil.i("create " + Thread.currentThread().getName());
                try {
                    Thread.sleep((long) (100 + Math.random() * 500));
                } catch (Exception e) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
                emitter.onNext("搜索的关键词是：" + s);
                emitter.onComplete();
            });
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
    }

    private void startSearch(String s) {
        mPublishSubject.onNext(s);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rx_edit_text_search;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
