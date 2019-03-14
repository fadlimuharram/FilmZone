package com.example.filmzone.feature.readmoreTv;

import com.example.filmzone.model.tvshow.Readmore.ReadmoreTv;

public interface ReadmoreTvView {
    void showLoading();
    void hideLoading();
    void getDataSuccess(ReadmoreTv model);
    void getDataFail(String message);
}
