package com.codepath.courses.twitterclient.di;

import android.app.Application;

import com.codepath.courses.twitterclient.di.component.AppComponent;
import com.codepath.courses.twitterclient.di.component.DaggerAppComponent;
import com.codepath.courses.twitterclient.di.modules.AppModule;

/**
 * Created by deepaks on 11/12/15.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    private AppComponent mAppComponent;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}