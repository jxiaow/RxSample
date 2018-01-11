package cn.xwj.rxsample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import cn.xwj.base_library.base.BaseActivity;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class CombineLatestActivity extends BaseActivity {
    private Button mBtnLogin;
    private EditText mETUsername;
    private EditText mETPassword;

    private PublishSubject<String> mNameSubject;
    private PublishSubject<String> mPasswordSubject;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    @Override
    public void init(Bundle savedInstanceState) {
        mBtnLogin = findViewById(R.id.btn_login);
        mETUsername = findViewById(R.id.et_username);
        mETPassword = findViewById(R.id.et_password);

        mNameSubject = PublishSubject.create();
        mPasswordSubject = PublishSubject.create();

        mETUsername.addTextChangedListener(new EditTextMonitor(mNameSubject));
        mETPassword.addTextChangedListener(new EditTextMonitor(mPasswordSubject));

        Observable<Boolean> observable = Observable.combineLatest(mNameSubject, mPasswordSubject, (name, password) -> {
            int nameLen = name.length();
            int passwordLen = password.length();
            return nameLen >= 2 && nameLen <= 8 && passwordLen >= 4 && passwordLen <= 16;
        });

        DisposableObserver<Boolean> disposableObserver = new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                mBtnLogin.setText(aBoolean ? "登录" : "用户名或密码无效");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        mCompositeDisposable.add(disposableObserver);
        observable.subscribe(disposableObserver);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_combine_latest;
    }

    private class EditTextMonitor implements TextWatcher {
        private PublishSubject<String> mPublishSubject;

        public EditTextMonitor(PublishSubject<String> publishSubject) {
            this.mPublishSubject = publishSubject;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mPublishSubject.onNext(s.toString());
        }
    }
}
