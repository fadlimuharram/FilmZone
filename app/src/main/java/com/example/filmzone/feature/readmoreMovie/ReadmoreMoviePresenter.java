package com.example.filmzone.feature.readmoreMovie;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.base.ui.BasePresenter;
import com.example.filmzone.model.movie.Readmore.ReadmoreMovie;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadmoreMoviePresenter extends BasePresenter<ReadmoreMovieView> {
    public ReadmoreMoviePresenter(ReadmoreMovieView view) {
        super.attachView(view);
    }

    public void loadData(long id){
        view.showLoading();
        Call<ReadmoreMovie> readmore = networkStores.getReadmoreMovie(id,BuildConfig.API_KEY);
        readmore.enqueue(new Callback<ReadmoreMovie>() {
            @Override
            public void onResponse(Call<ReadmoreMovie> call, Response<ReadmoreMovie> response) {
                view.getDataSuccess(response.body());
                view.hideLoading();
            }

            @Override
            public void onFailure(Call<ReadmoreMovie> call, Throwable t) {
                view.getDataFail(t.getMessage());
                view.hideLoading();
            }
        });
    }

    public static class LoadFavoriteMovieAsync extends AsyncTask<Void, Void, Cursor>{
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteMovieCallback> weakCallback;
        private final Uri uri;

        public LoadFavoriteMovieAsync(Context context, LoadFavoriteMovieCallback callback, Uri uri) {
            this.weakContext = new WeakReference<>(context);
            this.weakCallback = new WeakReference<>(callback);
            this.uri = uri;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecuteFavoriteMovie();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(uri, null,null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecuteFavoriteMovie(cursor);
        }
    }
}
