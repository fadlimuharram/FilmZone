package com.example.filmzone.feature.readmoreMovie;

import com.example.filmzone.model.movie.Readmore.ReadmoreMovie;

public interface ReadmoreMovieView {
    void showLoading();
    void hideLoading();
    void getDataSuccess(ReadmoreMovie model);
    void getDataFail(String message);
}
