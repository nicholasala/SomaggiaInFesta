package com.example.somaggiainfesta.network;

import android.util.SparseArray;

import com.example.somaggiainfesta.Kitchen;
import com.example.somaggiainfesta.data.Command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class KitchenNetOrchestrator extends WebSocketListener {
    private SparseArray<WebSocket> cashdesks;
    private int ids = 0;
    private MessageConverter cv;
    private Kitchen context;

    public KitchenNetOrchestrator(Kitchen context) {
        this.context = context;
        this.cashdesks = new SparseArray<>();
        this.cv = new MessageConverter();
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        cashdesks.append(ids++, webSocket);
        //TODO INVIARE IL MENU
        //webSocket.send()
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        //TODO AGGIUNGERE l'ID DELLA CASSA CORRETTO
        Command c = cv.stringToCommand(text);

        for(int i=0; i< cashdesks.size(); i++){
            if(webSocket.request().url().host().equals(cashdesks.get(cashdesks.keyAt(i)).request().url().host())){
                c.setId(cashdesks.keyAt(i));
                context.addCommand(c);
            }
        }
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }

    public void confirmCommand(Command c){
        //.request().url().host().toString()
        WebSocket ws = cashdesks.get(c.getCashdesk());
        if(ws != null)
            ws.send(cv.commandToConfString(c));
    }
}
