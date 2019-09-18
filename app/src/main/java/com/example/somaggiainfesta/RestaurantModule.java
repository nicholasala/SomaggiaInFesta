package com.example.somaggiainfesta;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;

import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.network.KitchenFinder;

//abstract class that represent component of a restaurant (cashdesk, kitchen, ...)
public abstract class RestaurantModule extends AppCompatActivity {

    public abstract void onKitchenInfo(Keys.kitchenState state);

    protected void findKitchen(){ new KitchenFinder(this).execute(); }

    public String myIp(){
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ip = wm.getConnectionInfo().getIpAddress();
        String sIp = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

        return sIp;
    }

    public boolean isKitchen(){
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ip = wm.getConnectionInfo().getIpAddress();
        String sIp = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

        return sIp.equals(Keys.ip.kitchen_string);
    }
}
