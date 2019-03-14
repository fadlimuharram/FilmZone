package com.example.filmzone.feature.tvshow;



import android.app.Activity;
import android.content.Intent;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.base.ui.BasePresenter;
import com.example.filmzone.feature.readmoreTv.ReadmoreTvActivity;
import com.example.filmzone.model.tvshow.DiscoverTV.DiscoverTV;
import com.example.filmzone.model.tvshow.DiscoverTV.Result;
import com.example.filmzone.utils.MovieConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowPresenter extends BasePresenter<TvShowView> {
    public TvShowPresenter(TvShowView view) {
        super.attachView(view);
    }

    public void loadData(){
        view.showLoading();
        Call<DiscoverTV> discoverTV = networkStores.getTvShow(BuildConfig.API_KEY, MovieConverter.langApiDetection());
        discoverTV.enqueue(new Callback<DiscoverTV>() {
            @Override
            public void onResponse(Call<DiscoverTV> call, Response<DiscoverTV> response) {
                view.getDataSuccess(response.body());
                view.hideLoading();
            }

            @Override
            public void onFailure(Call<DiscoverTV> call, Throwable t) {
                view.getDataFail(t.getMessage());
                view.hideLoading();
            }
        });
    }

    public void searchData(String query){
        view.showLoading();
        Call<DiscoverTV> discoverTV = networkStores.getTvSearch(BuildConfig.API_KEY,query,MovieConverter.langApiDetection());
        discoverTV.enqueue(new Callback<DiscoverTV>() {
            @Override
            public void onResponse(Call<DiscoverTV> call, Response<DiscoverTV> response) {
                view.getDataSuccess(response.body());
                view.hideLoading();
            }

            @Override
            public void onFailure(Call<DiscoverTV> call, Throwable t) {
                view.getDataFail(t.getMessage());
                view.hideLoading();
            }
        });
    }

    public void getReadMore(Result result, Activity activity){
        Intent intent = new Intent(activity, ReadmoreTvActivity.class);
        intent.putExtra("id", result.getId());
        view.moveToReadmore(intent);
    }
}
