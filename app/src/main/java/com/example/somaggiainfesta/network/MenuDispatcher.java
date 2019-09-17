package com.example.somaggiainfesta.network;

import android.os.AsyncTask;

import com.example.somaggiainfesta.CashDesk;
import com.example.somaggiainfesta.data.Menu;

public class MenuDispatcher extends AsyncTask<Void, Void, Menu> {
    private CashDesk context;
    private String message;
    private MessageConverter cv;

    public MenuDispatcher(CashDesk context, String message)
    {
        this.context = context;
        this.message = message;
        this.cv = new MessageConverter();
    }

    @Override
    protected Menu doInBackground(Void... voids) {
        return cv.stringToMenu(message);
    }

    @Override
    protected void onPostExecute(Menu m) {
        if(m != null){
            super.onPostExecute(m);
            context.onMenu(m);
        }
    }
}
