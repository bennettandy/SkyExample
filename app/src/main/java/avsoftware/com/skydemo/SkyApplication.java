package avsoftware.com.skydemo;

import android.support.multidex.MultiDexApplication;

import javax.inject.Inject;

import avsoftware.com.skydemo.cache.MovieCache;
import avsoftware.com.skydemo.dagger.ApplicationComponent;
import avsoftware.com.skydemo.dagger.DaggerApplicationComponent;
import avsoftware.com.skydemo.dagger.NetworkModule;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */

public class SkyApplication extends DaggerApplication {

    private static SkyApplication mInstance;

    @Inject
    protected ApplicationComponent mComponent;

    private CompositeDisposable mDisposabble;

    public static SkyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

//        // disposable leak here, but we need it to survive with the App Lifecycke
//        mDisposabble = new CompositeDisposable();
//
//        mComponent = DaggerApplicationComponent.builder()
//                .networkModule(new NetworkModule(this))
//                .build();

        connectCaches();
    }

    private void connectCaches() {
        MovieCache cache = mComponent.provideMovieCache();
        mDisposabble.add(cache
                .connectObservables().subscribe(() -> {
                }, Timber::e));
    }

//    public ApplicationComponent component() {
//        return mComponent;
//    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }
}
