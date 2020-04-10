package it.somaggia.somaggiainfesta.network;

import android.os.AsyncTask;

import it.somaggia.somaggiainfesta.Kitchen;
import it.somaggia.somaggiainfesta.model.Command;

public class KitchenDispatcher extends AsyncTask<Void, Void, Command> {
    private Kitchen context;
    private String message;
    private String cashDeskId;
    private MessageConverter cv;

    public KitchenDispatcher(Kitchen context, String message, String cashDeskId)
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
