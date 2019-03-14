package com.example.filmzone.feature.favorite.nowplaying;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.filmzone.base.ui.BasePresenter;
import com.example.filmzone.feature.readmoreMovie.ReadmoreMovieActivity;
import com.example.filmzone.model.movie.NowPlaying.Result;
import com.example.filmzone.model.movie.Readmore.ReadmoreMovie;

import java.lang.ref.WeakReference;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.CONTENT_URI;
public class NowPlayingFavoritePresenter extends BasePresenter<NowPlayingFavoriteView> {
    public NowPlayingFavoritePresenter(NowPlayingFavoriteView view) {
        super.attachView(view);
    }

    public static class LoadFavoriteMovieAsync extends AsyncTask<Void, Void, Cursor>{
        private WeakReference<Context> weakContext;
        private WeakReference<NowPlayingFavoriteView> weakCallback;

        public LoadFavoriteMovieAsync(Context context, NowPlayingFavoriteView view) {
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
        Intent intent = new Intent(activity, ReadmoreMovieActivity.class);
        intent.putExtra("id", result.getId());
        view.moveToReadMore(intent);
    }
}
