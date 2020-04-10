package it.somaggia.somaggiainfesta.network;

import android.os.AsyncTask;

import it.somaggia.somaggiainfesta.RestaurantModule;
import it.somaggia.somaggiainfesta.model.Keys;

public class EventsDispatcher extends AsyncTask<Void, Void, Keys.Event> {
    private RestaurantModule context;
    private Keys.Event event;

    public EventsDispatcher(RestaurantModule context, Keys.Event event)
    {
        this.context = context;
        this.event = event;
    }

    @Override
    protected Keys.Event doInBackground(Void... voids) {
        return this.event;
    }

    @Override
    protected void onPostExecute(Keys.Event e) {
        context.onEvent(e);
    }
}
