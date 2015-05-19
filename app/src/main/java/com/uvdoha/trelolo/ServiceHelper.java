package com.uvdoha.trelolo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.uvdoha.trelolo.rest.APIHelper;
import com.uvdoha.trelolo.utils.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// Синглтон
// Проверяет, запущен ли данный метод - Map(requestID, intent)
// Готовит интенты и посылает их Service'y (startService(intent))
// Обрабатывает коллбэки (binder callback) Service'a
// Вызывает коллбэки (binder callback), например, Activity
public class ServiceHelper {

    private static ServiceHelper instance = null;

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
        }

        return instance;
    }


    public void getBoards(Context context, Callback callback) {
        Bundle boardsBundle = new Bundle();
        boardsBundle.putString("method", APIHelper.GET_BOARDS_URL);

        startService(context, callback, boardsBundle);
    }

    public void getBoards(String token, Context context, Callback callback) {
        Bundle boardsBundle = new Bundle();
        boardsBundle.putString("method", APIHelper.GET_BOARDS_URL);
        boardsBundle.putString("token", token);

        startService(context, callback, boardsBundle);
    }

    public void getLists(Context context, Callback callback, String boardId) {
        Bundle listsBundle = new Bundle();
        final String method = String.format(APIHelper.GET_LISTS_URL, boardId);
        listsBundle.putString("method", method);

        startService(context, callback, listsBundle);
    }

    public void getCards(Context context, Callback callback, String listId) {
        Bundle cardsBundle = new Bundle();
        final String method = String.format(APIHelper.GET_CARDS_URL, listId);
        cardsBundle.putString("method", method);

        startService(context, callback, cardsBundle);
    }

    private void startService(Context context, Callback callback, Bundle bundle) {
        Intent intent = new Intent(context, MyService.class);
        intent.putExtras(bundle);
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
