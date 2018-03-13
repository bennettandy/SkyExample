package avsoftware.com.skydemo.cache;

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

    private final MovieApi mApi;

    // Movies cached as Timestamped List
    private final BehaviorRelay<Timed<List<Movie>>> cachedMovies;

    public final Observable<List<Movie>> movies;

    public final PublishRelay<Boolean> tryRefreshMovies;

    public MovieCache( MovieApi api){
        mApi = api;
        cachedMovies = BehaviorRelay.createDefault(new Timed<List<Movie>>(Collections.emptyList(), 0, TimeUnit.MILLISECONDS));
        tryRefreshMovies = PublishRelay.create();

        // expose
        movies = cachedMovies.map(Timed::value);
    }

    public Completable connectObservables(){
        return tryRefreshMovies
                .withLatestFrom(cachedMovies, (aBoolean, listTimed) -> listTimed )
                .filter( listTimed -> listTimed.time() < System.currentTimeMillis() + CACHE_MAX_LIFE_MILLIS )
                .doOnNext(__ -> Timber.d("Cache is stale"))
                .flatMapSingle(__ -> mApi.getMovies())
                .timestamp(TimeUnit.MILLISECONDS)
                .doOnNext(cachedMovies)
                .doOnError(Timber::e)
                .retry()
                .subscribeOn(Schedulers.io())
                .ignoreElements();
    }

}
