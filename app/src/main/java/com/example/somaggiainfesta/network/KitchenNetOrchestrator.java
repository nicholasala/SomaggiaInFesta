package com.example.somaggiainfesta.network;

import android.util.SparseArray;
import android.widget.Toast;

import com.example.somaggiainfesta.Kitchen;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Menu;

import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


public class KitchenNetOrchestrator extends WebSocketServer {
    private SparseArray<WebSocket> cashdesks;
    private int ids = 0;
    private MessageConverter cv;
    private Kitchen context;

    public KitchenNetOrchestrator(InetSocketAddress address, Kitchen context) {
        super(address);
        this.context = context;
        this.cashdesks = new SparseArray<>();
        this.cv = new MessageConverter();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake handshake) {
        cashdesks.append(ids++, webSocket);
        Menu m = context.getMenu();

        if(m.isValid())
            webSocket.send(cv.menuToString(m));
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        new KitchenDispatcher(context, text).execute();
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    public void confirmCommand(Command c){
        WebSocket ws = cashdesks.get(c.getCashdesk());
        if(ws != null && ws.isOpen())
            ws.send(cv.commandToConfString(c));
    }

    public void broadcastMenu(){
        String menu = cv.menuToString(context.getMenu());

        for(int i=0; i<cashdesks.size(); i++)
            cashdesks.get(cashdesks.keyAt(i)).send(menu);
    }
}
