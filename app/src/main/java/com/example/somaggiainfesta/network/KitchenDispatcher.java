package com.example.somaggiainfesta.network;

import android.os.AsyncTask;

import com.example.somaggiainfesta.Kitchen;
import com.example.somaggiainfesta.data.Command;

public class KitchenDispatcher extends AsyncTask<Void, Void, Command> {
    private Kitchen context;
    private String message;
    private int cashDeskId;
    private MessageConverter cv;

    public KitchenDispatcher(Kitchen context, String message, int cashDeskId)
    {
        this.context = context;
        this.message = message;
        this.cashDeskId = cashDeskId;
        this.cv = new MessageConverter();
    }

    @Override
    protected Command doInBackground(Void... voids) {
        Command c = cv.stringToCommand(message);
        c.setCashdesk(cashDeskId);
        return c;
    }

    @Override
    protected void onPostExecute(Command c) {
        super.onPostExecute(c);
        context.onCommand(c);
    }
}
