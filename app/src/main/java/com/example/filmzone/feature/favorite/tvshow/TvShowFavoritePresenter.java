package com.example.filmzone.feature.favorite.tvshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.filmzone.base.ui.BasePresenter;
import com.example.filmzone.feature.readmoreMovie.ReadmoreMovieActivity;
import com.example.filmzone.feature.readmoreTv.ReadmoreTvActivity;
import com.example.filmzone.model.tvshow.DiscoverTV.Result;

import java.lang.ref.WeakReference;
import static com.example.filmzone.db.DatabaseContract.FavoriteTv.CONTENT_URI;

public class TvShowFavoritePresenter extends BasePresenter<TvShowFavoriteView> {
    public TvShowFavoritePresenter(TvShowFavoriteView view) {
        super.attachView(view);
    }

    public static class LoadFavoriteTvAsycn extends AsyncTask<Void, Void, Cursor>{
        private WeakReference<Context> weakContext;
        private WeakReference<TvShowFavoriteView> weakCallback;

        public LoadFavoriteTvAsycn(Context context, TvShowFavoriteView view) {
            this.weakContext = new WeakReference<>(context);
            this.weakCallback = new WeakReference<>(view);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().showLoading();
            weakCallback.get().preExecuteFavorites();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null,null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecuteFavorites(cursor);
            weakCallback.get().hideLoading();
        }
    }

    public void getReadMore(Result result, Activity activity){
        Intent intent = new Intent(activity, ReadmoreTvActivity.class);
        intent.putExtra("id",result.getId());
        view.moveToReadMore(intent);
    }
}
