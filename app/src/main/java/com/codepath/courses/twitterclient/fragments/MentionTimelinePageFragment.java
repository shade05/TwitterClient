package com.codepath.courses.twitterclient.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.courses.twitterclient.TwitterClient;
import com.codepath.courses.twitterclient.di.AppController;
import com.codepath.courses.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by liyli on 2/17/15.
 */
public class MentionTimelinePageFragment extends ListFragment {

    private static  final String TAG = "MentionTimelinePageFrag";

    @Inject
    TwitterClient mTwitterClient;

    public static MentionTimelinePageFragment newInstance() {
        MentionTimelinePageFragment fragment = new MentionTimelinePageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        ((AppController) getActivity().getApplication()).getAppComponent().inject(this);
        populateTimeline();
        return view;
    }

    public void customLoadMoreDataFromApi(int offset) {
        Log.i("INFO", "load more " + offset);

        if (mTweets.size() == 0)
            return;

        Tweet lastTweet = mTweets.get(mTweets.size() - 1);
        long maxId = lastTweet.getUid();

        mTwitterClient.getMentionTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                mTweets.addAll(Tweet.fromJSONArray(response));
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    public void populateTimeline() {
        mTwitterClient.getMentionTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final JSONArray response) {
                Log.d(TAG, response.toString());
                mTweets.addAll(Tweet.fromJSONArray(response));
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(final int statusCode, final Header[] headers, final Throwable throwable, final JSONObject errorResponse) {
                Log.d(TAG, errorResponse.toString());
            }

        });
    }
}
