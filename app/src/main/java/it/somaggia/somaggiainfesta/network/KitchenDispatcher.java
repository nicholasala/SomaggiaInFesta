package it.somaggia.somaggiainfesta.network;

import android.os.AsyncTask;

import it.somaggia.somaggiainfesta.Kitchen;
import it.somaggia.somaggiainfesta.model.Command;

// TODO AsyncTask sono deprecati, cambiare tutti gli AsyncTasks in network
public class KitchenDispatcher extends AsyncTask<Void, Void, Command> {
    private final Kitchen kitchenContext;
    private final String message;
    private final String cashDeskId;
    private final MessageConverter cv;

    KitchenDispatcher(Kitchen kitchenContext, String message, String cashDeskId)
    {
        this.kitchenContext = kitchenContext;
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
        kitchenContext.onCommand(c);
    }
}
