package com.example.filmzone.feature.favorite;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.filmzone.R;
import com.example.filmzone.adapter.SectionsPageAdapter;
import com.example.filmzone.feature.favorite.nowplaying.NowPlayingFavoriteFragment;
import com.example.filmzone.feature.favorite.tvshow.TvShowFavoriteFragment;
import com.example.filmzone.feature.reminder.ReminderSettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {


    ViewPager mViewPager;
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mToolbar = view.findViewById(R.id.toolbar);
        mViewPager = view.findViewById(R.id.vp_favorite);

        tabLayout = view.findViewById(R.id.tablayout_favorite);


        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu_2, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }else if (id == R.id.action_reminder_setting){
            Intent mIntent = new Intent(getActivity(), ReminderSettingActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new NowPlayingFavoriteFragment(),"Now Playing");
        adapter.addFragment(new TvShowFavoriteFragment(),"Tv Show");
        viewPager.setAdapter(adapter);
    }

}
