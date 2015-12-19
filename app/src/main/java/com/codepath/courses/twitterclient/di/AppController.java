package com.codepath.courses.twitterclient.di;

import android.app.Application;

import com.codepath.courses.twitterclient.di.component.AppComponent;
import com.codepath.courses.twitterclient.di.component.DaggerAppComponent;
import com.codepath.courses.twitterclient.di.modules.AppModule;
import com.codepath.courses.twitterclient.models.User;

/**
 * Created by deepaks on 11/12/15.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    private AppComponent mAppComponent;

    private User signedInUser;

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


    public User getSignedInUser() {
        return signedInUser;
    }

    public void setSignedInUser(final User signedInUser) {
        this.signedInUser = signedInUser;
    }
}