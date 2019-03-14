package com.example.filmzone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.filmzone.db.DatabaseContract.FavoriteMovie;
import com.example.filmzone.db.DatabaseContract.FavoriteTv;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "filmzonedb";
    private static final int DATABASE_VERSION = 2;

    private static final String SQL_CREATE_TABLE_FAVORITE_MOVIE = String.format("CREATE TABLE %s" +
            " (" +
            "%s INTEGER PRIMARY KEY," +
            "%s TEXT NOT NULL," +
            "%s TEXT NOT NULL," +
            "%s TEXT NOT NULL" +
            ")",
            FavoriteMovie.TABLE_NAME,
            FavoriteMovie._ID,
            FavoriteMovie.TITLE,
            FavoriteMovie.OVERVIEW,
            FavoriteMovie.POSTER_PATH
            );

    private static final String SQL_CREATE_TABLE_FAVORITE_TV = String.format("CREATE TABLE %s" +
                    " (" +
                    "%s INTEGER PRIMARY KEY," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL" +
                    ")",
            FavoriteTv.TABLE_NAME,
            FavoriteTv._ID,
            FavoriteTv.TITLE,
            FavoriteTv.OVERVIEW,
            FavoriteTv.POSTER_PATH
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovie.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteTv.TABLE_NAME);
        onCreate(db);
    }
}
