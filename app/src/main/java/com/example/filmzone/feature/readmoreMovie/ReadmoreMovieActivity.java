package com.example.filmzone.feature.readmoreMovie;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.R;
import com.example.filmzone.adapter.TagAdapter;
import com.example.filmzone.base.mvp.MvpActivity;
import com.example.filmzone.model.movie.Readmore.Genre;
import com.example.filmzone.model.movie.Readmore.ReadmoreMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.CONTENT_URI;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie._ID;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.TITLE;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.POSTER_PATH;
import static com.example.filmzone.db.DatabaseContract.FavoriteMovie.OVERVIEW;

public class ReadmoreMovieActivity extends MvpActivity<ReadmoreMoviePresenter> implements ReadmoreMovieView, LoadFavoriteMovieCallback {

    private long id;
    private Menu menu;
    private Uri uri;
    private ReadmoreMovie readmoreMovie;
    private Boolean isFavorite;

    List<Genre> genreData;

    TagAdapter adapter;

    @BindView(R.id.rv_tag)
    RecyclerView rvTag;

    @BindView(R.id.iv_poster)
    ImageView ivPoster;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_tag_line)
    TextView tvTagLine;

    @BindView(R.id.tv_vote_average)
    TextView tvVoteAverage;

    @BindView(R.id.tv_run_time)
    TextView tvRunTime;

    @BindView(R.id.tv_original_language)
    TextView tvOriginalLanguage;

    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;

    @BindView(R.id.tv_overview)
    TextView tvOverview;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected ReadmoreMoviePresenter createPresenter() {
        return new ReadmoreMoviePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more_movie);
        setSupportActionBar(mToolbar);

        id = getIntent().getLongExtra("id",0);

        presenter.loadData(id);
        uri = Uri.parse(CONTENT_URI + "/" + id);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        reLoadFavoriteMovie();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.readmore_menu,menu);
        this.menu = menu;
        reLoadFavoriteMovie();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }else if (item.getItemId() == R.id.action_favorite){
            if (isFavorite){
                deleteFavorite();
            }else {
                postFavorite();
            }
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
    public void getDataSuccess(ReadmoreMovie model) {
        readmoreMovie = model;
        genreData = model.getGenres();
        initAdapter();

        String url = BuildConfig.POSTER_URL + BuildConfig.POSTER_WIDTH;
        Picasso.get().load(url + "/" + model.getPosterPath()).into(ivPoster);

        tvTitle.setText(model.getTitle());
        tvOriginalLanguage.setText(model.getOriginalLanguage());
        tvRunTime.setText(String.valueOf(model.getRuntime()));
        tvTagLine.setText(model.getTagline());
        tvVoteAverage.setText(String.valueOf(model.getVoteAverage()));
        tvOverview.setText(model.getOverview());
        tvReleaseDate.setText(model.getReleaseDate());
    }

    @Override
    public void getDataFail(String message) {

    }

    private void changeIconToFavorite(){
        if (menu != null) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_white_24dp));
        }
    }

    private void changeIconToUnfavorite(){
        if (menu != null) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_white_24dp));
        }
    }

    private void postFavorite(){
        ContentValues values = new ContentValues();
        values.put(_ID,readmoreMovie.getId());
        values.put(TITLE, readmoreMovie.getTitle());
        values.put(OVERVIEW, readmoreMovie.getOverview());
        values.put(POSTER_PATH, readmoreMovie.getPosterPath());
        getContentResolver().insert(CONTENT_URI, values);
        Toast.makeText(this, readmoreMovie.getTitle() + " Added successfully ", Toast.LENGTH_SHORT).show();
        reLoadFavoriteMovie();
    }

    private void deleteFavorite(){
        getContentResolver().delete(uri, null,null);
        Toast.makeText(this, readmoreMovie.getTitle() + " Removed Frp, Your Favorite", Toast.LENGTH_SHORT).show();
        reLoadFavoriteMovie();
    }

    private void initAdapter(){
        adapter = new TagAdapter(activity);
        rvTag.setLayoutManager(new LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false));
        rvTag.setAdapter(adapter);
        adapter.setmData(genreData);

    }

    private void reLoadFavoriteMovie(){
        new ReadmoreMoviePresenter.LoadFavoriteMovieAsync(this, this, uri).execute();
    }

    @Override
    public void preExecuteFavoriteMovie() {

    }

    @Override
    public void postExecuteFavoriteMovie(Cursor result) {
//        Log.wtf("debuhh", String.valueOf(result.getCount()));
        if (result.getCount() > 0){
            changeIconToFavorite();
            isFavorite = true;
        }else {
            changeIconToUnfavorite();
            isFavorite = false;
        }
    }
}
