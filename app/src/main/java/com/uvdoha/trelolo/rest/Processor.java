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

    public Processor() {


    }

    public void request(Intent intent, Callback callback) {
        Bundle data = intent.getExtras();

        RESTHandler handler = new RESTHandler();
        handler.processRequest(data, callback);
    }

}
