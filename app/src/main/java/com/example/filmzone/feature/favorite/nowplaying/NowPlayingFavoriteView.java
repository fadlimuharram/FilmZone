package com.example.filmzone.feature.favorite.nowplaying;

import android.content.Intent;
import android.database.Cursor;

import com.example.filmzone.model.movie.NowPlaying.NowPlaying;

public interface NowPlayingFavoriteView {
    void showLoading();
    void hideLoading();
    void preExecuteFavorites();
    void postExecuteFavorites(Cursor result);
    void moveToReadMore(Intent intent);
}
