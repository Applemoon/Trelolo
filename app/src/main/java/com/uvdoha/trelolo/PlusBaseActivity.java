package com.uvdoha.trelolo;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;


/**
 * Обертка над связью с Google Play Services PlusClient.
 */
public abstract class PlusBaseActivity extends Activity
        implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String TAG = PlusBaseActivity.class.getSimpleName();

    // A magic number we will use to know that our sign-in error resolution activity has completed
    private static final int OUR_REQUEST_CODE = 49404;

    // Флаг, предотвращяющий появление нескольких диалогов
    private boolean mAutoResolveOnFail;

    // Флаг, показывающий, что идет процесс соединения
    public boolean mPlusClientIsConnecting = false;

    // Вспомогательный объект, который соединяется с сервисами Google Play.
    private PlusClient mPlusClient;

    // Сохраненный результат от {@link #onConnectionFailed(ConnectionResult)}.
    // Не null, если была попытка соединения.
    // Null, если метод соединения еще запущен.
    private ConnectionResult mConnectionResult;


    /**
     * Called when the {@link PlusClient} revokes access to this app.
     */
    protected abstract void onPlusClientRevokeAccess();

    /**
     * Вызывается, когда PlusClient успешно соединен.
     */
    protected abstract void onPlusClientSignIn();

    /**
     * Вызывается, когда {@link PlusClient} отключен.
     */
    protected abstract void onPlusClientSignOut();

    /**
     * Вызывается, когда {@link PlusClient} блокирует UI. Если у вас есть виджет прогресса,
     * эта функция показывает, когда можно его показать или спрятать.
     */
    protected abstract void onPlusClientBlockingUI(boolean show);

    /**
     * Вызывается при изменении состояния соединения. Если у вас есть кнопки "Войти"/"Подключиться",
     * "Выйти"/"Отключиться" или "Отменить соединение", эта функция даст знать, когда необходимо
     * обновить их состояние.
     */
    protected abstract void updateConnectButtonState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализирует соединение с PlusClient.
        // Scopes indicate the information about the user your application will be able to access.
        mPlusClient = new PlusClient.Builder(this, this, this).setScopes(Scopes.PLUS_LOGIN,
                        Scopes.PLUS_ME).build();
    }

    /**
     * Попытка входа пользователем.
     */
    public void signIn() {
        if (!mPlusClient.isConnected()) {
            // Показывает диалог прогресса, пока мы не соединились.
            setProgressBarVisible(true);

            // Убеждаемся, что попробуем решить любые появившиеся ошибки
            // (e.g. fire the intent and pop up a dialog for the user)
            mAutoResolveOnFail = true;

            // We should always have a connection result ready to resolve,
            // so we can start that process.
            if (mConnectionResult != null) {
                startResolution();
            } else {
                // If we don't have one though, we can start connect in order to retrieve one.
                initiatePlusClientConnect();
            }
        }

        updateConnectButtonState();
    }

    /**
     * Соединяемся с {@link PlusClient} только если соединение не в процессе. This will
     * call back to {@link #onConnected(android.os.Bundle)} or
     * {@link #onConnectionFailed(com.google.android.gms.common.ConnectionResult)}.
     */
    private void initiatePlusClientConnect() {
        if (!mPlusClient.isConnected() && !mPlusClient.isConnecting()) {
            mPlusClient.connect();
        }
    }

    /**
     * Отключаемся от {@link PlusClient} только если соеденины (иначе может случиться ошибка)
     * This will call back to {@link #onDisconnected()}.
     */
    private void initiatePlusClientDisconnect() {
        if (mPlusClient.isConnected()) {
            mPlusClient.disconnect();
        }
    }

    /**
     * Выход из профиля (для того, чтобы пользователь мог войти в другой).
     */
    public void signOut() {

        // Выходим только в том случае, если мы подсоединены
        if (mPlusClient.isConnected()) {
            // Clear the default account in order to allow the user to potentially choose a
            // different account from the account chooser.
            mPlusClient.clearDefaultAccount();

            // Отключаемся от сервисов Google Play, затем подключаемся заново для того,
            // чтобы перезапустить процесс from scratch.
            initiatePlusClientDisconnect();

            Log.v(TAG, "Sign out successful!");
        }

        updateConnectButtonState();
    }

    /**
     * Полностью отменяем авторизацию в Google+.
     */
    public void revokeAccess() {

        if (mPlusClient.isConnected()) {
            // Clear the default account as in the Sign Out.
            mPlusClient.clearDefaultAccount();

            // Отменяем доступ ко всему приложению. This will call back to
            // onAccessRevoked when it is complete, as it needs to reach the Google
            // authentication servers to revoke all tokens.
            mPlusClient.revokeAccessAndDisconnect(new PlusClient.OnAccessRevokedListener() {
                public void onAccessRevoked(ConnectionResult result) {
                    updateConnectButtonState();
                    onPlusClientRevokeAccess();
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        initiatePlusClientConnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        initiatePlusClientDisconnect();
    }

    public boolean isPlusClientConnecting() {
        return mPlusClientIsConnecting;
    }

    private void setProgressBarVisible(boolean flag) {
        mPlusClientIsConnecting = flag;
        onPlusClientBlockingUI(flag);
    }

    /**
     * Вспомогательный метод to flip the mResolveOnFail flag and start the resolution
     * of the ConnectionResult from the failed connect() call.
     */
    private void startResolution() {
        try {
            // Don't start another resolution now until we have a result from the activity we're
            // about to start.
            mAutoResolveOnFail = false;

            // Если мы можем решить ошибку, тогда вызываем решение и передаем численное значение,
            // которую мы можем отслеживать.
            // Это значит, что когда мы получим onActivityResult коллбэк, мы будем знать,
            // что он был вызван здесь.
            mConnectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            // Если возникли проблемы, тогда просто пробуем connect() снова, чтобы получить
            // новый ConnectionResult.
            mConnectionResult = null;
            initiatePlusClientConnect();
        }
    }

    /**
     * Прошлое соединение закончилось неудачей, и теперь мы получаем результат попытки решения от PlusClient.
     *
     * @see #onConnectionFailed(ConnectionResult)
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        updateConnectButtonState();
        if (requestCode == OUR_REQUEST_CODE && responseCode == RESULT_OK) {
            // Если результат успешен, мы захотим уметь решать любые последующие ошибки,
            // поэтому включаем решение с помощью флага.
            mAutoResolveOnFail = true;
            // Если результат успешен, вызовем connect() снова. If there are any more
            // errors to resolve we'll get our onConnectionFailed, but if not,
            // we'll get onConnected.
            initiatePlusClientConnect();
        } else if (requestCode == OUR_REQUEST_CODE && responseCode != RESULT_OK) {
            // Если мы получили ошибку, которую не можем решить, значит мы больше не
            // соединеняемся и можем остановить диалог процесса.
            setProgressBarVisible(false);
        }
    }

    /**
     * Успешное соединение (вызывает PlusClient)
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        updateConnectButtonState();
        setProgressBarVisible(false);
        onPlusClientSignIn();
    }

    /**
     * Успешное отключение (вызывает PlusClient)
     */
    @Override
    public void onDisconnected() {
        updateConnectButtonState();
        onPlusClientSignOut();
    }

    /**
     * Соединение недачно по каким-то причинам (вызывает PlusClient)
     * Попытаемся решить проблему. Ошибка здесь обычно несерьезна, просто нужен пользовательский ввод.
     *
     * @see #onActivityResult(int, int, Intent)
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        updateConnectButtonState();

        // Чаще всего соединение потерпит неудачу по причине, которую может исправить пользователь.
        // Мы можем хранить это в свойстве mConnectionResult, готовое для использования, когда
        // пользователь нажмет кнопку авторизации
        if (result.hasResolution()) {
            mConnectionResult = result;
            if (mAutoResolveOnFail) {
                // Это локальная вспомогательная функция, которая начинает решение проблемы
                // и может показать пользователю выбор профиля или типа того.
                startResolution();
            }
        }
    }

    public PlusClient getPlusClient() {
        return mPlusClient;
    }

}
