package com.codepath.courses.twitterclient;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.courses.twitterclient.fragments.HomeTimelinePageFragment;
import com.codepath.courses.twitterclient.fragments.MentionTimelinePageFragment;

/**
 * Created by deepaks on 12/17/15.
 */
public class LaunchFragmentPagerAdapter extends FragmentPagerAdapter {

    final private String tabTitles[] = new String[] { "Home", "Mentions" };
    HomeTimelinePageFragment homeTimelinePageFragment;

    public LaunchFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        homeTimelinePageFragment = HomeTimelinePageFragment.newInstance();


    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        if (position == 0) {
            return homeTimelinePageFragment;
        }
        if (position == 1)
            return MentionTimelinePageFragment.newInstance();

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}