package com.example.filmzone.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.filmzone.base.ui.BaseActivity;
import com.example.filmzone.base.ui.BasePresenter;

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P presenter;
    protected abstract P createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.dettachView();
        }
    }
}
