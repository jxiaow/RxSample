package cn.xwj.rxsample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.xwj.base_library.base.BaseActivity;
import cn.xwj.easy.adapter.CommonBaseAdapter;
import cn.xwj.easy.holder.ViewHolder;
import cn.xwj.easy.holder.ViewHolderHelper;
import cn.xwj.entity.NewsEntity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xw on 2018/1/11.
 */

public class RxLoadCacheNetActivity extends BaseActivity {
    private final String TAG = "RxLoadCacheNet";

    private Button mBtnConcat;
    private Button mBtnMerge;
    private Button mBtnConcatEager;
    private Button mBtnPublish;
    private ListView mListView;

    private long cacheTime = 3000;
    private long netTime = 2000;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private CommonBaseAdapter<NewsEntity> mAdapter;

    @Override
    public void init(Bundle savedInstanceState) {
        mBtnConcat = findViewById(R.id.btn_concat);
        mBtnMerge = findViewById(R.id.btn_merge);
        mBtnConcatEager = findViewById(R.id.btn_concat_eager);
        mBtnPublish = findViewById(R.id.btn_publish);

        mListView = findViewById(R.id.list_view);

        mListView.setAdapter(mAdapter = new CommonBaseAdapter<NewsEntity>(this, R.layout.item_load, null) {
            @Override
            public void convert(ViewHolder holder, NewsEntity newsEntity, int position) {
                ViewHolderHelper helper = holder.helper();
                helper.setText(R.id.type, newsEntity.getType());
                helper.setText(R.id.list_item, newsEntity.getItem());
            }
        });

        mBtnConcat.setOnClickListener(v -> concat());
        mBtnMerge.setOnClickListener(v -> merge());
        mBtnConcatEager.setOnClickListener(v -> concatEager());
        mBtnPublish.setOnClickListener(v -> publish());
    }

    private void publish() {
        DisposableObserver<List<NewsEntity>> listObserver = getListObserver();
        mCompositeDisposable.add(listObserver);
        getNetObservable().subscribeOn(Schedulers.io())
                .publish(listObservable ->
                        Observable.merge(listObservable, getCacheObservable().subscribeOn(Schedulers.io()))
                                .takeUntil(listObservable)
                ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(listObserver);
    }

    private void concatEager() {
        DisposableObserver<List<NewsEntity>> listObserver = getListObserver();
        mCompositeDisposable.add(listObserver);
        List<Observable<List<NewsEntity>>> observables = new ArrayList<>();
        observables.add(getCacheObservable().subscribeOn(Schedulers.io()));
        observables.add(getNetObservable().subscribeOn(Schedulers.io()));
        Observable.concatEager(observables)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listObserver);
    }

    private void merge() {
        DisposableObserver<List<NewsEntity>> listObserver = getListObserver();
        mCompositeDisposable.add(listObserver);
        Observable.merge(getCacheObservable().subscribeOn(Schedulers.io()),
                getNetObservable().subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listObserver);
    }

    private void concat() {
        DisposableObserver<List<NewsEntity>> listObserver = getListObserver();
        mCompositeDisposable.add(listObserver);
        Observable.concat(getCacheObservable().subscribeOn(Schedulers.io()),
                getNetObservable().subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listObserver);
    }


    private Observable<List<NewsEntity>> getCacheObservable() {
        return Observable.create(emitter -> {
            try {
                Log.d(TAG, "开始加载缓存数据...");
                Thread.sleep(cacheTime);
                List<NewsEntity> cacheList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    NewsEntity newsEntity = new NewsEntity("缓存", "序号：" + i);
                    cacheList.add(newsEntity);
                }
                emitter.onNext(cacheList);
                emitter.onComplete();
                Log.d(TAG, "加载缓存数据结束");
            } catch (InterruptedException e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    private Observable<List<NewsEntity>> getNetObservable() {
        return Observable.create(emitter -> {
            try {
                Log.d(TAG, "开始加载网络数据...");
                Thread.sleep(netTime);
                List<NewsEntity> cacheList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    NewsEntity newsEntity = new NewsEntity("网络", "序号：" + i);
                    cacheList.add(newsEntity);
                }
                emitter.onNext(cacheList);
                emitter.onComplete();
                Log.d(TAG, "加载网络数据结束");
            } catch (InterruptedException e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rx_load_cache_net;
    }

    public DisposableObserver<List<NewsEntity>> getListObserver() {
        return new DisposableObserver<List<NewsEntity>>() {
            @Override
            public void onNext(List<NewsEntity> newsEntities) {
                updateList(newsEntities);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
    }

    private void updateList(List<NewsEntity> newsEntities) {
        mAdapter.setList(newsEntities);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
