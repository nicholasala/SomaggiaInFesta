package com.example.somaggiainfesta.network;

import android.util.SparseArray;

import com.example.somaggiainfesta.Kitchen;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Menu;

import org.jetbrains.annotations.NotNull;

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
        Menu m = context.getMenu();

        if(m.isValid())
            webSocket.send(cv.menuToString(m));
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        Command c = cv.stringToCommand(text);

        //TODO verificare metodo migliore, se il seguente non funziona è possibile fare uno sparseArray con stringhe invece che con l'Oggetto ws
        //TODO vanno probabilmente aggiunti dei controlli
        c.setCashdesk(cashdesks.keyAt(cashdesks.indexOfValue(webSocket)));
        context.addCommand(c);

//        for(int i=0; i< cashdesks.size(); i++){
//            if(webSocket.request().url().host().equals(cashdesks.get(cashdesks.keyAt(i)).request().url().host())){
//                c.setId(cashdesks.keyAt(i));
//                context.addCommand(c);
//            }
//        }
    }

    public void confirmCommand(Command c){
        WebSocket ws = cashdesks.get(c.getCashdesk());
        if(ws != null)
            ws.send(cv.commandToConfString(c));
    }

    public void broadcastMenu(){
        String menu = cv.menuToString(context.getMenu());

        for(int i=0; i<cashdesks.size(); i++)
            cashdesks.get(cashdesks.keyAt(i)).send(menu);
    }
}
