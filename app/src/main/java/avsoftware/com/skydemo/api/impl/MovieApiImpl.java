package avsoftware.com.skydemo.api.impl;

import java.util.List;

import avsoftware.com.skydemo.api.MovieApi;
import avsoftware.com.skydemo.api.model.Movie;
import avsoftware.com.skydemo.api.model.MovieResponse;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * Created by abennett on 12/03/2018.
 */

public class MovieApiImpl implements MovieApi {

    private MovieRetrofit mApi;

    public MovieApiImpl( Retrofit retrofit){
        mApi = retrofit.create(MovieRetrofit.class);
    }

    private interface  MovieRetrofit {
        @GET("api/movies")
        Single<MovieResponse> getMovies();
    }

    @Override
    public Single<List<Movie>> getMovies() {
        return mApi.getMovies()
                .subscribeOn(Schedulers.io())
                .map(MovieResponse::data);
    }
}
