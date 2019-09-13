package com.example.somaggiainfesta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Kitchen extends RestaurantModule implements SwipeController.RecyclerItemTouchHelperListener{
    private TextView infoText;
    private BottomNavigationView bottomNavigationView;
    private ActiveCommandsAdapter activesAdapter;
    private StaticCommandsAdapter servedAdapter;
    RecyclerView activeRecycler;
    RecyclerView staticRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        infoText = findViewById(R.id.waitingText);

        findKitchen();
    }

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
                        switch (item.getItemId()) {
                            case R.id.action_actives:
                                inflateFragment(ActiveCommandsFragment.newInstance());
                                activeRecycler = (RecyclerView)findViewById(R.id.active_recycler);
                                setupActivesRecyclerView();
                                break;
                            case R.id.action_served:
                                inflateFragment(StaticCommandsFragment.newInstance());
                                staticRecycler = (RecyclerView)findViewById(R.id.static_recycler);
                                setupServedRecyclerView();
                                break;
                            case R.id.action_settings:
                                inflateFragment(SettingsFragment.newInstance());
                                break;
                        }
                        return true;
                    }
                });

                //setup adapters
                setActivesAdapter();
                setServedAdapter();
                //setup manually first fragment

                Command a = new Command(13, 12, "patatine", new String[]{}, 3);
                Command b = new Command(13, 12, "panino salsiccia", new String[]{"maionese", "ketchup"}, 2);
                Command c = new Command(13, 12, "panino salsiccia", new String[]{"cipolle", "pomodori"}, 1);
                activesAdapter.putCommand(a);
                activesAdapter.putCommand(b);
                activesAdapter.putCommand(c);

                inflateFragment(ActiveCommandsFragment.newInstance());
                activeRecycler = (RecyclerView)findViewById(R.id.active_recycler);
                setupActivesRecyclerView();
                break;
        }
    }

    private void setActivesAdapter(){
        activesAdapter = new ActiveCommandsAdapter();
    }

    private void setServedAdapter(){
        servedAdapter = new StaticCommandsAdapter();
    }

    private void setupActivesRecyclerView(){
        activeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activeRecycler.setItemAnimator(new DefaultItemAnimator());
        activeRecycler.setAdapter(activesAdapter);

        //attach swipe helper to recyclerview
        SwipeController sc = new SwipeController(this);
        ItemTouchHelper ith = new ItemTouchHelper(sc);
        ith.attachToRecyclerView(activeRecycler); //TODO CAMBIARE RECYCLER VIEW
    }

    private void setupServedRecyclerView(){
        staticRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        staticRecycler.setItemAnimator(new DefaultItemAnimator());
        staticRecycler.setAdapter(servedAdapter);
    }

    private void inflateFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, frag);
        transaction.commit();
        //force transaction execution to avoid null pointer exception
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        confirmCommand(activesAdapter.getCommand(position));
        activesAdapter.removeCommand(position);
    }

    public void confirmCommand(Command c){
        //TODO Inviare alla cassa identificata dal suo id la conferma della comanda
        servedAdapter.putCommand(c);
        Toast.makeText(this, "Comanda confermata", Toast.LENGTH_SHORT).show();
    }
}
