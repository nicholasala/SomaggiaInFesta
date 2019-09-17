package com.example.somaggiainfesta.network;

import android.os.AsyncTask;

import com.example.somaggiainfesta.Kitchen;
import com.example.somaggiainfesta.data.Command;

public class KitchenDispatcher extends AsyncTask<Void, Void, Command> {

    private Kitchen context;
    private String message;

    public KitchenDispatcher(Kitchen context, String message)
    {
        this.context = context;
        this.message = message;
    }

    @Override
    protected Command doInBackground(Void... voids) {
        //        Command c = cv.stringToCommand(text);
//
//        //TODO verificare metodo migliore, se il seguente non funziona è possibile fare uno sparseArray con stringhe invece che con l'Oggetto ws
//        //TODO vanno probabilmente aggiunti dei controlli
//        c.setCashdesk(cashdesks.keyAt(cashdesks.indexOfValue(webSocket)));
//        context.addCommand(c);
//
////        for(int i=0; i< cashdesks.size(); i++){
////            if(webSocket.request().url().host().equals(cashdesks.get(cashdesks.keyAt(i)).request().url().host())){
////                c.setId(cashdesks.keyAt(i));
////                context.addCommand(c);
////            }
////        }


        return new Command(1, "Panino");
    }

    @Override
    protected void onPostExecute(Command c) {
        super.onPostExecute(c);
        context.onCommand(c);
    }
}
