package com.uvdoha.trelolo.rest;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.uvdoha.trelolo.data.BoardsTable;
import com.uvdoha.trelolo.data.ListsTable;
import com.uvdoha.trelolo.utils.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Зеркалирует состояние данных на сервере в данные на устройстве
// Обновляет данные в БД, ставит им статус операции
// Делает запрос через RESTHandler
// Получает ответ от RESTHandler
// Обновляет данные в БД (убирает статус)
public class Processor {
    private final static String TAG = Processor.class.getName();
    ContentResolver contentResolver;

    public Processor(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void request(Intent intent, Callback callback) {
        Bundle data = intent.getExtras();

        RESTHandler handler = new RESTHandler();

        final String result = handler.processRequest(data, callback);
        final String method = data.getString("method");

        handleResponse(method, result);
    }

    void handleResponse(String method, String response) {
        if (method.equals(APIHelper.GET_BOARDS_URL)) {
            saveBoards(response);
        } else if (method.equals(APIHelper.GET_LISTS_URL)) {
            saveLists(response);
        } else if (method.equals(APIHelper.GET_CARDS_URL)) {
            saveCards(response);
        }
    }

    void saveBoards(String resultJson) {
        try {
            JSONArray boardsArrayJsonObj = new JSONArray(resultJson);
            for (int i = 0; i < boardsArrayJsonObj.length(); i++) {
                JSONObject boardJsonObj = boardsArrayJsonObj.getJSONObject(i);
                final String id = boardJsonObj.getString("id");
                final String name = boardJsonObj.getString("name");
                final Boolean closed = boardJsonObj.getBoolean("closed");

                ContentValues cv = new ContentValues();
                cv.put(BoardsTable._ID, id);
                cv.put(BoardsTable.COLUMN_NAME, name);
                cv.put(BoardsTable.COLUMN_CLOSED, closed ? 1 : 0);
                Uri newUri = contentResolver.insert(BoardsTable.CONTENT_URI, cv);
                Log.d(TAG, "insert, result Uri : " + newUri.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void saveLists(String resultJson) {
        try {
            JSONArray listsArrayJsonObj = new JSONArray(resultJson);
            for (int i = 0; i < listsArrayJsonObj.length(); i++) {
                // TODO
//                JSONObject listJsonObj = listsArrayJsonObj.getJSONObject(i);
//                final String id = listJsonObj.getString("id");
//                final String name = listJsonObj.getString("name");
//                final Boolean closed = listJsonObj.getBoolean("closed");

//                ContentValues cv = new ContentValues();
//                cv.put(ListsTable.COLUMN_ID, id);
//                cv.put(ListsTable.COLUMN_NAME, name);
//                cv.put(ListsTable.COLUMN_CLOSED, closed ? 1 : 0);
//                Uri newUri = contentResolver.insert(ListsTable.CONTENT_URI, cv);
//                Log.d(TAG, "insert, result Uri : " + newUri.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void saveCards(String resultJson) {
        try {
            JSONArray cardsArrayJsonObj = new JSONArray(resultJson);
            for (int i = 0; i < cardsArrayJsonObj.length(); i++) {
                // TODO
//                JSONObject cardJsonObj = cardsArrayJsonObj.getJSONObject(i);
//                final String id = cardJsonObj.getString("id");
//                final String name = cardJsonObj.getString("name");
//                final Boolean closed = cardJsonObj.getBoolean("closed");
//
//                ContentValues cv = new ContentValues();
//                cv.put(BoardsTable.COLUMN_ID, id);
//                cv.put(BoardsTable.COLUMN_NAME, name);
//                cv.put(BoardsTable.COLUMN_CLOSED, closed ? 1 : 0);
//                Uri newUri = contentResolver.insert(BoardsTable.CONTENT_URI, cv);
//                Log.d(TAG, "insert, result Uri : " + newUri.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
