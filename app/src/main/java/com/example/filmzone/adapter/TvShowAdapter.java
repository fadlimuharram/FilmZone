package com.example.filmzone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filmzone.BuildConfig;
import com.example.filmzone.R;
import com.example.filmzone.model.tvshow.DiscoverTV.Result;
import com.example.filmzone.utils.MovieConverter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {

    private Context context;
    private List<Result> mData;

    public TvShowAdapter(Context context) {
        this.context = context;
    }

    public List<Result> getmData() {
        return mData;
    }

    public void setmData(List<Result> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_view_tvshow,viewGroup,false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textViewName.setText(getmData().get(i).getName());
        viewHolder.textViewOverview.setText(MovieConverter.textSubStr(getmData().get(i).getOverview(),105));
        Picasso.get()
                .load(BuildConfig.POSTER_URL + BuildConfig.POSTER_WIDTH + "/" + getmData().get(i).getPosterPath())
                .into(viewHolder.imageViewPoster);
    }

    @Override
    public int getItemCount() {
        return getmData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_tv_name)
        TextView textViewName;

        @BindView(R.id.txt_tv_overview)
        TextView textViewOverview;

        @BindView(R.id.iv_poster)
        ImageView imageViewPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
