package com.example.filmzone.feature.readmoreMovie;

import android.database.Cursor;

public interface LoadFavoriteMovieCallback {
    void preExecuteFavoriteMovie();
    void postExecuteFavoriteMovie(Cursor result);
}
