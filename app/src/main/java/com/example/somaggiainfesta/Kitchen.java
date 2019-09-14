package com.example.somaggiainfesta;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somaggiainfesta.adapters.ActiveComAdapter;
import com.example.somaggiainfesta.adapters.MenuElAdapter;
import com.example.somaggiainfesta.adapters.StaticComAdapter;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.data.Menu;
import com.example.somaggiainfesta.fragments.ActiveCommandsFragment;
import com.example.somaggiainfesta.fragments.SettingsFragment;
import com.example.somaggiainfesta.fragments.StaticCommandsFragment;
import com.example.somaggiainfesta.network.KitchenNetOrchestrator;

public class Kitchen extends RestaurantModule implements SwipeController.RecyclerItemTouchHelperListener{
    private TextView infoText;
    private BottomNavigationView bottomNavigationView;
    private ActiveComAdapter activesAdapter;
    private StaticComAdapter servedAdapter;
    private MenuElAdapter namesAdapter;
    private MenuElAdapter addsAdapter;
    private RecyclerView activeRecycler;
    private RecyclerView staticRecycler;
    private RecyclerView namesRecycler;
    private RecyclerView addsRecycler;
    private KitchenNetOrchestrator netManager;

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
                //Toast.makeText(this, "Ip:"+android.provider.Settings.System.commandToString(getContentResolver(), Settings.System.WIFI_STATIC_IP), Toast.LENGTH_SHORT).show();

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
                                namesRecycler = (RecyclerView)findViewById(R.id.names_recycler);
                                addsRecycler = (RecyclerView)findViewById(R.id.adds_recycler);
                                setupNamesRecyclerView();
                                setupAddsRecyclerView();

                                //setup buttons
                                ImageView addFood = (ImageView)findViewById(R.id.add_food_icon);
                                ImageView addAdd = (ImageView)findViewById(R.id.add_add_icon);

                                addFood.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final EditText editText = new EditText(Kitchen.this);

                                        new AlertDialog.Builder(Kitchen.this)
                                                .setTitle("Nome del piatto:")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String name = editText.getText().toString();

                                                        if(!name.equals(""))
                                                            namesAdapter.putElement(name);
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.cancel, null)
                                                .setView(editText)
                                                .show();

                                        //update menu over the network
                                        netManager.broadcastMenu();
                                        Toast.makeText(Kitchen.this, "Menu inoltrato", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                addAdd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final EditText editText = new EditText(Kitchen.this);

                                        new AlertDialog.Builder(Kitchen.this)
                                                .setTitle("Nome del'aggiunta:")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String name = editText.getText().toString();

                                                        if(!name.equals(""))
                                                            addsAdapter.putElement(name);
                                                    }
                                                })
                                                .setNegativeButton("Annulla", null)
                                                .setView(editText)
                                                .show();

                                        //update menu over the network
                                        netManager.broadcastMenu();
                                        Toast.makeText(Kitchen.this, "Menu inoltrato", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });

                //setup network connection
                netManager = new KitchenNetOrchestrator(this);
                //setup adapters
                activesAdapter = new ActiveComAdapter();
                servedAdapter = new StaticComAdapter();
                namesAdapter = new MenuElAdapter();
                addsAdapter = new MenuElAdapter();

                //random data
                Command a = new Command(13, "patatine", 12, new String[]{}, 3);
                Command b = new Command(13, "panino salsiccia", 12, new String[]{"maionese", "ketchup"}, 2);
                Command c = new Command(13, "arrosticini", 12, new String[]{"cipolle", "pomodori"}, 1);
                activesAdapter.putCommand(a);
                activesAdapter.putCommand(b);
                activesAdapter.putCommand(c);

                //setup manually first fragment
                inflateFragment(ActiveCommandsFragment.newInstance());
                activeRecycler = (RecyclerView)findViewById(R.id.active_recycler);
                setupActivesRecyclerView();
                break;
        }
    }

    private void setupActivesRecyclerView(){
        activeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activeRecycler.setItemAnimator(new DefaultItemAnimator());
        activeRecycler.setAdapter(activesAdapter);

        //attach swipe helper to recyclerview
        SwipeController sc = new SwipeController(this);
        ItemTouchHelper ith = new ItemTouchHelper(sc);
        ith.attachToRecyclerView(activeRecycler);
    }

    private void setupServedRecyclerView(){
        staticRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        staticRecycler.setItemAnimator(new DefaultItemAnimator());
        staticRecycler.setAdapter(servedAdapter);
    }

    private void setupNamesRecyclerView(){
        namesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        namesRecycler.setItemAnimator(new DefaultItemAnimator());
        namesRecycler.setAdapter(namesAdapter);
    }

    private void setupAddsRecyclerView(){
        addsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addsRecycler.setItemAnimator(new DefaultItemAnimator());
        addsRecycler.setAdapter(addsAdapter);
    }

    private void inflateFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, frag);
        transaction.commit();
        //force transaction execution
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        confirmCommand(activesAdapter.getCommand(position));
        activesAdapter.removeCommand(position);
    }

    public void confirmCommand(Command c){
        servedAdapter.putCommand(c);
        netManager.confirmCommand(c);
        Toast.makeText(this, "Comanda confermata", Toast.LENGTH_SHORT).show();
    }

    public void addCommand(Command c){
        activesAdapter.putCommand(c);
    }

    public Menu getMenu(){
        Menu m = new Menu();

        for(String s : namesAdapter.getElements())
            m.addFood(s);

        for(String s : addsAdapter.getElements())
            m.addAdd(s);

        return m;
    }

    public void onMenuElRemoved(View v){
        ViewGroup vp = (ViewGroup) v.getParent();
        final int recyclerId = ((View) v.getParent().getParent().getParent()).getId();
        final String toRemove = ((TextView) vp.getChildAt(0)).getText().toString();

        new AlertDialog.Builder(Kitchen.this)
                .setTitle("Rimovere "+toRemove+" dal menu ?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(recyclerId == R.id.names_recycler){
                            int index = namesAdapter.indexOf(toRemove);
                            if(index != -1)
                                namesAdapter.removeElement(index);
                        }else if(recyclerId == R.id.adds_recycler){
                            int index = addsAdapter.indexOf(toRemove);
                            if(index != -1)
                                addsAdapter.removeElement(index);
                        }
                    }
                })
                .setNegativeButton("Annulla", null)
                .show();

        //update menu over the network
        netManager.broadcastMenu();
        Toast.makeText(this, "Menu inoltrato", Toast.LENGTH_SHORT).show();
    }
}