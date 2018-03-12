package avsoftware.com.skydemo.ui;

import avsoftware.com.skydemo.api.MovieApi;

/**
 * Created by abennett on 12/03/2018.
 */

public class MainActivityViewModel {

    private MovieApi mApi;

    public MainActivityViewModel(MovieApi api){
        mApi = api;
    }
}
