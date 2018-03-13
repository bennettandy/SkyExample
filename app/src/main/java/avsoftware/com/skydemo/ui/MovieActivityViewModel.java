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
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by abennett on 12/03/2018.
 */

public class MovieActivityViewModel {

    // output
    private BehaviorRelay<List<Movie>> mMovies;

    // input
    public final BehaviorRelay<String> searchString;

    // trigger
    private final PublishRelay<String> searchTrigger;

    private MovieCache mCache;

    public MovieActivityViewModel(MovieCache cache) {
        mMovies = BehaviorRelay.createDefault(Collections.emptyList());
        searchString = BehaviorRelay.createDefault("");
        searchTrigger = PublishRelay.create();
        mCache = cache;
    }

    public CompositeDisposable connectObservables() {

        CompositeDisposable disposable = new CompositeDisposable();

        // trigger filteration if movie list changes
        disposable.add(
                mCache.movies
                        .withLatestFrom(searchString, (movies, s) -> s)
                        .doOnNext(searchTrigger)
                        .retry()
                        .subscribeOn(Schedulers.computation())
                        .subscribe());

        // trigger filteration if search string changes
        disposable.add(
                searchString
                        .doOnNext(searchTrigger)
                        .retry()
                        .subscribeOn(Schedulers.computation())
                        .subscribe()
        );

        // Filter movie list
        disposable.add(searchTrigger
                .throttleFirst(3, TimeUnit.SECONDS)
                .withLatestFrom(mCache.movies, Pair::new)
                .flatMapSingle(pair -> Observable.fromIterable(pair.getValue1())
                        .filter(movie -> movie.title().contains(pair.getValue0()))
                        .toList()
                )
                .doOnNext(mMovies)
                .doOnNext(__ -> mCache.tryRefresh()) // FIXME - convoluted
                .retry()
                .subscribeOn(Schedulers.computation())
                .subscribe());

        return disposable;
    }

    public PowerAdapter getMovieAdapter() {
        ObservableAdapterBuilder<Movie> builder = new ObservableAdapterBuilder<>(movieBinder);
        builder.contents(mMovies);
        return builder.build();
    }

    public void tryRefresh() {
        mCache.tryRefresh();
    }

    private Binder<Movie, View> movieBinder =
            ViewHolderBinder.create(R.layout.movie_card,
                    MovieViewHolder::new, (container, movie, movieViewHolder, holder) -> {
                        movieViewHolder.bindViewHolder(movie);
                    });
}
