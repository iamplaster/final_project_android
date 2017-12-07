package com.studio.plaster.tweetporter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

public class LoginActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences("AUTHEN_PREF", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("HAS_LOGIN", false)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginButton = findViewById(R.id.login_button_twitter);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                SharedPreferences share = getSharedPreferences("AUTHEN_PREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                editor.putString("PREF_KEY_OAUTH_TOKEN", token);
                editor.putString("PREF_KEY_OAUTH_SECRET", secret);
                editor.putBoolean("HAS_LOGIN", true);
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void byPassLogIn(){
        SharedPreferences share = getSharedPreferences("AUTHEN_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("PREF_KEY_OAUTH_TOKEN", "938798561260486656-V9CfQGgIxD9hWYKEx85LpWHSx0buJEj");
        editor.putString("PREF_KEY_OAUTH_SECRET", "P9U4banIg52MCP0MDVKGHgthknWcVZCnFguxkYVuimA8W");
        editor.putBoolean("HAS_LOGIN", true);
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}
