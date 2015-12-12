package com.codepath.courses.twitterclient;

import org.json.JSONObject;

import com.codepath.courses.twitterclient.di.AppController;
import com.codepath.courses.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

public class PostNewTweetActivity extends Activity {

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
		User u = (User)extras.getSerializable("login_user");
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
				String status = etContent.getText().toString(); 
				if (!status.equals((""))) {
					mTwitterClient.postTweet(status, new JsonHttpResponseHandler() {
						public void onSuccess(JSONObject arg0) {
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
							finish();
						}
					});

				}
				Toast.makeText(PostNewTweetActivity.this, "Post:" + status, Toast.LENGTH_SHORT).show();
			}
		});
		
	}
}