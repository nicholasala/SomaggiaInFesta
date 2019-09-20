package com.example.somaggiainfesta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somaggiainfesta.adapters.AddsOrderAdapter;
import com.example.somaggiainfesta.adapters.StaticComAdapter;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.data.Menu;
import com.example.somaggiainfesta.fragments.ActiveCommandsFragment;
import com.example.somaggiainfesta.fragments.CashDeskSettingsFrag;
import com.example.somaggiainfesta.fragments.OrderFragment;
import com.example.somaggiainfesta.fragments.StaticCommandsFragment;
import com.example.somaggiainfesta.network.CashDeskNetOrchestrator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class CashDesk extends RestaurantModule{
    private TextView infoText;
    private Button retryButton;
    private StaticComAdapter activesAdapter;
    private StaticComAdapter servedAdapter;
    private AddsOrderAdapter addsAdapter;
    private RecyclerView activeRecycler;
    private RecyclerView servedRecycler;
    private CashDeskNetOrchestrator netManager;
    private Menu menu;
    private Integer commandId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        infoText = findViewById(R.id.waitingText);
        retryButton = findViewById(R.id.retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoText.setText(R.string.waiting_network);
                findKitchen();
                retryButton.setVisibility(View.INVISIBLE);
            }
        });

        findKitchen();
    }

    @Override
    protected void onDestroy() {
        if(netManager != null){
            netManager.closeConnection(Keys.MessageCode.close, Keys.MessageText.close);
            netManager.close();
        }

        super.onDestroy();
    }


    @Override
    public void onKitchenInfo(Keys.kitchenState state) {
        switch (state){
            case NETERR:
                infoText.setText(R.string.network_error);
                retryButton.setVisibility(View.VISIBLE);
                break;
            case FOUND:
                if(!isKitchen()){
                    setContentView(R.layout.activity_cashdesk);
                    inflateBottomBar();
                    setupBottomBar();

                    //setup network connection and connect with kitchen
                    try {
                        netManager = new CashDeskNetOrchestrator( new URI( Keys.ip.kitchen_url + ":" + Keys.ip.ws_port ), this);
                        netManager.connect();
                    } catch (URISyntaxException e) {
                        Toast.makeText(CashDesk.this, "Errore con la connessione alla cucina", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    //setup adapters
                    activesAdapter = new StaticComAdapter(false);
                    servedAdapter = new StaticComAdapter(true);

                    //setup manually first fragment
                    setupActiveFragment();
                }else{
                    infoText.setText(R.string.cucina);
                    retryButton.setVisibility(View.VISIBLE);
                }

                break;
            case NOTFOUND:
                infoText.setText(R.string.not_founded_kitchen);
                retryButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    protected void setupActiveFragment(){
        clearServedUI();

        inflateFragment(StaticCommandsFragment.newInstance(true, false));
        activeRecycler = findViewById(R.id.static_recycler);
        activeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activeRecycler.setItemAnimator(new DefaultItemAnimator());
        activeRecycler.setAdapter(activesAdapter);

        setupFAB(R.drawable.ic_action_plus);
    }

    protected void setupServedFragment(){
        inflateFragment(StaticCommandsFragment.newInstance(true, true));
        servedRecycler = findViewById(R.id.static_recycler);
        servedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        servedRecycler.setItemAnimator(new DefaultItemAnimator());
        servedRecycler.setAdapter(servedAdapter);

        if(servedAdapter.hasCommandToView())
            setupFAB(R.drawable.ic_action_done);
    }

    protected void setupSettingsFragment(){
        clearServedUI();

        inflateFragment(CashDeskSettingsFrag.newInstance());
        TextView ip = findViewById(R.id.settings_ip);
        TextView served = findViewById(R.id.settings_served);
        TextView actives = findViewById(R.id.settings_actives);

        String myIp = myIp();
        ip.setText("Identificativo cassa: "+myIp.substring(myIp.lastIndexOf('.') + 1));
        served.setText("Comande servite: "+servedAdapter.getItemCount());
        actives.setText("Comande attive: "+activesAdapter.getItemCount());
    }

    private void clearServedUI(){
        //if the previusly loaded fragment was the served fragment, change the state of the not viewed confirmed commands
        if(actualFrag instanceof StaticCommandsFragment && ((StaticCommandsFragment)actualFrag).newItemAnimation){
            servedAdapter.viewCommands();
        }
    }

    private void allarmServedIcon(){
        int servedIndex = getBottomIndexOf("Serviti");

        if(servedIndex != -1){
            bottomItems[servedIndex].setTextColor(getColor(R.color.colorNotify));
            //bottomItems[servedIndex].compound
        }

        //TODO
        //BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //bottomNavigationView.getMenu().findItem(R.id.action_served).setIcon(R.drawable.ic_action_notify);
    }

    @SuppressLint("RestrictedApi")
    private void setupFAB(int id){
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(getDrawable(id));
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFABClick(fab);
            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void onFABClick(FloatingActionButton fab){
        if(((StaticCommandsFragment)actualFrag).newItemAnimation){
            servedAdapter.viewCommands();
            setupServedFragment();
            //TODO far scorrere il servedRecycler alla cima
            fab.setVisibility(View.GONE);
        }else{
            if(menu != null && menu.isValid()){
                inflateFragment(OrderFragment.newInstance());
                setupOrderFragment();
            }else{
                Toast.makeText(CashDesk.this, "Menu non disponibile", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setupOrderFragment(){
        ImageView add = findViewById(R.id.increment_food_icon);
        ImageView remove = findViewById(R.id.decrement_food_icon);
        final TextView number = findViewById(R.id.order_number);
        final Spinner foods = findViewById(R.id.order_foods);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number.setText(String.valueOf(Integer.valueOf(number.getText().toString()) + 1));
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.valueOf(number.getText().toString());
                if(value > 1)
                    number.setText(String.valueOf(value - 1));
            }
        });

        //setup foods spinner
        ArrayAdapter<String> foodsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menu.getNames());
        foodsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foods.setAdapter(foodsAdapter);

        //setup adds recyclerview
        addsAdapter = new AddsOrderAdapter();
        for(String a : menu.getAdds())
            addsAdapter.putElement(a);

        RecyclerView orderAddsRV = findViewById(R.id.order_adds_recycler);
        orderAddsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderAddsRV.setAdapter(addsAdapter);

        //setup cancel and send command action
        ImageView cancel = findViewById(R.id.order_cancel);
        ImageView send = findViewById(R.id.order_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CashDesk.this)
                        .setTitle("Inviare ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String foodName = foods.getSelectedItem().toString();

                                if(!foodName.equals("")){
                                    Command c = new Command(commandId++, foodName);
                                    List<String> added = addsAdapter.getCheckedAdds();
                                    String[] sAdded = new String[added.size()];

                                    for(int i=0; i<sAdded.length; i++){
                                        sAdded[i] = added.get(i);
                                    }

                                    c.setAdded(sAdded);
                                    c.setNumber(Integer.valueOf(number.getText().toString()));
                                    netManager.sendCommand(c);

                                    activesAdapter.putCommand(c);
                                    setupActiveFragment();
                                }else{
                                    Toast.makeText(CashDesk.this, "Comanda non valida", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupActiveFragment();
            }
        });
    }

    public void onCommandConfirm(int id){
        Command c = activesAdapter.removeCommandById(id);

        if(c != null){
            allarmServedIcon();
            allarm();
            servedAdapter.putCommand(c);
            Toast.makeText(CashDesk.this, "Comanda "+id+" confermata", Toast.LENGTH_LONG).show();

            if(((StaticCommandsFragment)actualFrag).newItemAnimation)
                setupFAB(R.drawable.ic_action_done);
        }

    }

    public void onMenu(Menu m){
        Toast.makeText(CashDesk.this, "Menu aggiornato", Toast.LENGTH_SHORT).show();
        this.menu = m;
    }
}
