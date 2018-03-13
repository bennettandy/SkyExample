package avsoftware.com.skydemo.dagger;

import avsoftware.com.skydemo.cache.MovieCache;
import avsoftware.com.skydemo.ui.MainActivity;
import dagger.Component;

/**
 * Created by abennett on 12/03/2018.
 */
@ApplicationScope
@Component(modules = {NetworkModule.class})
public interface ApplicationComponent {

    void inject(MainActivity activity);

    MovieCache provideMovieCache();
}
