package com.example.somaggiainfesta;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Kitchen extends RestaurantModule implements SwipeController.RecyclerItemTouchHelperListener{
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
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //attach swipe helper to recyclerview
        SwipeController sc = new SwipeController(this);
        ItemTouchHelper ith = new ItemTouchHelper(sc);
        ith.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        mAdapter.removeCommand(position);
        Toast.makeText(this, "Comanda confermata", Toast.LENGTH_SHORT).show();
    }
}
