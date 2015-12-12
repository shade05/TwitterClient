package com.codepath.courses.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.courses.twitterclient.models.User;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		final Intent i = new Intent(this, TimelineActivity.class);

		Log.d(TAG, "on login success has been called");

		getClient().getVerifyCredentials(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(final int statusCode, final Header[] headers, final JSONObject json) {
				User u = User.fromJSON(json);
				i.putExtra("login_user", u);
				startActivity(i);
				Toast.makeText(LoginActivity.this, "It is a success", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(final int statusCode, final Header[] headers, final Throwable throwable, final JSONArray errorResponse) {
				Log.d(TAG, "Status code : " + statusCode);
				Log.d(TAG, "Error occured" + throwable.getMessage());
			}

		});
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
