package avsoftware.com.skydemo.ui;

import android.databinding.ObservableBoolean;
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
import java.util.Locale;

import avsoftware.com.skydemo.R;
import avsoftware.com.skydemo.api.model.Movie;
import avsoftware.com.skydemo.cache.MovieCache;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */

public class MovieActivityViewModel {

    // flag to tell ui the movie list is being refreshed
    public final ObservableBoolean isRefreshing;

    // search string from UI
    final BehaviorRelay<String> searchString;

    // triggers filtering of movie list
    private final PublishRelay<String> searchTrigger;

    // filtered movie list for display
    private final BehaviorRelay<List<Movie>> mMovies;

    // Movie cache provides movie list
    private final MovieCache mCache;

    // UI Binder for Recycler Adapter
    private Binder<Movie, View> movieBinder =
            ViewHolderBinder.create(R.layout.movie_card,
                    MovieViewHolder::new, (container, movie, movieViewHolder, holder) -> {
                        movieViewHolder.bindViewHolder(movie);
                    });

    public MovieActivityViewModel(MovieCache cache) {
        mMovies = BehaviorRelay.createDefault(Collections.emptyList());
        searchString = BehaviorRelay.createDefault("");
        searchTrigger = PublishRelay.create();
        mCache = cache;
        isRefreshing = mCache.isRefreshing;
    }

    CompositeDisposable connectObservables() {

        CompositeDisposable disposable = new CompositeDisposable();

        // trigger filteration if the movie list changes
        disposable.add(
                mCache.movies
                        .doOnNext(movies -> {
                            Timber.d("Movies loaded");
                        })
                        .withLatestFrom(searchString, (movies, s) -> s)
                        .doOnNext(searchTrigger)
                        .subscribeOn(Schedulers.computation())
                        .subscribe());

        // trigger filteration if search string changes
        disposable.add(
                searchString
                        .doOnNext(searchTrigger)
                        .doOnNext(__ -> mCache.tryRefresh()) // also try cache refresh?
                        .subscribeOn(Schedulers.computation())
                        .subscribe()
        );

        // Filter movie list when triggered by either search change or arrival of new movie list
        disposable.add(searchTrigger
                .withLatestFrom(mCache.movies, Pair::new)
                .flatMapSingle(pair -> Observable.fromIterable(pair.getValue1())
                        // filter movies depending on search string
                        .filter(movie -> {
                            // lower case strings before matching
                            String search = pair.getValue0().toLowerCase(Locale.getDefault());
                            String title = movie.title().toLowerCase(Locale.getDefault());
                            String genre = movie.genre().toLowerCase(Locale.getDefault());
                            if (search.isEmpty()) {
                                // always match if search string is empty
                                return true;
                            }
                            // match title or genre
                            return title.contains(search) || genre.contains(search);
                        })
                        .toList()
                )
                .doOnNext(movies -> Timber.d("Filtered %d", movies.size()))
                .doOnNext(mMovies)
                .retry()
                .subscribeOn(Schedulers.computation())
                .subscribe());

        return disposable;
    }

    /**
     * Power adapter binds Observable<List<Movie>> to Movie Card
     *
     * Nice library
     * https://github.com/NextFaze/power-adapters
     */
    public PowerAdapter getMovieAdapter() {

        Observable<List<Movie>> movieObservable = mMovies
                .doOnSubscribe(disposable -> mCache.tryRefresh())
                .doOnNext(movies -> Timber.d("Adapter received %d movies", movies.size()));

        ObservableAdapterBuilder<Movie> builder = new ObservableAdapterBuilder<>(movieBinder);
        builder.contents(movieObservable); // will replace full contents with each event
        return builder.build();
    }

}
