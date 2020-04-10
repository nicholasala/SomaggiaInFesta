package it.somaggia.somaggiainfesta.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import it.somaggia.somaggiainfesta.RestaurantModule;
import it.somaggia.somaggiainfesta.model.Keys;

import java.io.IOException;
import java.net.InetAddress;

public class KitchenFinder extends AsyncTask<Void, Void, Keys.kitchenState> {

    private RestaurantModule context;

    public KitchenFinder(RestaurantModule context){
        this.context = context;
    }

    //TODO risolvere deprecated oppure gestire la cosa in modo diverso
    // (Se la cassa prova a connettersi con la libreria websocket e ottiene errore significa che la cucina non esiste)
    @Override
    protected Keys.kitchenState doInBackground(Void... voids) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting())
                return Keys.kitchenState.NETERR;

            if(InetAddress.getByAddress(Keys.ip.kitchen).isReachable(3000))
                return Keys.kitchenState.FOUND;
            else
                return Keys.kitchenState.NOTFOUND;

        } catch (IOException e) {
            return Keys.kitchenState.NETERR;
        }
    }

    @Override
    protected void onPostExecute(Keys.kitchenState state) {
        super.onPostExecute(state);
        context.onKitchenInfo(state);
    }
}
