package com.example.filmzone.feature.readmoreTv;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.base.ui.BasePresenter;
import com.example.filmzone.model.tvshow.Readmore.ReadmoreTv;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadmoreTvPresenter extends BasePresenter<ReadmoreTvView> {
    public ReadmoreTvPresenter(ReadmoreTvView view) {
        super.attachView(view);
    }

    public void loadData(long id){
        view.showLoading();
        Call<ReadmoreTv> readmoreTv = networkStores.getReadmoreTv(id, BuildConfig.API_KEY);
        readmoreTv.enqueue(new Callback<ReadmoreTv>() {
            @Override
            public void onResponse(Call<ReadmoreTv> call, Response<ReadmoreTv> response) {
                view.getDataSuccess(response.body());
                view.hideLoading();
            }

            @Override
            public void onFailure(Call<ReadmoreTv> call, Throwable t) {
                view.getDataFail(t.getMessage());
                view.hideLoading();
            }
        });
    }

    public static class LoadFavoriteTvAsync extends AsyncTask<Void, Void, Cursor>{
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteTvCallback> weakCallback;
        private final Uri uri;

        public LoadFavoriteTvAsync(Context context, LoadFavoriteTvCallback callback, Uri uri) {
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
            return context.getContentResolver().query(uri, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecuteFavoriteMovie(cursor);
        }
    }
}
