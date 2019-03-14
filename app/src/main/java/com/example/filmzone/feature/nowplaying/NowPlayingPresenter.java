package com.example.filmzone.feature.nowplaying;

import android.app.Activity;
import android.content.Intent;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.base.ui.BasePresenter;
import com.example.filmzone.feature.readmoreMovie.ReadmoreMovieActivity;
import com.example.filmzone.model.movie.NowPlaying.NowPlaying;
import com.example.filmzone.model.movie.NowPlaying.Result;
import com.example.filmzone.model.movie.Readmore.ReadmoreMovie;
import com.example.filmzone.model.movie.Search.SearchMovie;
import com.example.filmzone.utils.MovieConverter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NowPlayingPresenter extends BasePresenter<NowPlayingView> {
    public NowPlayingPresenter(NowPlayingView view) {
        super.attachView(view);
    }

    public void loadData(){
        view.showLoading();
        Call<NowPlaying> upcoming = networkStores.getNowPlaying(BuildConfig.API_KEY, MovieConverter.langApiDetection());
        upcoming.enqueue(new Callback<NowPlaying>() {
            @Override
            public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                view.getDataSuccess(response.body());
                view.hideLoading();
            }

            @Override
            public void onFailure(Call<NowPlaying> call, Throwable t) {
                view.getDataFail(t.getMessage());
                view.hideLoading();
            }
        });

    }

    public void searchData(String query){
        view.showLoading();
        Call<NowPlaying> searchMovie = networkStores.getMovieSearch(BuildConfig.API_KEY,query, MovieConverter.langApiDetection());
        searchMovie.enqueue(new Callback<NowPlaying>() {
            @Override
            public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                view.getDataSuccess(response.body());
                view.hideLoading();
            }

            @Override
            public void onFailure(Call<NowPlaying> call, Throwable t) {
                view.getDataFail(t.getMessage());
                view.hideLoading();
            }
        });
    }

    public void getReadMore(Result result, Activity activity){
        Intent intent = new Intent(activity, ReadmoreMovieActivity.class);
        intent.putExtra("id", result.getId());
        view.moveToReadMore(intent);
    }
}
