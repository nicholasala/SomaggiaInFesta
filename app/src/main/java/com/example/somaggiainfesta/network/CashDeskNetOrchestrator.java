package com.example.somaggiainfesta.network;

import android.widget.Toast;

import com.example.somaggiainfesta.CashDesk;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


public class CashDeskNetOrchestrator extends WebSocketClient {
    private CashDesk context;
    private MessageConverter cv;

    public CashDeskNetOrchestrator(URI serverUri, CashDesk context) {
        super(serverUri);
        this.context = context;
        cv = new MessageConverter();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        int a = 0;

    }

    @Override
    public void onMessage(String message) {
        try {
            int code = new JSONObject(message).getInt("code");

            switch (code){
                case Keys.MessageCode.menu:
                    new MenuDispatcher(context, message);
                    break;
                case Keys.MessageCode.confirmCommand:
                    new ConfCmdDispatcher(context, message);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) { }

    @Override
    public void onError(Exception ex) { }

    public void sendCommand(Command c){
        send(cv.commandToString(c));
    }
}
