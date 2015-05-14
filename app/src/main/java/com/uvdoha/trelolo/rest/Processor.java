package com.uvdoha.trelolo.rest;


// Зеркалирует состояние данных на сервере в данные на устройстве
// Обновляет данные в БД, ставит им статус операции
// Делает запрос через RESTHandler
// Получает ответ от RESTHandler
// Обновляет данные в БД (убирает статус)
class Processor {

    public Processor() {

        RESTHandler rest = new RESTHandler();
        rest.getBoards(new RestCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail() {

            }
        });

    }
}
