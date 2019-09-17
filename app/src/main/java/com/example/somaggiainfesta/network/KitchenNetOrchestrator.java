package com.example.somaggiainfesta.network;

import android.util.SparseArray;

import com.example.somaggiainfesta.Kitchen;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Menu;

import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


public class KitchenNetOrchestrator extends WebSocketServer {
    private int ids = 0;
    private Kitchen context;
    private MessageConverter cv;

    public KitchenNetOrchestrator(InetSocketAddress address, Kitchen context) {
        super(address);
        this.context = context;
        this.cv = new MessageConverter();
    }

    @Override
    public void onStart() { }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake handshake) {
        Menu m = Kitchen.menu;

        if(m!= null && m.isValid())
            webSocket.send(cv.menuToString(m));
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        for(int i=0; i<getConnections().size(); i++)
            if(webSocket.equals(getConnections().toArray()[i]))
                new KitchenDispatcher(context, text, i).execute();
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) { }

    @Override
    public void onError(WebSocket conn, Exception ex) { }

    public void confirmCommand(Command c){
        try {
            WebSocket client = (WebSocket) getConnections().toArray()[c.getId()];
            client.send(cv.commandToConfString(c));
        }catch (Exception e) {}
    }

    public void broadcastMenu(){
        broadcast(cv.menuToString(Kitchen.menu));
    }
}
