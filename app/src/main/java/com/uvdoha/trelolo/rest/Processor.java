package com.uvdoha.trelolo.rest;


import android.content.Intent;
import android.os.Bundle;

import com.uvdoha.trelolo.utils.Callback;

// Зеркалирует состояние данных на сервере в данные на устройстве
// Обновляет данные в БД, ставит им статус операции
// Делает запрос через RESTHandler
// Получает ответ от RESTHandler
// Обновляет данные в БД (убирает статус)
public class Processor {
    public Processor() {}

    public void request(Intent intent, Callback callback) {
        Bundle data = intent.getExtras();

        RESTHandler handler = new RESTHandler();
        
        final String result = handler.processRequest(data, callback);
        final String method = data.getString("method");

        if (method.equals(APIHelper.GET_BOARDS_URL)) {
            saveBoards(result);
        } else if (method.equals(APIHelper.GET_LISTS_URL)) {
            saveLists(result);
        } else if (method.equals(APIHelper.GET_CARDS_URL)) {
            saveCards(result);
        }
    }

    void saveBoards(String resultJson) {
        // TODO
    }

    void saveLists(String resultJson) {
        // TODO
    }

    void saveCards(String resultJson) {
        // TODO
    }
}
