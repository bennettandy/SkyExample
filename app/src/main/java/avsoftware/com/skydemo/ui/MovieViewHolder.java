package avsoftware.com.skydemo.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;

import com.nextfaze.poweradapters.binding.ViewHolder;

import avsoftware.com.skydemo.api.model.Movie;
import avsoftware.com.skydemo.databinding.MovieCardBinding;

/**
 * Created by abennett on 28/11/2017.
 */

public class MovieViewHolder extends ViewHolder {

    private MovieCardBinding movieCardBinding;

    public MovieViewHolder(@NonNull View view) {
        super(view);
        movieCardBinding = DataBindingUtil.getBinding(view);
        if (movieCardBinding == null){
            movieCardBinding = MovieCardBinding.bind(view);
        }
    }

    public void bindViewHolder(Movie movie){
        movieCardBinding.setMovie(movie);
    }
}