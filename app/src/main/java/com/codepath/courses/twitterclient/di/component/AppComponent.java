package com.codepath.courses.twitterclient.di.component;

import com.codepath.courses.twitterclient.LaunchActivity;
import com.codepath.courses.twitterclient.PostNewTweetActivity;
import com.codepath.courses.twitterclient.ProfileActivity;
import com.codepath.courses.twitterclient.TimelineActivity;
import com.codepath.courses.twitterclient.fragments.TwitterListFragment;
import com.codepath.courses.twitterclient.di.modules.AppModule;
import com.codepath.courses.twitterclient.fragments.HomeTimelinePageFragment;
import com.codepath.courses.twitterclient.fragments.MentionTimelinePageFragment;
import com.codepath.courses.twitterclient.fragments.UserTimelinePageFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by deepaks on 12/10/15.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(TimelineActivity mainActivity);

    void inject(PostNewTweetActivity postNewTweetActivity);

    void inject(TwitterListFragment instagramPhotoListFragment);

    void inject(HomeTimelinePageFragment homeTimelinePageFragment);

    void inject(MentionTimelinePageFragment mentionTimelinePageFragment);

    void inject(UserTimelinePageFragment userTimelinePageFragment);

    void inject(ProfileActivity profileActivity);

    void inject(LaunchActivity launchActivity);

}
