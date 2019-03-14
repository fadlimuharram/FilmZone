package com.example.filmzone.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.R;
import com.example.filmzone.model.movie.NowPlaying.Result;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.CONTENT_URI;
import static com.example.filmzone.helper.MappingHelper.mapCursorToArrayListMovie;

public class StackRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Result> movieFavorite;
    private final Context mContext;
    private Cursor mCursor;


    public StackRemoteFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        mCursor = mContext.getContentResolver().query(CONTENT_URI, null,null,null,null);
        movieFavorite = mapCursorToArrayListMovie(mCursor);
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null){
            mCursor.close();
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                mCursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
                movieFavorite = mapCursorToArrayListMovie(mCursor);
            }
        };

        thread.start();

        try {
            thread.join();
        }catch (InterruptedException e){

        }
    }

    @Override
    public void onDestroy() {
        if (mCursor != null){
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return movieFavorite.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try {
            Bitmap b = Picasso.get().load(BuildConfig.POSTER_URL + BuildConfig.POSTER_WIDTH + "/" + movieFavorite.get(position).getPosterPath()).get();
            rv.setImageViewBitmap(R.id.imageView, b);
        }catch (IOException e){
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(ImageFavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
