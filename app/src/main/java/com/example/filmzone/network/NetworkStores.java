package com.example.filmzone.network;

import com.example.filmzone.model.movie.Readmore.ReadmoreMovie;
import com.example.filmzone.model.movie.Search.SearchMovie;
import com.example.filmzone.model.movie.UpComing.UpComing;
import com.example.filmzone.model.movie.NowPlaying.NowPlaying;
import com.example.filmzone.model.tvshow.DiscoverTV.DiscoverTV;
import com.example.filmzone.model.tvshow.Readmore.ReadmoreTv;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkStores {

    @GET("movie/now_playing")
    Call<NowPlaying> getNowPlaying(@Query("api_key") String api_key, @Query("language") String lang);

    @GET("movie/upcoming")
    Call<UpComing> getUpComingAlert(@Query("api_key") String api_key, @Query("language") String lang);

    @GET("search/movie")
    Call<NowPlaying> getMovieSearch(@Query("api_key") String api_key, @Query("query") String query, @Query("language") String lang);

    @GET("movie/{id}")
    Call<ReadmoreMovie> getReadmoreMovie(@Path("id") long id, @Query("api_key") String api_key);

    @GET("discover/tv")
    Call<DiscoverTV> getTvShow(@Query("api_key") String api_key, @Query("language") String lang);

    @GET("search/tv")
    Call<DiscoverTV> getTvSearch(@Query("api_key") String api_key, @Query("query") String query, @Query("language") String lang);

    @GET("tv/{id}")
    Call<ReadmoreTv> getReadmoreTv(@Path("id") long id, @Query("api_key") String query);
}
