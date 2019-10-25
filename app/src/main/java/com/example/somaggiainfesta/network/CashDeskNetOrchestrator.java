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

        //TODO all'apertura della connessione con la cucina bisogna chiedere alla cucina se la cassa con il seguente ip ha delle comande attive in cucina
        // se si bisogna farsele inviare
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
    public void onClose(int code, String reason, boolean remote) {
        //TODO verificare perchè il metodo KitchenNetOrchestrator.closeConnections non riesce a mandare in uscita della cucina la chiusura corretta
        // la chiusura arriva con un codice probabilmente interno della libreria, è come se ci fosse qualcosa che chiamasse la close sovrastando la
        // nostra chiamata che dovrebbe tornare come codice Keys.MessageCode.correctEndOfServiceKitchen

        switch(code){
            case -1:
                //connection refused
                break;
            case Keys.MessageCode.correctEndOfServiceKitchen:
                //new EventsDispatcher(this.context, Keys.Event.CORRECTCONNCLOSEDFROMKITCHEN).execute();
                break;
            case Keys.MessageCode.correctEndOfServiceCashDesk:
                break;
            default:
                new EventsDispatcher(this.context, Keys.Event.CONNCLOSED).execute();
        }
    }

    @Override
    public void onError(Exception ex) { }

    public boolean sendCommand(Command c){
        if(isOpen()){
            send(cv.commandToString(c));
            return true;
        }

        try {
            reconnectBlocking();

            if(isOpen()){
                send(cv.commandToString(c));
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean tryReconnect(){
        try {
            reconnectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return isOpen();
    }
}

//TODO comande multiple
//TODO miglior gestione del menu quando ci si riconnette per qualche motivo alla cucina (rimane il vecchio ma in realtà dovrebbe aggiornarsi)
//TODO alla riconnessione con la cucina bisogna gestire le comande fantasma: le comande attive vanno reinviate ;)
