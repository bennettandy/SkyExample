package avsoftware.com.skydemo;

import android.support.multidex.MultiDexApplication;

import avsoftware.com.skydemo.dagger.ApplicationComponent;
import avsoftware.com.skydemo.dagger.DaggerApplicationComponent;
import avsoftware.com.skydemo.dagger.NetworkModule;

/**
 * Created by abennett on 12/03/2018.
 */

public class SkyApplication extends MultiDexApplication {

    private static SkyApplication mInstance;

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mComponent = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule(this))
                .build();
    }

    public static SkyApplication getInstance() {
        return mInstance;
    }

    public ApplicationComponent component() {
        return mComponent;
    }
}
