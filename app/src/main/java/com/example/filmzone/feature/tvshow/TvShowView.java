package com.example.filmzone.feature.tvshow;

import android.content.Intent;

import com.example.filmzone.model.tvshow.DiscoverTV.DiscoverTV;

public interface TvShowView {
    void showLoading();
    void hideLoading();
    void getDataSuccess(DiscoverTV model);
    void getDataFail(String message);
    void moveToReadmore(Intent intent);
}
