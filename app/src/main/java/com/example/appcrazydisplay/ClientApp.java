package com.example.appcrazydisplay;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class ClientApp extends WebSocketClient{
    private LoginCallback loginCallback;
    public ClientApp(URI serverUri, LoginCallback callback) {
        super(serverUri);
        this.loginCallback = callback;
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
            if ("login".equals(jsonObject.getString("type"))) {
                boolean isValid = jsonObject.getBoolean("valid");
                if (loginCallback != null) {
                    loginCallback.onLoginResult(isValid);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }

}
