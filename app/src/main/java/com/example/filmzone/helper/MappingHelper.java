package com.example.filmzone.helper;

import android.database.Cursor;

import com.example.filmzone.model.movie.NowPlaying.Result;

import java.util.ArrayList;

import static com.example.filmzone.db.DatabaseContract.FavoriteMovie._ID;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.TITLE;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.OVERVIEW;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.POSTER_PATH;

import static com.example.filmzone.db.DatabaseContract.FavoriteTv;
public class MappingHelper {
    public static ArrayList<Result> mapCursorToArrayListMovie(Cursor resultCursor){
        ArrayList<Result> results = new ArrayList<>();
        while (resultCursor.moveToNext()){
            long id = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(_ID));
            String overview = resultCursor.getString(resultCursor.getColumnIndexOrThrow(OVERVIEW));
            String poster_path = resultCursor.getString(resultCursor.getColumnIndexOrThrow(POSTER_PATH));
            String title = resultCursor.getString(resultCursor.getColumnIndexOrThrow(TITLE));
            results.add(new Result(id, overview, poster_path, title));
        }
        return results;
    }

    public static ArrayList<com.example.filmzone.model.tvshow.DiscoverTV.Result> mapCursorToArrayListTv(Cursor resultCursor){
        ArrayList<com.example.filmzone.model.tvshow.DiscoverTV.Result> results = new ArrayList<>();
        while (resultCursor.moveToNext()){
            long id = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(FavoriteTv._ID));
            String overview = resultCursor.getString(resultCursor.getColumnIndexOrThrow(FavoriteTv.OVERVIEW));
            String poster_path = resultCursor.getString(resultCursor.getColumnIndexOrThrow(FavoriteTv.POSTER_PATH));
            String title = resultCursor.getString(resultCursor.getColumnIndexOrThrow(FavoriteTv.TITLE));
            results.add(new com.example.filmzone.model.tvshow.DiscoverTV.Result(id, title, overview, poster_path));
        }

        return results;
    }
}
