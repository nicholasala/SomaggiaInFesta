package com.example.somaggiainfesta.network;

import com.example.somaggiainfesta.Kitchen;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.data.Menu;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;


public class KitchenNetOrchestrator extends WebSocketServer {
    private Kitchen context;
    private MessageConverter cv;

    public KitchenNetOrchestrator(InetSocketAddress address, Kitchen context) {
        super(address);
        this.context = context;
        this.cv = new MessageConverter();
    }

    @Override
    public void onStart() {
        setConnectionLostTimeout(Keys.ConstValues.conLostTimeout);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake handshake) {
        webSocket.send(cv.getHandshakeText());

        //send menu
        Menu m = Kitchen.menu;
        if(m!= null && m.isValid())
            webSocket.send(cv.menuToString(m));

        //TODO bisognerebbe nel caso di menu non validi, inviare comunque il menu, così che se la cucina esce e poi rientra e non ha menu valido le casse non possano
        // ordinare con menu fantasma

        //TODO bisognerebbe all'apertura di una nuova connessione, chiedere alla cassa se ha comande attive, e se si farsele inviare
        // così da mantenere uno stato precedente
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            int code = new JSONObject(message).getInt("code");

            switch (code){
                case Keys.MessageCode.command:
                    String ip = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
                    new KitchenDispatcher(context, message, ip).execute();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) { }

    @Override
    public void onError(WebSocket conn, Exception ex) { }

    public boolean confirmCommand(Command c){
        for(WebSocket ws : getConnections()) {
            if (c.getCashdesk().equals(ws.getRemoteSocketAddress().getAddress().getHostAddress())) {
                ws.send(cv.commandToConfString(c));
                return true;
            }
        }

        return false;
    }

    public void broadcastMenu(){
        broadcast(cv.menuToString(Kitchen.menu));
    }

    public void closeConnections(){
        for(WebSocket ws : getConnections())
            ws.closeConnection(Keys.MessageCode.correctEndOfServiceKitchen, Keys.MessageText.endservice);
    }
}
