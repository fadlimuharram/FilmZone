package com.example.filmzone.feature.readmoreTv;

import android.database.Cursor;

public interface LoadFavoriteTvCallback {
    void preExecuteFavoriteMovie();
    void postExecuteFavoriteMovie(Cursor result);
}
