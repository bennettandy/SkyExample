package avsoftware.com.skydemo;

import android.app.Application;
import android.content.Context;

import avsoftware.com.skydemo.dagger.ApplicationComponent;
import avsoftware.com.skydemo.dagger.DaggerApplicationComponent;

/**
 * Created by abennett on 12/03/2018.
 */

public class SkyApplication extends Application {

    private static SkyApplication mInstance;

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mComponent = DaggerApplicationComponent.builder().build();
    }

    public static ApplicationComponent getComponent(Context ctx){
        SkyApplication app = (SkyApplication) ctx.getApplicationContext();
        return app.mComponent;
    }
}
