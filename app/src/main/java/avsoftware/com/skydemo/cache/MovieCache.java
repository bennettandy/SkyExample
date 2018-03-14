package avsoftware.com.skydemo.cache;

import android.databinding.ObservableBoolean;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import avsoftware.com.skydemo.api.MovieApi;
import avsoftware.com.skydemo.api.model.Movie;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */

public class MovieCache {

    private final static long CACHE_MAX_LIFE_MILLIS = 10 * 60 * 1000;
    public final Observable<List<Movie>> movies;
    public final ObservableBoolean isRefreshing;
    private final MovieApi mApi;
    // Movies cached as Timestamped List
    private final BehaviorRelay<Timed<List<Movie>>> cachedMovies;
    private final PublishRelay<Boolean> tryRefreshMovies;

    public MovieCache(MovieApi api) {
        mApi = api;
        cachedMovies = BehaviorRelay.createDefault(new Timed<List<Movie>>(Collections.emptyList(), 0, TimeUnit.MILLISECONDS));
        tryRefreshMovies = PublishRelay.create();

        // expose
        movies = cachedMovies.map(Timed::value);

        isRefreshing = new ObservableBoolean(false);
    }

    public void tryRefresh() {
        tryRefreshMovies.accept(true);
    }

    public Completable connectObservables() {
        return tryRefreshMovies
                .doOnNext(aBoolean -> {
                    Timber.d("X");
                })
                .withLatestFrom(cachedMovies, (aBoolean, listTimed) -> listTimed)
                .filter(listTimed -> listTimed.time() < System.currentTimeMillis() + CACHE_MAX_LIFE_MILLIS)
                .doOnNext(__ -> Timber.d("Cache is stale"))
                .doOnNext(__ -> isRefreshing.set(true))
                .flatMapSingle(__ -> mApi.getMovies())
                .timestamp(TimeUnit.MILLISECONDS)
                .doOnNext(cachedMovies)
                .doOnError(Timber::e)
                .doOnEach(__ -> isRefreshing.set(false))
                .retry()
                .subscribeOn(Schedulers.io())
                .ignoreElements();
    }

}
