package avsoftware.com.skydemo.ui;

import android.view.View;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.nextfaze.poweradapters.PowerAdapter;
import com.nextfaze.poweradapters.binding.Binder;
import com.nextfaze.poweradapters.binding.ViewHolderBinder;
import com.nextfaze.poweradapters.rxjava2.ObservableAdapterBuilder;

import org.javatuples.Pair;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import avsoftware.com.skydemo.R;
import avsoftware.com.skydemo.api.model.Movie;
import avsoftware.com.skydemo.cache.MovieCache;
import io.reactivex.Completable;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */

public class MovieActivityViewModel {

    // output
    private BehaviorRelay<List<Movie>> mMovies;

    // input
    public final PublishRelay<String> searchString;

    private final PublishRelay<String> filterMovieList;

    private MovieCache mCache;

    public MovieActivityViewModel(MovieCache cache){
        mMovies = BehaviorRelay.createDefault(Collections.emptyList());
        searchString = PublishRelay.create();
        filterMovieList = PublishRelay.create();
        mCache = cache;
    }

    public Completable searchObservable(){
        return searchString
                .throttleFirst(3, TimeUnit.SECONDS)
                .withLatestFrom(mCache.movies, Pair::new)
                .flatMapSingle(pair -> Observable.fromIterable(pair.getValue1())
                        .filter(movie -> movie.title().contains(pair.getValue0()))
                        .toList()
                )
                .doOnNext(mMovies)
                .retry()
                .ignoreElements();
    }

    public PowerAdapter getMovieAdapter(){
        ObservableAdapterBuilder<Movie> builder = new ObservableAdapterBuilder<>(movieBinder);
        builder.contents(mMovies);
        return builder.build();
    }

    public void tryRefresh(){
        mCache.tryRefresh();
    }

    private Binder<Movie, View> movieBinder =
            ViewHolderBinder.create(R.layout.movie_card,
                    MovieViewHolder::new, (container, movie, movieViewHolder, holder) -> {
                        movieViewHolder.bindViewHolder(movie);
                    });
}
