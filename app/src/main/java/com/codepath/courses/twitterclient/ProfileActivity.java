package com.codepath.courses.twitterclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.courses.twitterclient.di.AppController;
import com.codepath.courses.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by deepaks on 12/17/15.
 */
public class ProfileActivity extends AppCompatActivity {

    @Inject
    TwitterClient mClient;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ((AppController) getApplication()).getAppComponent().inject(this);

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mUser = User.fromJSON(response);
                getSupportActionBar().setTitle("@" + mUser.getScreenName());
                populateProfileHeader();
            }
        };


        // Get the screen name from the activity that launches this
        String screenName = getIntent().getStringExtra("screen_name");

        if (screenName == null)
            mClient.getUserInfo(handler);
        else mClient.getOtherUserInfo(screenName, handler);


        //if (savedInstanceState == null) {
            //UserTimelinePageFragment fragment = UserTimelinePageFragment.newInstance(screenName);

            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.replace(R.id.flContainer, fragment);
            //ft.commit();
        //}
    }

    private void populateProfileHeader() {

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowings = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileView = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(mUser.getName());
        tvTagline.setText(mUser.getTagline());
        tvFollowers.setText(mUser.getFollowersCount() + " Followers");
        tvFollowings.setText(mUser.getFollowingsCount() + " Following");
        Picasso.with(this).load(mUser.getProfileImageUrl()).into(ivProfileView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
