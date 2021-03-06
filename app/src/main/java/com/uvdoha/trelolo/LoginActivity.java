package com.uvdoha.trelolo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by dmitry on 16.05.15.
 */
public class LoginActivity extends Activity {

    View mProgressView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Log.d("login", "activity started");
        mProgressView = findViewById(R.id.login_progress2);

        String token = getSharedPreferences("trelolo", MODE_PRIVATE).getString("token", null);

        if (token == null || !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cb", false)) {
            showLogin();
        } else {
            showBoards();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebView view = (WebView) findViewById(R.id.webView);
        view.clearHistory();
        view.clearFormData();
        view.clearCache(true);
        showLogin();
    }

    public void showLogin() {

        WebView view = (WebView) findViewById(R.id.webView);
        view.requestFocus(View.FOCUS_DOWN);
        view.setVisibility(View.INVISIBLE);
        view.getSettings().setJavaScriptEnabled(true);
        view.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        showProgress(true);

        view.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function() { " + "document.getElementsByClassName('deny')[0].style.display = 'none'; " + "})()");
                showProgress(false);
                view.setVisibility(View.VISIBLE);
                if (url.startsWith("https://trello.com/1/token/approve")) {
                    view.setVisibility(View.INVISIBLE);
                    view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('pre')[0].innerText);");
                }
            }

        });
        view.loadUrl("https://trello.com/1/authorize?key=be71e8b1f723f5a966aeba9357d86e83&name=Trellolo&expiration=never&response_type=token&scope=read,write");

    }

    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void showBoards() {
        Intent intent = new Intent(LoginActivity.this, BoardsActivity.class);
        startActivity(intent);
    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String token)
        {
            Log.d("Token", token);

            token = token.replace("\n", "").replace(" ", "");

            getSharedPreferences("trelolo", MODE_PRIVATE).edit().putString("token", token).commit();

            showBoards();
        }
    }

}
