package com.codepath.courses.twitterclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.courses.twitterclient.di.AppController;
import com.codepath.courses.twitterclient.models.Tweet;
import com.codepath.courses.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class TwitterListFragment extends Fragment {

    private static String TAG = "InstagramPhotoListFragment";

    @Inject
    TwitterClient mTwitterClient;

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Tweet> mTweets;

    private User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppController) getActivity().getApplication()).getAppComponent().inject(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(
                R.layout.fragment_twitter_timeline_list, container, false);
        mRecyclerView = (RecyclerView) mSwipeRefreshLayout.findViewById(R.id.recyclerview);
        setupRecyclerView(mRecyclerView);

        mTweets = new ArrayList<>();
        TweetRecyclerViewAdapter tweetRecyclerViewAdapter = new TweetRecyclerViewAdapter(this.getActivity(), mTweets);
        mRecyclerView.setAdapter(tweetRecyclerViewAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                })
        );
        populateTimeline();
        mUser = (User) getActivity().getIntent().getSerializableExtra("login_user");
        return mRecyclerView;
    }

    private void populateTimeline() {
        mTwitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final JSONArray response) {
                Log.d(TAG, response.toString());
                mTweets.addAll(Tweet.fromJSONArray(response));
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(final int statusCode, final Header[] headers, final Throwable throwable, final JSONArray errorResponse) {
                Log.d(TAG, errorResponse.toString());
            }

        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.d(TAG, "Entering into this onActivityResult fragment");
        if (resultCode == Activity.RESULT_OK) {

            if (mUser != null) {
                // only insert the tweet in timeline if the user is fetched
                String tweetBody = data.getStringExtra("tweet");
                long uid = mUser.getUid();
                String createdAt = "now";

                Tweet tweet = new Tweet(mUser, uid, tweetBody, createdAt);
                // insert the tweet at the beginning of the stream
                mTweets.add(0, tweet);
                mRecyclerView.getAdapter().notifyDataSetChanged();

            }
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    public static class TweetRecyclerViewAdapter
            extends RecyclerView.Adapter<TweetRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Tweet> mTweets;
        private Context mContext;


        public TweetRecyclerViewAdapter(Context context, List<Tweet> tweets) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mTweets = tweets;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tweet, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            viewHolder.mTweet = mTweets.get(position);
            viewHolder.mUserNameTV.setText(viewHolder.mTweet.getUser().getName());
            viewHolder.mBodyTV.setText(viewHolder.mTweet.getBody());

            String relativeTS = null;
            try {
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").parse(viewHolder.mTweet.getCreatedAt());
                relativeTS = TimeUtil.getTimeAgo(date.getTime());
            } catch (ParseException e) {
                relativeTS = "just now";
            }
            viewHolder.mTimeStampTV.setText(relativeTS);

            Picasso.with(mContext).load(viewHolder.mTweet.getUser().getProfileImageUrl()).into(viewHolder.mProfileiImageIV);
        }

        @Override
        public int getItemCount() {
            return mTweets.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mUserNameTV;
            public final TextView mBodyTV;
            public final ImageView mProfileiImageIV;
            public final TextView mTimeStampTV;
            public Tweet mTweet;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mUserNameTV = (TextView) view.findViewById(R.id.tvUserName);
                mBodyTV = (TextView) view.findViewById(R.id.tvBody);
                mProfileiImageIV = (ImageView) view.findViewById(R.id.ivProfileImage);
                mTimeStampTV = (TextView) view.findViewById(R.id.tvTimeStamp);
            }
        }
    }
}