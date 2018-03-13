package avsoftware.com.skydemo.cache;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import avsoftware.com.skydemo.api.MovieApi;
import avsoftware.com.skydemo.api.model.Movie;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */

public class MovieCache {

    private final MovieApi mApi;

    public final BehaviorRelay<List<Movie>> movies;

    public final PublishRelay<Boolean> refreshMovies;

    public MovieCache( MovieApi api){
        mApi = api;
        movies = BehaviorRelay.createDefault(Collections.emptyList());
        refreshMovies = PublishRelay.create();
    }

    public Completable connectObservables(){
        return refreshMovies
                .doOnNext(aBoolean -> {
                    Timber.d("S");
                })
                .flatMapSingle(__ -> mApi.getMovies())
                .doOnNext(movies)
                .doOnError(Timber::e)
                .retry()
                .subscribeOn(Schedulers.io())
                .ignoreElements();
    }

}
