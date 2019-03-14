package com.example.filmzone.feature.tvshow;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.filmzone.R;
import com.example.filmzone.adapter.TvShowAdapter;
import com.example.filmzone.base.mvp.MvpFragment;
import com.example.filmzone.feature.reminder.ReminderSettingActivity;
import com.example.filmzone.model.tvshow.DiscoverTV.DiscoverTV;
import com.example.filmzone.model.tvshow.DiscoverTV.Result;
import com.example.filmzone.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends MvpFragment<TvShowPresenter> implements TvShowView, ItemClickSupport.OnItemClickListener, SearchView.OnQueryTextListener {

    private final String stateName = "TV_SHOW";

    List<Result> tvShow;
    TvShowAdapter adapter;

    @BindView(R.id.rv_tvshow)
    RecyclerView rvTvShow;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    SearchView mSearchView;




    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        if (savedInstanceState!=null){
            hideLoading();
            tvShow = savedInstanceState.getParcelableArrayList(stateName);
            initAdapter();
        }else {
            presenter.loadData();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    protected TvShowPresenter createPresenter() {
        return new TvShowPresenter(this);
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
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getDataSuccess(DiscoverTV model) {
        tvShow = model.getResults();
        initAdapter();
    }

    @Override
    public void getDataFail(String message) {

    }

    @Override
    public void moveToReadmore(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        presenter.getReadMore(tvShow.get(position), activity);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(stateName, (ArrayList<? extends Parcelable>) tvShow);
    }

    private void initAdapter(){
        adapter = new TvShowAdapter(activity);
        rvTvShow.setLayoutManager(new LinearLayoutManager(activity));
        rvTvShow.setAdapter(adapter);
        adapter.setmData(tvShow);
        ItemClickSupport.addTo(rvTvShow).setOnItemClickListener(this);
    }


}
