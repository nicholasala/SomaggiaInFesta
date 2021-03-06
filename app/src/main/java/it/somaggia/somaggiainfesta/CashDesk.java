package it.somaggia.somaggiainfesta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import it.somaggia.somaggiainfesta.adapter.menu.SelectableElementsAdapter;
import it.somaggia.somaggiainfesta.adapter.command.AnimatedCompactCommandsAdapter;
import it.somaggia.somaggiainfesta.adapter.command.StaticCommandsAdapter;
import it.somaggia.somaggiainfesta.model.Command;
import it.somaggia.somaggiainfesta.model.Keys;
import it.somaggia.somaggiainfesta.model.Menu;
import it.somaggia.somaggiainfesta.fragment.CashDeskSettingsFrag;
import it.somaggia.somaggiainfesta.fragment.OrderFragment;
import it.somaggia.somaggiainfesta.fragment.StaticAnimatedCommandsFragment;
import it.somaggia.somaggiainfesta.fragment.StaticCommandsFragment;
import it.somaggia.somaggiainfesta.fragment.StaticFabCommandsFragment;
import it.somaggia.somaggiainfesta.network.CashDeskNetOrchestrator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class CashDesk extends RestaurantModule{
    private TextView infoText;
    private Button retryButton;
    private SelectableElementsAdapter additionsAdapter;
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
            netManager.closeConnection(Keys.MessageCode.correctEndOfServiceCashDesk, Keys.MessageText.endservice);
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
                    //setup network connection and connect with kitchen
                    try {
                        netManager = new CashDeskNetOrchestrator( new URI( Keys.ip.kitchen_url + ":" + Keys.ip.ws_port ), this);
                        netManager.connectBlocking();

                        //if connection is open, setup cashdesk ui
                        if(netManager.isOpen()){
                            //setup UI
                            setContentView(R.layout.activity_cashdesk);
                            inflateBottomBar();
                            setupBottomBar();

                            //setup adapters
                            buildAdapters();

                            //setup first fragment
                            setupActiveFragment();
                        }else{
                            infoText.setText(R.string.not_active_kitchen);
                            retryButton.setVisibility(View.VISIBLE);
                        }
                    } catch (URISyntaxException | InterruptedException e) {
                        e.printStackTrace();

                        infoText.setText(R.string.network_error);
                        retryButton.setVisibility(View.VISIBLE);
                    }

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

    public void buildAdapters(){
        activesAdapter = new StaticCommandsAdapter();
        servedAdapter = new AnimatedCompactCommandsAdapter();
    }

    protected void setupActiveFragment(){
        if(isActualFragmentServed()){
            clearServedUI();
        }

        if(!checkCongrats()){
            inflateFragment(StaticFabCommandsFragment.newInstance());
            activeRecycler = findViewById(R.id.static_recycler);
            activeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            activeRecycler.setItemAnimator(new DefaultItemAnimator());
            activeRecycler.setAdapter(activesAdapter);

            setupFAB(R.drawable.ic_action_plus);
        }else{
            setupCongratFragment();
        }

    }

    protected void setupServedFragment(){
        inflateFragment(StaticAnimatedCommandsFragment.newInstance());
        servedRecycler = findViewById(R.id.static_recycler);
        servedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        servedRecycler.setItemAnimator(new DefaultItemAnimator());
        servedRecycler.setAdapter(servedAdapter);

        if(((AnimatedCompactCommandsAdapter) servedAdapter).hasCommandToView())
            setupFAB(R.drawable.ic_action_done);
    }

    protected void setupSettingsFragment(){
        if(isActualFragmentServed()){
            clearServedUI();
        }

        inflateFragment(CashDeskSettingsFrag.newInstance());
        TextView ip = findViewById(R.id.settings_ip);
        TextView served = findViewById(R.id.settings_served);
        TextView actives = findViewById(R.id.settings_actives);

        String myIp = myIp();
        ip.setText("Identificativo cassa: "+myIp.substring(myIp.lastIndexOf('.') + 1));
        served.setText("Comande servite: "+servedAdapter.getItemCount());
        actives.setText("Comande attive: "+activesAdapter.getItemCount());
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
        additionsAdapter = new SelectableElementsAdapter();
        for(String a : menu.getAdds())
            additionsAdapter.putElement(a);

        RecyclerView orderAddsRV = findViewById(R.id.order_adds_recycler);
        orderAddsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderAddsRV.setAdapter(additionsAdapter);

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
                                Command c = new Command(commandId++, foods.getSelectedItem().toString());
                                List<String> added = additionsAdapter.getCheckedElementsName();
                                String[] sAdded = new String[added.size()];

                                for(int i=0; i<sAdded.length; i++){
                                    sAdded[i] = added.get(i);
                                }

                                c.setAdded(sAdded);
                                c.setNumber(Integer.valueOf(number.getText().toString()));
                                if(!netManager.sendCommand(c)){
                                    Toast.makeText(CashDesk.this, R.string.error_connecting_kitchen, Toast.LENGTH_LONG).show();
                                }else{
                                    putCommand(c);
                                    setupActiveFragment();
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

    @Override
    public void onEvent(Keys.Event event) {
        //TODO handle events

        switch (event){
            case CONNCLOSED:
//                new AlertDialog.Builder(CashDesk.this)
//                        .setTitle(R.string.on_try_reconnect)
//                        .setMessage(R.string.on_try_reconnect_message)
//                        .setCancelable(true)
//                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if(netManager.tryReconnect())
//                                    Toast.makeText(CashDesk.this, R.string.on_reconnect_succesful, Toast.LENGTH_LONG).show();
//                                else
//                                    CashDesk.this.onEvent(Keys.Event.CONNCLOSED);
//                            }
//                        })
//                        .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                CashDesk.this.onBackPressed();
//                            }
//                        })
//                        .show();

                break;
            default:

                break;
        }
    }

    private void clearServedUI(){
        servedRecycler.setAdapter(null);
        ((AnimatedCompactCommandsAdapter)servedAdapter).viewCommands();
        servedRecycler.setAdapter(servedAdapter);
        servedAdapter.notifyDataSetChanged();
        changeIcon(getBottomIndexOf("Serviti"), R.drawable.ic_action_served, R.color.colorPrimaryText);
    }

    @SuppressLint("RestrictedApi")
    private void setupFAB(int id){
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(getDrawable(id));
        fab.setVisibility(View.VISIBLE);

        if(isActualFragmentServed()){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearServedUI();
                    activateBottomIcon(getBottomIndexOf("Serviti"));
                    fab.setVisibility(View.GONE);
                }
            });
        }else{
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(menu != null && menu.isValid()){
                        inflateFragment(OrderFragment.newInstance());
                        setupOrderFragment();
                    }else{
                        Toast.makeText(CashDesk.this, R.string.menu_not_available, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void onCommandConfirm(int id){
        Command c = activesAdapter.removeCommandById(id);

        if(c != null){
            changeIcon(getBottomIndexOf("Serviti"), R.drawable.ic_action_notify, R.color.colorNotify);
            allarm();
            servedAdapter.putCommand(c);
            Toast.makeText(CashDesk.this, R.string.command_conf_ok, Toast.LENGTH_LONG).show();

            if(isActualFragmentServed())
                setupFAB(R.drawable.ic_action_done);
            else if(isActualFragmentActive() && checkCongrats())
                setupCongratFragment();
        }

        //TODO all'arrivo di una comanda fantasma, bisogna chiedere alla cucina di ritornarci la comanda completa invece che la sola conferma
        // così che anche ad un secondo avvio della cassa sia possibile ricevere le comande passate (sia servite che attive)
    }

    public void onMenu(Menu m){
        Toast.makeText(CashDesk.this, R.string.menu_updated, Toast.LENGTH_SHORT).show();
        this.menu = m;
    }

    public void putCommand(Command c){
        activesAdapter.putCommand(c);
    }

    public int activeCommands(){
        return activesAdapter.getItemCount();
    }

    public int servedCommands(){
        return servedAdapter.getItemCount();
    }

    public List<Command> getActiveCommands() { return activesAdapter.getCommands(); }

    public List<Command> getServedCommands() { return servedAdapter.getCommands(); }

    @Override
    public void onBackPressed() {
        if(actualFrag instanceof StaticCommandsFragment || actualFrag instanceof CashDeskSettingsFrag){
            new AlertDialog.Builder(CashDesk.this)
                    .setTitle(R.string.on_back_alert)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CashDesk.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }else{
            CashDesk.super.onBackPressed();
        }
    }

    private boolean isActualFragmentServed(){
        return actualFrag instanceof StaticAnimatedCommandsFragment;
    }

    private boolean isActualFragmentActive(){
        return actualFrag instanceof StaticFabCommandsFragment;
    }
}
