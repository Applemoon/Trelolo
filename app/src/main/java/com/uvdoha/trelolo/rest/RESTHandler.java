package com.uvdoha.trelolo.rest;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

// Реализует HTTP протокол
// Готовит и отправляет HTTP запросы
// Обрабатывает HTTP ответы
public class RESTHandler {

    static String API_PREFIX = "https://api.trello.com/1/";
    static String GET_BOARDS_URL = "/members/dmitrydorofeev/boards";
    static String GET_LISTS_URL = "/members/{board}/lists";

    public void getBoards(RestCallback callback) {

        try {
            this.urlConnectionGet(GET_BOARDS_URL);
            callback.onSuccess();
        }
        catch (Exception e) {
            callback.onFail();
        }

    }

    public void getLists(int boardId, RestCallback callback) {
        try {
            this.urlConnectionGet(GET_LISTS_URL.replace("{board}", Integer.toString(boardId)));
        }
        catch (Exception e) {
            callback.onFail();
        }
    }

    public String urlConnectionGet( String strUrl ) throws IOException {
        HttpURLConnection connection = null;
        String str = "";
        try {
            URL url = new URL(API_PREFIX + strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream in = connection.getInputStream();
                // парсим инпут стрим
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return str;
    }
}
