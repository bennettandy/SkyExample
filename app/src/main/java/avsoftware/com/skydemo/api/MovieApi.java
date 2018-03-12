package avsoftware.com.skydemo.api;

import java.util.List;

import avsoftware.com.skydemo.api.model.Movie;
import io.reactivex.Single;

/**
 * Created by abennett on 12/03/2018.
 */

public interface MovieApi {

    Single<List<Movie>> getMovies();
}
