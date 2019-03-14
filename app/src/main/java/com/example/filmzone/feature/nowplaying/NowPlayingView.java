package com.example.filmzone.feature.nowplaying;

import android.content.Intent;

import com.example.filmzone.model.movie.NowPlaying.NowPlaying;
import com.example.filmzone.model.movie.NowPlaying.Result;

public interface NowPlayingView {
    void showLoading();
    void hideLoading();
    void getDataSuccess(NowPlaying model);
    void getDataFail(String message);
    void moveToReadMore(Intent intent);
}
