package com.example.filmzone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.TABLE_NAME;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie._ID;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.TITLE;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.OVERVIEW;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.POSTER_PATH;

public class FavoriteMovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static FavoriteMovieHelper INSTANCE;
    private static SQLiteDatabase database;

    public FavoriteMovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteMovieHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE==null){
                    INSTANCE = new FavoriteMovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() throws SQLException{
        databaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }

    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE, null, null, null, null, null, _ID + " ASC");
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE, null, _ID + " = ?", new String[]{id}, null,null,null,null);
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
