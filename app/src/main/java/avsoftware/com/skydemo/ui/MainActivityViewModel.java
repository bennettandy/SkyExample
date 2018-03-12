package avsoftware.com.skydemo.ui;

import android.view.View;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.nextfaze.poweradapters.PowerAdapter;
import com.nextfaze.poweradapters.binding.Binder;
import com.nextfaze.poweradapters.binding.ViewHolderBinder;
import com.nextfaze.poweradapters.rxjava2.ObservableAdapterBuilder;

import java.util.Collections;
import java.util.List;

import avsoftware.com.skydemo.R;
import avsoftware.com.skydemo.api.MovieApi;
import avsoftware.com.skydemo.api.model.Movie;
import io.reactivex.Completable;

/**
 * Created by abennett on 12/03/2018.
 */

public class MainActivityViewModel {

    private BehaviorRelay<List<Movie>> mMovies;

    private MovieApi mApi;

    public MainActivityViewModel(MovieApi api){
        mMovies = BehaviorRelay.createDefault(Collections.emptyList());
        mApi = api;
    }

    public Completable connect(){
        return mApi.getMovies()
                .doOnSuccess(mMovies)
                .toCompletable();
    }

    public PowerAdapter getMovieAdapter(){
        ObservableAdapterBuilder<Movie> builder = new ObservableAdapterBuilder<>(movieBinder);
        builder.contents(mMovies);
        return builder.build();
    }

    private Binder<Movie, View> movieBinder =
            ViewHolderBinder.create(R.layout.movie_card,
                    MovieViewHolder::new, (container, movie, topicViewHolder, holder) -> {
                        topicViewHolder.bindViewHolder(movie);
                    });
}
