package com.example.somaggiainfesta.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.somaggiainfesta.CashDesk;
import com.example.somaggiainfesta.data.Command;

public class ConfCmdDispatcher extends AsyncTask<Void, Void, Command> {
    private CashDesk context;
    private String message;
    private MessageConverter cv;

    public ConfCmdDispatcher(CashDesk context, String message)
    {
        this.context = context;
        this.message = message;
        this.cv = new MessageConverter();
    }

    @Override
    protected Command doInBackground(Void... voids) {
        return new Command(cv.getId(message), "");
    }

    @Override
    protected void onPostExecute(Command c) {
        if(c.getId() != -1){
            Log.v("CommandConf", String.valueOf(c.getId()));

            super.onPostExecute(c);
            context.onCommandConfirm(c.getId());
        }
    }
}
