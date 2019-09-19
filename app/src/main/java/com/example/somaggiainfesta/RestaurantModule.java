package com.example.somaggiainfesta;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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

    public boolean isKitchen(){ return myIp().equals(Keys.ip.kitchen_string); }

    public void allarm(){
        //code from https://stackoverflow.com/questions/4441334/how-to-play-an-android-notification-sound

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
