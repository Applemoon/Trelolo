package com.uvdoha.trelolo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.uvdoha.trelolo.rest.APIHelper;
import com.uvdoha.trelolo.utils.Callback;

import java.util.Stack;

// Синглтон
// Проверяет, запущен ли данный метод - Map(requestID, intent)
// Готовит интенты и посылает их Service'y (startService(intent))
// Обрабатывает коллбэки (binder callback) Service'a
// Вызывает коллбэки (binder callback), например, Activity
public class ServiceHelper {

    private static ServiceHelper instance = null;
    public SharedPreferences prefs;

    public static final String TAG = ServiceHelper.class.getName();
    public static final String RECEIVER = ServiceBroadcastReceiver.class.getName();

    private ServiceBroadcastReceiver receiver;

    Stack<Callback> callbacks = new Stack<>();

    private ServiceHelper(Context context) {
        IntentFilter filter = new IntentFilter(RECEIVER);
        receiver = new ServiceBroadcastReceiver();
        context.registerReceiver(receiver, filter);
    }

    public static ServiceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ServiceHelper(context);
            instance.prefs = context.getSharedPreferences("", 0);
        }

        return instance;
    }


    public void getBoards(Context context, Callback callback) {

        Bundle boardsBundle = new Bundle();
        boardsBundle.putString("method", APIHelper.GET_BOARDS_URL);

        Intent intent = new Intent(context, MyService.class);
        intent.putExtras(boardsBundle);

        callbacks.push(callback);

        context.startService(intent);
    }

    public void getLists(Context context, String board_id, Callback callback) {
        Bundle listsBundle = new Bundle();
        listsBundle.putString("method", APIHelper.GET_LISTS_URL.replace("[0-9a-zA-Z]+", board_id));

        Intent intent = new Intent(context, MyService.class);
        intent.putExtras(listsBundle);

        callbacks.push(callback);

        context.startService(intent);
    }

    public void getCards(Context context, String list_id, Callback callback) {
        Bundle cardsBundle = new Bundle();
        cardsBundle.putString("method", APIHelper.GET_CARDS_URL.replace("[0-9a-zA-Z]+", list_id));

        Intent intent = new Intent(context, MyService.class);
        intent.putExtras(cardsBundle);

        callbacks.push(callback);

        context.startService(intent);
    }

    public class ServiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "receive");
            callbacks.pop().success(intent.getExtras());

        }
    }

}
