package com.example.somaggiainfesta.network;

import com.example.somaggiainfesta.Kitchen;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.data.Menu;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;


public class KitchenNetOrchestrator extends WebSocketServer {
    private Kitchen context;
    private MessageConverter cv;
    private ArrayList<String> binder;

    public KitchenNetOrchestrator(InetSocketAddress address, Kitchen context) {
        super(address);
        this.context = context;
        this.cv = new MessageConverter();
        this.binder = new ArrayList<>();
    }

    @Override
    public void onStart() {
        setConnectionLostTimeout(Keys.ConstValues.conLostTimeout);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake handshake) {
        webSocket.send(cv.getHandshakeText());

        //store connection, ip - position in list
        if(!binder.contains(webSocket.getRemoteSocketAddress().getAddress().getHostAddress()))
            binder.add(webSocket.getRemoteSocketAddress().getAddress().getHostAddress());

        //send menu
        Menu m = Kitchen.menu;
        if(m!= null && m.isValid())
            webSocket.send(cv.menuToString(m));
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            int code = new JSONObject(message).getInt("code");

            switch (code){
                case Keys.MessageCode.command:
                    String ip = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();

                    for(int i=0; i<binder.size(); i++)
                        if(binder.get(i).equals(ip))
                            new KitchenDispatcher(context, message, i).execute();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if(binder.contains(conn.getRemoteSocketAddress().getAddress().getHostAddress()))
            binder.remove(conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) { }

    public void confirmCommand(Command c){
        String ip = binder.get(c.getCashdesk());

        for(WebSocket ws : getConnections())
            if(ip.equals(ws.getRemoteSocketAddress().getAddress().getHostAddress()))
                ws.send(cv.commandToConfString(c));
    }

    public void broadcastMenu(){
        broadcast(cv.menuToString(Kitchen.menu));
    }
}
