package avsoftware.com.skydemo.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import avsoftware.com.skydemo.BuildConfig;
import avsoftware.com.skydemo.api.MovieApi;
import avsoftware.com.skydemo.api.impl.MovieApiImpl;
import avsoftware.com.skydemo.ui.MainActivityViewModel;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */
@Module
public class NetworkModule {

    private static final String API_URL = "https://movies-sample.herokuapp.com";

    private final static int DISK_CACHE_SIZE = 250 * 1024 * 1024; // 250 MB
    private final static int TIMEOUT_CONNECT = 30; // 10 secs
    private final static int TIMEOUT_READ = 30; // 15 secs
    private final static int TIMEOUT_WRITE = 30; // 15 secs

    private Context mContext;

    public NetworkModule(Context context){
        mContext = context;
    }

    @Provides
    Context provideContext(){
        return mContext;
    }

    @Provides
    static MainActivityViewModel provideMainActivityViewModel(MovieApi api) {
        return new MainActivityViewModel(api);
    }

    @Provides
    static MovieApi provideMovieApi(Retrofit retrofit){
        return new MovieApiImpl(retrofit);
    }

    @Provides
    @ApplicationScope
    static Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()//
                .addConverterFactory(GsonConverterFactory.create(gson))//
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
                .baseUrl(API_URL)//
                .client(okHttpClient)//
                .build();
    }

    @Provides
    @ApplicationScope
    static Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    @ApplicationScope
    static OkHttpClient.Builder provideOkHttpBuilder() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // Set Timeouts
        builder.connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS) //
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS) //
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS);

        return builder;
    }

    @Provides
    @ApplicationScope
    static OkHttpClient provideOkHttpClient(Context context, OkHttpClient.Builder builder) {
        // Set Cache
        Cache cache = initCache(context);
        builder.cache(cache);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }

    /**
     * Creates and returns response cache using the cache directory and size restriction
     */
    private static Cache initCache(Context context) {
        try {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir == null) {
                // Fall back to using the internal cache directory
                cacheDir = context.getCacheDir();
            }
            return new Cache(cacheDir, DISK_CACHE_SIZE);
        } catch (Exception e) {
            Timber.e(e, "Error creating OkHttp cache: " + e.getMessage());
            return null;
        }
    }
}
