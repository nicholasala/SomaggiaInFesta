package com.example.somaggiainfesta.network;

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
        send(cv.getHandshakeText());
    }

    @Override
    public void onMessage(String message) {
        try {
            int code = new JSONObject(message).getInt("code");

            switch (code){
                case Keys.MessageCode.menu:
                    new MenuDispatcher(context, message).execute();
                    break;
                case Keys.MessageCode.confirmCommand:
                    new ConfCmdDispatcher(context, message).execute();
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

    public boolean sendCommand(Command c){
        if(isOpen()){
            send(cv.commandToString(c));
            return true;
        }

        return retrySendCommand(c, 4);
    }

    private boolean retrySendCommand(Command c, int times){
        reconnect();
        if(isOpen()){
            send(cv.commandToString(c));
            return true;
        }

        if(times > 0)
            return retrySendCommand(c, times-1);
        else
            return false;
    }
}
