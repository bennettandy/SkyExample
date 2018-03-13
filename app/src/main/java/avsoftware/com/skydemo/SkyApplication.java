package avsoftware.com.skydemo;

import android.support.multidex.MultiDexApplication;

import avsoftware.com.skydemo.cache.MovieCache;
import avsoftware.com.skydemo.dagger.ApplicationComponent;
import avsoftware.com.skydemo.dagger.DaggerApplicationComponent;
import avsoftware.com.skydemo.dagger.NetworkModule;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */

public class SkyApplication extends MultiDexApplication {

    private static SkyApplication mInstance;

    private ApplicationComponent mComponent;

    private CompositeDisposable mDisposabble;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        // disposable leak here, but we need it to survive with the App Lifecycke
        mDisposabble = new CompositeDisposable();

        mComponent = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule(this))
                .build();

        connectCaches();
    }

    private void connectCaches() {
        MovieCache cache = mComponent.provideMovieCache();

        mDisposabble.add( cache.connectObservables().subscribe(() -> {}, Timber::e));

        // try to pre-populate
        cache.tryRefresh();

    }

    public static SkyApplication getInstance() {
        return mInstance;
    }

    public ApplicationComponent component() {
        return mComponent;
    }
}
