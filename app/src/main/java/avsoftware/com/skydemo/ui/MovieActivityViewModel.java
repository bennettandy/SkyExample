package avsoftware.com.skydemo.ui;

import android.view.View;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.Relay;
import com.nextfaze.poweradapters.PowerAdapter;
import com.nextfaze.poweradapters.binding.Binder;
import com.nextfaze.poweradapters.binding.ViewHolderBinder;
import com.nextfaze.poweradapters.rxjava2.ObservableAdapterBuilder;

import java.util.Collections;
import java.util.List;

import avsoftware.com.skydemo.R;
import avsoftware.com.skydemo.api.model.Movie;
import avsoftware.com.skydemo.cache.MovieCache;
import io.reactivex.schedulers.Timed;

/**
 * Created by abennett on 12/03/2018.
 */

public class MovieActivityViewModel {

    private BehaviorRelay<List<Movie>> mMovies;

    private MovieCache mCache;

    public MovieActivityViewModel(MovieCache cache){
        mMovies = BehaviorRelay.createDefault(Collections.emptyList());
        mCache = cache;
    }

    public PowerAdapter getMovieAdapter(){
        ObservableAdapterBuilder<Movie> builder = new ObservableAdapterBuilder<>(movieBinder);
        builder.contents(mCache.movies);
        return builder.build();
    }

    public void tryRefresh(){
        mCache.tryRefresh();
    }

    private Binder<Movie, View> movieBinder =
            ViewHolderBinder.create(R.layout.movie_card,
                    MovieViewHolder::new, (container, movie, topicViewHolder, holder) -> {
                        topicViewHolder.bindViewHolder(movie);
                    });
}
