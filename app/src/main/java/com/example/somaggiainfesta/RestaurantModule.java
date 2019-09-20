package com.example.somaggiainfesta;

import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.fragments.BottomBarFragment;
import com.example.somaggiainfesta.network.KitchenFinder;

import java.util.ArrayList;

//abstract class that represent component of a restaurant (cashdesk, kitchen, ...)
public abstract class RestaurantModule extends AppCompatActivity {
    protected Fragment actualFrag;
    protected TextView[] bottomItems;

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

    protected void inflateFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, frag);
        transaction.commit();
        actualFrag = frag;

        //force transaction execution
        getSupportFragmentManager().executePendingTransactions();
    }

    protected void inflateBottomBar(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_bottom, BottomBarFragment.newInstance());
        transaction.commit();

        //force transaction execution
        getSupportFragmentManager().executePendingTransactions();
    }

    protected abstract void setupActiveFragment();

    protected abstract void setupServedFragment();

    protected abstract void setupSettingsFragment();

    protected void setupBottomBar(){
        //bottom bar setting
        LinearLayout bottomBar = findViewById(R.id.bottom_bar);
        bottomItems = new TextView[bottomBar.getChildCount()];

        for(int i=0; i<bottomBar.getChildCount(); i++){
            bottomItems[i] = (TextView) bottomBar.getChildAt(i);
            final int index = i;

            switch(bottomItems[i].getText().toString()){
                case "Attivi":
                    bottomItems[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setupActiveFragment();
                            activateBottomIcon(index);
                        }
                    });

                    break;
                case "Serviti":
                    bottomItems[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setupServedFragment();
                            activateBottomIcon(index);
                        }
                    });

                    break;
                case "Impostazioni":
                    bottomItems[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setupSettingsFragment();
                            activateBottomIcon(index);
                        }
                    });

                    break;
            }
        }
    }

    protected void activateBottomIcon(int index){
        for(TextView t : bottomItems)
            t.setTextColor(getColor(R.color.colorPrimaryText));

        bottomItems[index].setTextColor(Color.WHITE);
    }

    protected int getBottomIndexOf(String title){
        for(int i=0; i<bottomItems.length; i++)
            if(bottomItems[i].getText().toString().equals(title))
                return i;

        return -1;
    }


}
