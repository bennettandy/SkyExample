package avsoftware.com.skydemo.dagger;

import android.app.Application;

import avsoftware.com.skydemo.SkyApplication;
import avsoftware.com.skydemo.cache.MovieCache;
import avsoftware.com.skydemo.ui.MovieActivity;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;

/**
 * Created by abennett on 12/03/2018.
 */
@ApplicationScope
@Component(modules = {NetworkModule.class})
public interface ApplicationComponent extends AndroidInjector<SkyApplication> {

    void inject(SkyApplication application);

    void inject(MovieActivity activity);

    MovieCache provideMovieCache();

    @Component.Builder
    interface Builder {

        @BindsInstance
        ApplicationComponent.Builder application(Application app);

        ApplicationComponent build();
    }
}
