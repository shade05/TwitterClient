package com.codepath.courses.twitterclient.di.modules;

import android.app.Application;
import android.content.res.Resources;

import com.codepath.courses.twitterclient.TwitterClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private static final String TAG = "AppModule";

    Application mApplication;

    Resources mResources;

    public AppModule(Application application) {
        mApplication = application;
    }



    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return mApplication.getResources();
    }

    @Provides
    @Singleton
    TwitterClient providesTwitterClient() {
        return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, mApplication);
    }
}