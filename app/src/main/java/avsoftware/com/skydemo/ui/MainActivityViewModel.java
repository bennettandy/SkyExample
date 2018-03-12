package avsoftware.com.skydemo.ui;

import avsoftware.com.skydemo.api.MovieApi;
import io.reactivex.Completable;
import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */

public class MainActivityViewModel {

    private MovieApi mApi;

    public MainActivityViewModel(MovieApi api){
        mApi = api;
    }

    public Completable connect(){
        return mApi.getMovies()
                .doOnSuccess(c -> {
                    Timber.d("Xxx");
                })
                .toCompletable();
    }
}
