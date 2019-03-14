package com.example.filmzone.feature.favorite.tvshow;

import android.content.Intent;
import android.database.Cursor;

public interface TvShowFavoriteView {
    void showLoading();
    void hideLoading();
    void preExecuteFavorites();
    void postExecuteFavorites(Cursor result);
    void moveToReadMore(Intent intent);
}
