package com.example.filmzone.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.filmzone.db.FavoriteMovieHelper;
import com.example.filmzone.db.FavoriteTvHelper;

import static com.example.filmzone.db.DatabaseContract.AUTHORITY;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie;
import static com.example.filmzone.db.DatabaseContract.FavoriteTv;

public class FavoriteProvider extends ContentProvider {

    private static final int FAVORITE_MOVIE = 1;
    private static final int FAVORITE_MOVIE_ID = 2;
    private static final int FAVORITE_TV = 3;
    private static final int FAVORITE_TV_ID = 4;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, FavoriteMovie.TABLE_NAME, FAVORITE_MOVIE);
        sUriMatcher.addURI(AUTHORITY, FavoriteMovie.TABLE_NAME + "/#", FAVORITE_MOVIE_ID);
        sUriMatcher.addURI(AUTHORITY, FavoriteTv.TABLE_NAME, FAVORITE_TV);
        sUriMatcher.addURI(AUTHORITY, FavoriteTv.TABLE_NAME + "/#", FAVORITE_TV_ID);
    }

    private FavoriteMovieHelper favoriteMovieHelper;
    private FavoriteTvHelper favoriteTvHelper;

    @Override
    public boolean onCreate() {
        favoriteMovieHelper = FavoriteMovieHelper.getInstance(getContext());
        favoriteTvHelper = FavoriteTvHelper.getInstance(getContext());
        return false;
    }

    
    @Override
    public Cursor query(@NonNull Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIE:
                favoriteMovieHelper.open();
                cursor = favoriteMovieHelper.queryProvider();
                break;
            case FAVORITE_MOVIE_ID:
                favoriteMovieHelper.open();
                cursor = favoriteMovieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case FAVORITE_TV:
                favoriteTvHelper.open();
                cursor = favoriteTvHelper.queryProvider();
                break;
            case FAVORITE_TV_ID:
                favoriteTvHelper.open();
                cursor = favoriteTvHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor=null;
                break;
        }
        return cursor;
    }

    
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    
    @Override
    public Uri insert(@NonNull Uri uri,  ContentValues values) {
        Log.wtf("debugFavorite", String.valueOf(sUriMatcher.match(uri)));
        long addedMovie = 0;
        long addedTv = 0;
        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIE:
                favoriteMovieHelper.open();
                addedMovie = favoriteMovieHelper.insertProvider(values);
                break;
            case FAVORITE_TV:
                favoriteTvHelper.open();
                addedTv = favoriteTvHelper.insertProvider(values);
                break;
            default:
                addedMovie=0;
                addedTv=0;
                break;
        }

        if (addedMovie > 0){
            getContext().getContentResolver().notifyChange(FavoriteMovie.CONTENT_URI, null);
            return Uri.parse(FavoriteMovie.CONTENT_URI + "/" + addedMovie);
        }

        if (addedTv > 0){
            getContext().getContentResolver().notifyChange(FavoriteTv.CONTENT_URI,null);
            return Uri.parse(FavoriteTv.CONTENT_URI + "/" + addedTv);
        }

        return Uri.parse(FavoriteMovie.CONTENT_URI + "/" + addedMovie);

    }

    @Override
    public int delete(@NonNull Uri uri,  String selection,  String[] selectionArgs) {
        int deleted = 0;
        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIE_ID:
                favoriteMovieHelper.open();
                deleted = favoriteMovieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            case FAVORITE_TV_ID:
                favoriteTvHelper.open();
                deleted = favoriteTvHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
