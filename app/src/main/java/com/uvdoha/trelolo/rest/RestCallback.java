package com.uvdoha.trelolo.rest;

/**
 * Created by dmitry on 14.05.15.
 */
abstract class RestCallback {

    public RestCallback() {}

    public abstract void onSuccess();
    public abstract void onFail();

}
