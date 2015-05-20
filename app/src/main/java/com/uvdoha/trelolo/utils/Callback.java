package com.uvdoha.trelolo.utils;

import android.os.Bundle;

/**
 * Created by dmitry on 14.05.15.
 */
public abstract class Callback {

    public Callback() {}
    public abstract void onSuccess(Bundle data);
    public abstract void onFail(Bundle data);
    public void onDone(Bundle data) {}

    public void success(Bundle data) {
        this.onDone(data);
        this.onSuccess(data);
    }

    public void fail(Bundle data) {
        this.onDone(data);
        this.onFail(data);
    }

}
