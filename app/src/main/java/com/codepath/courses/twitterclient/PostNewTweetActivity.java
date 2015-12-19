package com.codepath.courses.twitterclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.courses.twitterclient.di.AppController;
import com.codepath.courses.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import javax.inject.Inject;

public class PostNewTweetActivity extends AppCompatActivity {

    private static final String TAG = "PostNewTweetActivity";

    @Inject
    TwitterClient mTwitterClient;

    private ImageView ivImageProfile;
    private TextView tvMyName;
    private EditText etContent;
    private Button btSubmit;

    public static Intent getIntent(Context context, User user) {
        Intent i = new Intent(context, PostNewTweetActivity.class);
        i.putExtra("login_user", user);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_tweet);
        ((AppController) getApplication()).getAppComponent().inject(this);
        init();
    }

    private void init() {
        Bundle extras = getIntent().getExtras();
        User u = (User) extras.getSerializable("login_user");
        ivImageProfile = (ImageView) findViewById(R.id.ivMyprofile);
        ivImageProfile.setImageResource(android.R.color.transparent);

        Picasso.with(this).load(u.getProfileImageUrl()).into(ivImageProfile);
        tvMyName = (TextView) findViewById(R.id.tvMyName);
        tvMyName.setText("@" + u.getName());

        etContent = (EditText) findViewById(R.id.etContent);
        btSubmit = (Button) findViewById(R.id.btTweet);


        btSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String status = etContent.getText().toString();

                if (status == null || status.length() == 0) {
                    Toast.makeText(PostNewTweetActivity.this, "empty tweet...", Toast.LENGTH_LONG).show();
                    return;
                }

                mTwitterClient.postTweet(status, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(final int statusCode, final Header[] headers, final JSONObject response) {
                        Log.d(TAG, "Success post tweer");
                        Intent intent = new Intent();
                        intent.putExtra("tweet", status);
                        setResult(RESULT_OK, intent);
                        PostNewTweetActivity.this.finish();
                    }

                    @Override
                    public void onFailure(final int statusCode, final Header[] headers, final Throwable throwable, final JSONObject errorResponse) {
                        Log.d(TAG, "Error occured" + throwable.getMessage());
                        Log.d(TAG, errorResponse.toString());
                    }
                });
            }
        });

    }
}