package com.example.filmzone.base.ui;

import com.example.filmzone.network.NetworkClient;
import com.example.filmzone.network.NetworkStores;

public class BasePresenter<V> {
    public V view;
    protected NetworkStores networkStores;

    public void attachView(V view){
        this.view = view;
        networkStores = NetworkClient.getRetrofit().create(NetworkStores.class);
    }

    public void dettachView(){
        this.view = null;
    }
}
