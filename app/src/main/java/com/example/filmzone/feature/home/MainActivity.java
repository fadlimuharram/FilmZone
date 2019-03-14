package com.example.filmzone.feature.home;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.filmzone.R;
import com.example.filmzone.feature.favorite.FavoriteFragment;
import com.example.filmzone.feature.nowplaying.NowPlayingFragment;
import com.example.filmzone.feature.tvshow.TvShowFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bn_navigation)
    BottomNavigationView btnNavigation;


    Fragment pageContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        pageContent = new NowPlayingFragment();


        if (savedInstanceState != null){
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState, "pageContent");
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_container, pageContent).commit();
        }else {
            mFragmentTransaction.add(R.id.main_frame_container, pageContent, NowPlayingFragment.class.getSimpleName());
            mFragmentTransaction.commit();
        }

        btnNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState,"pageContent", pageContent);
        super.onSaveInstanceState(outState);
    }

    private void tvShow(FragmentTransaction mFragmentTransaction){
        pageContent = new TvShowFragment();
        mFragmentTransaction.replace(R.id.main_frame_container, pageContent, TvShowFragment.class.getSimpleName());
    }

    private void nowPlaying(FragmentTransaction mFragmentTransaction){
        pageContent = new NowPlayingFragment();
        mFragmentTransaction.replace(R.id.main_frame_container, pageContent, NowPlayingFragment.class.getSimpleName());
    }

    private void favorite(FragmentTransaction mFragmentTransaction){
        pageContent = new FavoriteFragment();
        mFragmentTransaction.replace(R.id.main_frame_container, pageContent, FavoriteFragment.class.getSimpleName());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    FragmentManager mFragmentManager = getSupportFragmentManager();
                    if (mFragmentManager != null){
                        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                        switch (menuItem.getItemId()){
                            case R.id.now_playing:
                                nowPlaying(mFragmentTransaction);
                                break;
                            case R.id.tv_show:
                                tvShow(mFragmentTransaction);
                                break;
                            case R.id.favorite:
                                favorite(mFragmentTransaction);
                                break;
                        }
                        menuItem.setChecked(true);
                        mFragmentTransaction.commit();
                    }
                    return false;
                }
            };
}
