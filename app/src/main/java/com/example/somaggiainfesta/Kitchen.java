package com.example.somaggiainfesta;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Kitchen extends RestaurantModule{
    private TextView infoText;
    private BottomNavigationView bottomNavigationView;
    private CommandsDataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        infoText = findViewById(R.id.waitingText);

        findKitchen();
    }

    //TODO RICOMINCIARE UTILIZZANDO UNA BOTTOM BAR
    //TODO OGNI ELEMENTO NEGLI ATTIVI POTRÀ ESSERE TRASCINATO PER CONFERMARE CHE LA COMANDA È PRONTA
    //TODO CON UN'ANIMAZIONE VERDE AL TRASCINAMENTO
    //TODO https://www.truiton.com/2017/01/android-bottom-navigation-bar-example/
    //TODO https://www.androidhive.info/2017/09/android-recyclerview-swipe-delete-undo-using-itemtouchhelper/

    @Override
    public void onKitchenInfo(Keys.kitchenState state) {
        switch (state){
            case NETERR:
                infoText.setText(R.string.network_error);
                break;
            case FOUND:
                infoText.setText(R.string.founded_kitchen);
                break;
            case NOTFOUND:
                //network setting
                //android.provider.Settings.System.putInt(getContentResolver(), Settings.System.WIFI_USE_STATIC_IP, 1);
                //android.provider.Settings.System.putString(getContentResolver(), Settings.System.WIFI_STATIC_IP, Keys.ip.kitchen_string);
                //Toast.makeText(this, "Ip:"+android.provider.Settings.System.getString(getContentResolver(), Settings.System.WIFI_STATIC_IP), Toast.LENGTH_SHORT).show();

                //ui setting
                setContentView(R.layout.activity_kitchen);
                bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //handle action case
                        switch (item.getItemId()) {
                            case R.id.action_actives:

                                break;
                            case R.id.action_served:

                                break;
                            case R.id.action_settings:

                                break;
                        }
                        return true;
                    }
                });

                //setup recycler view
                //TODO da fare solo nel caso attivi ?
                setCommandsDataAdapter();
                setupRecyclerView();

                break;
        }
    }

    private void setCommandsDataAdapter(){
        List<Command> commands = new ArrayList<>();

        Command a = new Command(13, 12, "patatine", new String[]{}, 3);

        Command b = new Command(13, 12, "panino salsiccia", new String[]{"maionese", "ketchup"}, 2);

        Command c = new Command(13, 12, "panino salsiccia", new String[]{"cipolle", "pomodori"}, 1);

        commands.add(a);
        commands.add(b);
        commands.add(c);

        mAdapter = new CommandsDataAdapter(commands);
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }
}
