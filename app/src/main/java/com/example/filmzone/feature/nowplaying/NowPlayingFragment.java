package com.example.filmzone.feature.nowplaying;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.R;
import com.example.filmzone.adapter.MovieAdapter;
import com.example.filmzone.base.mvp.MvpFragment;
import com.example.filmzone.feature.reminder.ReminderSettingActivity;
import com.example.filmzone.model.movie.NowPlaying.NowPlaying;
import com.example.filmzone.model.movie.NowPlaying.Result;
import com.example.filmzone.model.movie.Search.SearchMovie;
import com.example.filmzone.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends MvpFragment<NowPlayingPresenter> implements NowPlayingView, ItemClickSupport.OnItemClickListener, SearchView.OnQueryTextListener {

    private final String stateName = "MOVIE_NOW_PLAYING";

    List<Result> movieNowPlaying;
    MovieAdapter adapter;

    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    SearchView mSearchView;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        if (savedInstanceState != null){
            hideLoading();
            movieNowPlaying = savedInstanceState.getParcelableArrayList(stateName);
            initAdapter();
        }else {
            presenter.loadData();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    protected NowPlayingPresenter createPresenter() {
        return new NowPlayingPresenter(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem mItem = menu.findItem(R.id.action_to_search);
        mSearchView = (SearchView) mItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getDataSuccess(NowPlaying model) {
        movieNowPlaying = model.getResults();
        initAdapter();
    }


    @Override
    public void getDataFail(String message) {

    }

    @Override
    public void moveToReadMore(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        presenter.getReadMore(movieNowPlaying.get(position), activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }else if (id == R.id.action_reminder_setting){
            Intent mIntent = new Intent(activity, ReminderSettingActivity.class);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(stateName, (ArrayList<? extends Parcelable>) movieNowPlaying);
    }

    private void initAdapter(){
        adapter = new MovieAdapter(activity);
        rvMovie.setLayoutManager(new LinearLayoutManager(activity));
        rvMovie.setAdapter(adapter);
        adapter.setmData(movieNowPlaying);
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.searchData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
