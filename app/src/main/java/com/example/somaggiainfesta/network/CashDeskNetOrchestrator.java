package com.example.somaggiainfesta.network;

import android.widget.Toast;

import com.example.somaggiainfesta.CashDesk;
import com.example.somaggiainfesta.data.Keys;

import org.java_websocket.client.WebSocketClient;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


public class CashDeskNetOrchestrator extends WebSocketClient {
    private MessageConverter cv;
    private CashDesk context;

    public CashDeskNetOrchestrator(URI serverUri, CashDesk context) {
        super(serverUri);
        cv = new MessageConverter();
        this.context = context;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Buongiorno");
    }

    @Override
    public void onMessage(String message) {
        try {
            int code = new JSONObject(message).getInt("code");

            switch (code){
                case Keys.MessageCode.menu:
                    context.updateMenu(cv.stringToMenu(message));
                    break;
                case Keys.MessageCode.confirmCommand:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {
        Toast.makeText(context, "Error "+ex.toString(), Toast.LENGTH_LONG).show();
    }
}
