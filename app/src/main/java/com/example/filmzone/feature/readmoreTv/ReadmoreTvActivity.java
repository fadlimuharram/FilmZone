package com.example.filmzone.feature.readmoreTv;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.filmzone.model.tvshow.Readmore.Genre;
import com.example.filmzone.model.tvshow.Readmore.ReadmoreTv;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.filmzone.db.DatabaseContract.FavoriteTv.CONTENT_URI;
import static com.example.filmzone.db.DatabaseContract.FavoriteTv._ID;
import static com.example.filmzone.db.DatabaseContract.FavoriteTv.TITLE;
import static com.example.filmzone.db.DatabaseContract.FavoriteTv.OVERVIEW;
import static com.example.filmzone.db.DatabaseContract.FavoriteTv.POSTER_PATH;

public class ReadmoreTvActivity extends MvpActivity<ReadmoreTvPresenter> implements ReadmoreTvView, LoadFavoriteTvCallback {

    private long id;
    private Uri uri;
    private Menu menu;
    private boolean isFavorite;
    private ReadmoreTv readmoreTv;

    TagAdapter adapter;

    List<Genre> genreData;

    @BindView(R.id.rv_tag)
    RecyclerView rvTag;

    @BindView(R.id.iv_poster)
    ImageView ivPoster;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_vote_average)
    TextView tvVoteAverage;

    @BindView(R.id.tv_original_language)
    TextView tvOriginalLanguage;

    @BindView(R.id.tv_number_of_episode)
    TextView tvNumberOfEpisode;

    @BindView(R.id.tv_number_of_season)
    TextView tvNumberOfSeason;

    @BindView(R.id.tv_last_air_date)
    TextView tvLastAirDate;

    @BindView(R.id.tv_overview)
    TextView tvOverview;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected ReadmoreTvPresenter createPresenter() {
        return new ReadmoreTvPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readmore_tv);
        setSupportActionBar(mToolbar);

        id = getIntent().getLongExtra("id",0);
        presenter.loadData(id);
        uri = Uri.parse(CONTENT_URI + "/" + id);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        reLoadFavoriteTv();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.readmore_menu, menu);
        this.menu = menu;
        reLoadFavoriteTv();
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
    public void getDataSuccess(ReadmoreTv model) {
        readmoreTv = model;
        genreData = model.getGenres();
        initAdapter();

        String url = BuildConfig.POSTER_URL + BuildConfig.POSTER_WIDTH;
        Picasso.get().load(url + "/" + model.getPosterPath()).into(ivPoster);

        tvTitle.setText(model.getName());
        tvLastAirDate.setText(model.getLastAirDate());



        tvNumberOfEpisode.setText(String.valueOf(model.getNumberOfEpisodes()));
        tvNumberOfSeason.setText(String.valueOf(model.getNumberOfSeasons()));
        tvVoteAverage.setText(String.valueOf(model.getVoteAverage()));
        tvOriginalLanguage.setText(model.getOriginalLanguage());
        tvOverview.setText(model.getOverview());
    }

    @Override
    public void getDataFail(String message) {

    }

    private void initAdapter(){
        adapter = new TagAdapter(activity);
        rvTag.setLayoutManager(new LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false));
        rvTag.setAdapter(adapter);

        adapter.setmData(convertGenre());
    }

    private List<com.example.filmzone.model.movie.Readmore.Genre> convertGenre(){
        List<com.example.filmzone.model.movie.Readmore.Genre> genres = new ArrayList<>();
        for (int x=0;x<genreData.size();x++){
            com.example.filmzone.model.movie.Readmore.Genre genre = new com.example.filmzone.model.movie.Readmore.Genre();
            genre.setName(genreData.get(x).getName());
            genre.setId(genreData.get(x).getId());

            genres.add(genre);
        }
        return genres;
    }

    private void changeIconToFavorite(){
        if (menu != null){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_white_24dp));
        }
    }

    private void changeIconToUnfavorite(){
        if (menu != null){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_favorite_border_white_24dp));
        }

    }

    private void postFavorite(){
        ContentValues values = new ContentValues();
        values.put(_ID, readmoreTv.getId());
        values.put(TITLE, readmoreTv.getName());
        values.put(OVERVIEW, readmoreTv.getOverview());
        values.put(POSTER_PATH, readmoreTv.getPosterPath());
        getContentResolver().insert(CONTENT_URI, values);
        Toast.makeText(this, readmoreTv.getName() + " Added successfully to your favoirte", Toast.LENGTH_SHORT).show();
        reLoadFavoriteTv();
    }

    private void deleteFavorite(){
        getContentResolver().delete(uri, null, null);
        Toast.makeText(this, readmoreTv.getName() + " Removed from your favorite", Toast.LENGTH_SHORT).show();
        reLoadFavoriteTv();
    }

    private void reLoadFavoriteTv(){
        new ReadmoreTvPresenter.LoadFavoriteTvAsync(this, this, uri).execute();
    }

    @Override
    public void preExecuteFavoriteMovie() {

    }

    @Override
    public void postExecuteFavoriteMovie(Cursor result) {
        if (result.getCount() > 0){
            changeIconToFavorite();
            isFavorite = true;
        }else {
            changeIconToUnfavorite();
            isFavorite = false;
        }
    }
}
