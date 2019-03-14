package com.example.filmzone.feature.favorite.tvshow;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filmzone.R;
import com.example.filmzone.adapter.MovieAdapter;
import com.example.filmzone.adapter.TvShowAdapter;
import com.example.filmzone.base.mvp.MvpFragment;
import com.example.filmzone.model.tvshow.DiscoverTV.Result;
import com.example.filmzone.utils.ItemClickSupport;

import java.util.ArrayList;

import butterknife.BindView;

import static com.example.filmzone.helper.MappingHelper.mapCursorToArrayListMovie;
import static com.example.filmzone.helper.MappingHelper.mapCursorToArrayListTv;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFavoriteFragment extends MvpFragment<TvShowFavoritePresenter> implements TvShowFavoriteView, ItemClickSupport.OnItemClickListener {

    ArrayList<Result> movieFavorite;
    private TvShowAdapter adapter;

    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;

    public TvShowFavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new TvShowFavoritePresenter.LoadFavoriteTvAsycn(getActivity(), this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show_favorite, container, false);
    }

    @Override
    protected TvShowFavoritePresenter createPresenter() {
        return new TvShowFavoritePresenter(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void preExecuteFavorites() {

    }

    @Override
    public void postExecuteFavorites(Cursor result) {
        if (result.getCount() > 0){
            movieFavorite = mapCursorToArrayListTv(result);
            initAdapter();
        }
    }

    @Override
    public void moveToReadMore(Intent intent) {
        startActivity(intent);
    }

    private void initAdapter(){
        adapter = new TvShowAdapter(activity);
        rvMovie.setLayoutManager(new LinearLayoutManager(activity));
        rvMovie.setAdapter(adapter);
        adapter.setmData(movieFavorite);
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(this);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        presenter.getReadMore(movieFavorite.get(position), activity);
    }
}
