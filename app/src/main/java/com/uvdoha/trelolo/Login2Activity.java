package com.uvdoha.trelolo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by dmitry on 16.05.15.
 */
public class Login2Activity extends Activity {

    View mProgressView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mProgressView = findViewById(R.id.login_progress2);

        WebView view = (WebView) findViewById(R.id.webView);
        view.requestFocus(View.FOCUS_DOWN);
        view.getSettings().setJavaScriptEnabled(true);
        view.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        showProgress(true);

        view.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                showProgress(false);
                if (url.startsWith("https://trello.com/1/token/approve")) {
                    view.setVisibility(View.INVISIBLE);
                    view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('pre')[0].innerText);");
                }
            }

        });
        view.loadUrl("https://trello.com/1/authorize?key=be71e8b1f723f5a966aeba9357d86e83&name=Trellolo&expiration=never&response_type=token&scope=read,write");

    }

    public void showProgress(final boolean show) {
        // В Honeycomb MR2 у нас есть API ViewPropertyAnimator, который позволяет делать
        // очень простую анимацию. Если доступно, используем этот API для затухания
        // вращалки диалога.
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
            // Если API ViewPropertyAnimator недоступен, просто показываем и прячем нужные компоненты.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String token)
        {
            Log.d("Token", token);

            token = token.replace("\n", "").replace(" ", "");

            Bundle tokenBundle = new Bundle();
            tokenBundle.putString("token", token);

            Intent intent = new Intent(Login2Activity.this, BoardsActivity.class);
            intent.putExtras(tokenBundle);

            startActivity(intent);
        }
    }

}
