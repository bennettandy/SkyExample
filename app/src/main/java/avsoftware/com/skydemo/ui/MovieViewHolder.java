package avsoftware.com.skydemo.ui;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nextfaze.poweradapters.binding.ViewHolder;

import avsoftware.com.skydemo.api.model.Movie;
import avsoftware.com.skydemo.databinding.MovieCardBinding;
import timber.log.Timber;

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

    @BindingAdapter("loadMovieImage")
    public static void setImage(ImageView view, String url) {

        try {
            Glide.with(view.getContext())
                    .applyDefaultRequestOptions(new RequestOptions())
                    .load(url).into(view);
        }
        catch (Exception e){
            Timber.e(e, "Failed to load %s with Glide", url);
        }
    }
}