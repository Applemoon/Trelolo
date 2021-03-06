package com.uvdoha.trelolo;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.uvdoha.trelolo.rest.Processor;
import com.uvdoha.trelolo.utils.Callback;

// Ловит интенты с коллбэками
// Вызывает методы Processor'a
// Обрабатывает коллбэки (binder callback) Processor'a
// Вызывает коллбэки (binder callback) ServiceHelper'a
// Завершается после завершения всех операций (!!!)
public class MyService extends IntentService {

    public static final String TAG = MyService.class.getName();

    public MyService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "--start service--");

        Processor processor = new Processor(this, getContentResolver());

        final ResultReceiver rcv = intent.getParcelableExtra("receiver");

        processor.request(intent, new Callback() {
            @Override
            public void onSuccess(Bundle data) {
                rcv.send(ServiceHelper.RESULT_OK, data);
            }

            @Override
            public void onFail(Bundle data) {
                rcv.send(ServiceHelper.RESULT_FAIL, data);
            }
        });

    }
}
