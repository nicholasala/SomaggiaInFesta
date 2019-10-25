package com.example.somaggiainfesta;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

//abstract class that represent component of a restaurant (cashdesk, kitchen, ...)
public abstract class RestaurantModule extends AppCompatActivity {
    //TODO activesAdapter e servedAdapter è corretto che stiano qui, visto che sono presenti sia in cucina che in cassa.
    // Probabilmente anche i metodi relativi all'aggiunta delle comande attive possono essere generalizzati
    // anche i test presenti per le casse probabilmente possono essere generalizzati


    protected Fragment actualFrag;
    protected TextView[] bottomItems;

    public abstract void onKitchenInfo(Keys.kitchenState state);

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

    protected void findKitchen(){ new KitchenFinder(this).execute(); }

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
                    bottomItems[i].setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_actives, 0, 0);

                    bottomItems[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setupActiveFragment();
                            activateBottomIcon(index);
                        }
                    });

                    break;
                case "Serviti":
                    bottomItems[i].setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_served, 0, 0);

                    bottomItems[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setupServedFragment();
                            activateBottomIcon(index);
                        }
                    });

                    break;
                case "Impostazioni":
                    bottomItems[i].setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_settings, 0, 0);

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

        //iniztially activate the first icon
        activateBottomIcon(0);
    }

    protected void activateBottomIcon(int index){
        for(TextView t : bottomItems){
            t.setTextColor(getColor(R.color.colorPrimaryText));
            t.setTextSize(12);
            t.getCompoundDrawables()[1].setTint(getColor(R.color.colorPrimaryText));
        }

        bottomItems[index].setTextColor(Color.WHITE);
        bottomItems[index].setTextSize(13);
        bottomItems[index].getCompoundDrawables()[1].setTint(Color.WHITE);
    }

    protected int getBottomIndexOf(String title){
        if(bottomItems != null)
            for(int i=0; i<bottomItems.length; i++)
                if(bottomItems[i].getText().toString().equals(title))
                    return i;

        return -1;
    }

    protected void changeIcon(int index, int iconId, int colorId){
        if(index != -1 && index < bottomItems.length){
            bottomItems[index].setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
            bottomItems[index].getCompoundDrawables()[1].setTint(getColor(colorId));
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(RestaurantModule.this)
                .setTitle(R.string.on_back_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RestaurantModule.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
