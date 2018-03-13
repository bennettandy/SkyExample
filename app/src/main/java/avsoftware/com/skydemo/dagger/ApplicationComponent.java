package avsoftware.com.skydemo.dagger;

import avsoftware.com.skydemo.cache.MovieCache;
import avsoftware.com.skydemo.ui.MovieActivity;
import dagger.Component;

/**
 * Created by abennett on 12/03/2018.
 */
@ApplicationScope
@Component(modules = {NetworkModule.class})
public interface ApplicationComponent {

    void inject(MovieActivity activity);

    MovieCache provideMovieCache();
}
