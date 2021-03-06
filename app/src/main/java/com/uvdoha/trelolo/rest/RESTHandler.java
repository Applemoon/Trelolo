package com.uvdoha.trelolo.rest;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.uvdoha.trelolo.utils.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Реализует HTTP протокол
// Готовит и отправляет HTTP запросы
// Обрабатывает HTTP ответы
public class RESTHandler {

    static String requestURL = "https://trello.com/1/OAuthGetRequestToken";
    static String accessURL = "https://trello.com/1/OAuthGetAccessToken";
    static String authorizeURL = "https://trello.com/1/OAuthAuthorizeToken";
    static String appName = "Trello OAuth Example";

    static String API_KEY = "be71e8b1f723f5a966aeba9357d86e83";
    static String API_SECRET = "800cd2b168004b2be0ac2c4d33d6a1dd73878ee299393043f6adb49cf753b40a";

    static String API_PREFIX = "https://api.trello.com/1/";

    private String token;
    private Context context;


    public RESTHandler(Context context) {
        this.context = context;
    }

    public String processRequest(Bundle data, Callback callback) {

        String method = data.getString("method");

        if (this.token == null) {
            this.token = context.getSharedPreferences("trelolo", context.MODE_PRIVATE).getString("token", null);
            if (this.token == null) {
                //azaz
            }
        }

        Bundle res = new Bundle();
        try {
            String result = this.urlConnectionGet(API_PREFIX + method + "?key=" + API_KEY + "&token=" + this.token);

            res.putString("result", result);
            res.putString("error", null);

            callback.success(res);
            return result;
        } catch (Exception e) {

            res.putString("result", null);
            res.putString("error", e.getMessage());

            callback.fail(res);
        }

        return "";
    }

    public String urlConnectionGet(String strUrl) throws IOException {
        HttpURLConnection connection = null;
        String str = "";
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder buf = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buf.append(line);
                }

                str = buf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        Log.i("RESULT", str);

        return str;
    }

}
