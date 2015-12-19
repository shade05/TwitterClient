package com.codepath.courses.twitterclient.fragments;

/**
 * Created by deepaks on 12/18/15.
 */

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

import com.codepath.courses.twitterclient.EndLessScrollListener;
import com.codepath.courses.twitterclient.R;
import com.codepath.courses.twitterclient.RecyclerItemClickListener;
import com.codepath.courses.twitterclient.TimeUtil;
import com.codepath.courses.twitterclient.di.AppController;
import com.codepath.courses.twitterclient.models.Tweet;
import com.codepath.courses.twitterclient.models.User;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class ListFragment extends Fragment {

    private static String TAG = "InstagramPhotoListFragment";

    RecyclerView mRecyclerView;

    SwipeRefreshLayout mSwipeRefreshLayout;

    List<Tweet> mTweets;

    User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(
                R.layout.fragment_twitter_timeline_list, container, false);
        mRecyclerView = (RecyclerView) mSwipeRefreshLayout.findViewById(R.id.recyclerview);
        setupRecyclerView(mRecyclerView);

        mTweets = new ArrayList<>();
        TweetRecyclerViewAdapter tweetRecyclerViewAdapter = new TweetRecyclerViewAdapter(this.getActivity(), mTweets);
        mRecyclerView.setAdapter(tweetRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                })
        );
        mRecyclerView.addOnScrollListener(new EndLessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(final int currentpage) {
                customLoadMoreDataFromApi(currentpage);
            }
        });
        mUser = ((AppController) getActivity().getApplication()).getSignedInUser();
        return mRecyclerView;
    }

    public abstract void customLoadMoreDataFromApi(int offset);

    public abstract void populateTimeline();

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