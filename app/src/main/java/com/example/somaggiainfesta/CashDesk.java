package com.example.somaggiainfesta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somaggiainfesta.adapters.StaticComAdapter;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.data.Menu;
import com.example.somaggiainfesta.fragments.OrderFragment;
import com.example.somaggiainfesta.fragments.StaticCommandsFragment;
import com.example.somaggiainfesta.network.CashDeskNetOrchestrator;

import java.net.URI;
import java.net.URISyntaxException;

public class CashDesk extends RestaurantModule{
    private TextView infoText;
    private Button retryButton;
    private StaticComAdapter activesAdapter;
    private StaticComAdapter servedAdapter;
    private RecyclerView activeRecycler;
    private RecyclerView servedRecycler;
    private CashDeskNetOrchestrator netManager;
    private Menu menu;

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
                setContentView(R.layout.activity_cashdesk);
                BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_actives:
                                inflateFragment(StaticCommandsFragment.newInstance(true));
                                activeRecycler = (RecyclerView)findViewById(R.id.static_recycler);
                                setupActivesRecyclerView();
                                setupFAB();
                                break;
                            case R.id.action_served:
                                inflateFragment(StaticCommandsFragment.newInstance(false));
                                servedRecycler = (RecyclerView)findViewById(R.id.static_recycler);
                                setupServedRecyclerView();
                                break;
                            case R.id.action_settings:
                                Toast.makeText(CashDesk.this, "Da costruire", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                //setup network connection and connect with kitchen
                try {
                    netManager = new CashDeskNetOrchestrator( new URI( Keys.ip.kitchen_url + ":" + Keys.ip.ws_port ), this);
                    netManager.connect();
                } catch (URISyntaxException e) {
                    Toast.makeText(CashDesk.this, "Errore con la connessione alla cucina", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                //setup adapters
                activesAdapter = new StaticComAdapter();
                servedAdapter = new StaticComAdapter();

                //setup manually first fragment
                inflateFragment(StaticCommandsFragment.newInstance(true));
                activeRecycler = (RecyclerView)findViewById(R.id.static_recycler);
                setupActivesRecyclerView();
                setupFAB();
                break;
            case NOTFOUND:
                infoText.setText(R.string.not_founded_kitchen);
                retryButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupActivesRecyclerView(){
        activeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activeRecycler.setItemAnimator(new DefaultItemAnimator());
        activeRecycler.setAdapter(activesAdapter);
    }

    private void setupServedRecyclerView(){
        servedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        servedRecycler.setItemAnimator(new DefaultItemAnimator());
        servedRecycler.setAdapter(servedAdapter);
    }

    private void setupFAB(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ///////
                netManager.sendCommand(new Command(1, "Panino", new String[]{"maionese", "Ketchup"}, 2));

                if(menu != null && menu.isValid()){
                    inflateFragment(OrderFragment.newInstance());
                    setupOrderFragment();
                }else{
                    Toast.makeText(CashDesk.this, "Menu non disponibile", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupOrderFragment(){
        ImageView add = (ImageView)findViewById(R.id.increment_food_icon);
        ImageView remove = (ImageView)findViewById(R.id.decrement_food_icon);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView number = (TextView) findViewById(R.id.order_number);
                number.setText(String.valueOf(Integer.valueOf(number.getText().toString()) + 1));
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView number = (TextView) findViewById(R.id.order_number);
                int value = Integer.valueOf(number.getText().toString());
                if(value > 1)
                    number.setText(String.valueOf(value - 1));
            }
        });
    }

    private void inflateFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, frag);
        transaction.commit();
        //force transaction execution
        getSupportFragmentManager().executePendingTransactions();
    }

    public void onCommandConfirm(int id){
        Toast.makeText(CashDesk.this, "comanda confermata" + id, Toast.LENGTH_LONG).show();
    }

    public void onMenu(Menu m){
        Log.v("CashDesk", "New menu");
        this.menu = m;
    }
}
