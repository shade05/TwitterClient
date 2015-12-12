package com.codepath.courses.twitterclient.di.component;

import com.codepath.courses.twitterclient.PostNewTweetActivity;
import com.codepath.courses.twitterclient.TimelineActivity;
import com.codepath.courses.twitterclient.TwitterListFragment;
import com.codepath.courses.twitterclient.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by deepaks on 12/10/15.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(TimelineActivity mainActivity);

    void inject(TwitterListFragment instagramPhotoListFragment);

    void inject(PostNewTweetActivity postNewTweetActivity);

}
