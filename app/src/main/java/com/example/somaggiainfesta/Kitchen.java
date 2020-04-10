package com.example.somaggiainfesta;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somaggiainfesta.adapters.ActiveCommandsAdapter;
import com.example.somaggiainfesta.adapters.CompactCommandsAdapter;
import com.example.somaggiainfesta.adapters.MenuElAdapter;
import com.example.somaggiainfesta.adapters.StaticCommandsAdapter;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.data.Menu;
import com.example.somaggiainfesta.fragments.ActiveCommandsFragment;
import com.example.somaggiainfesta.fragments.CongratulationsFragment;
import com.example.somaggiainfesta.fragments.KitchenSettingsFrag;
import com.example.somaggiainfesta.fragments.StaticCommandsFragment;
import com.example.somaggiainfesta.network.KitchenNetOrchestrator;
import com.example.somaggiainfesta.network.MessageConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.util.Date;

public class Kitchen extends RestaurantModule implements SwipeController.RecyclerItemTouchHelperListener{
    private TextView infoText;
    private MenuElAdapter namesAdapter;
    private MenuElAdapter addsAdapter;
    private RecyclerView namesRecycler;
    private RecyclerView addsRecycler;
    private KitchenNetOrchestrator netManager;
    public static volatile Menu menu;
    private final String menuFile = "menu.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        infoText = findViewById(R.id.waitingText);

        findKitchen();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //TODO a volte, rimuovendo dal task manager l'app senza tornare indietro di un activity, il server non viene rimosso completamente e questo comporta difficoltà future di connessione
        // da parte di casse, e molto più probabilmente impossibilità di avviare il server cucina
        if(netManager != null){
            try {
                netManager.closeConnections();
                netManager.stop();
            } catch (IOException | InterruptedException  e) {
                e.printStackTrace();
            }

            //save report in a txt file of the previus service
            try {
                String reportFile = "report.txt";
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), reportFile);
                FileOutputStream fos = new FileOutputStream(f, true);
                fos.write(getServiceDetails().getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onDestroy();
    }

    @Override
    public void onKitchenInfo(Keys.kitchenState state) {
        switch (state){
            case NETERR:
                infoText.setText(R.string.network_error);
                break;
            case FOUND:
                if(isKitchen())
                    setupKitchen();
                else
                    infoText.setText(R.string.founded_kitchen);

                break;
            case NOTFOUND:
                infoText.setText(R.string.waiting_ip_change);
                final Button goButton = findViewById(R.id.retry);
                goButton.setVisibility(View.VISIBLE);

                goButton.setText(R.string.modify);
                goButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(Settings.ACTION_WIFI_IP_SETTINGS), 0);
                        goButton.setText(R.string.go_on);
                        goButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setupKitchen();
                            }
                        });
                    }
                });

                break;
        }
    }

    public void setupKitchen(){
        startService();
        setupUi();
    }

    public void startService(){
        //start kitchen server
        netManager = new KitchenNetOrchestrator(new InetSocketAddress(Keys.ip.kitchen_string, Keys.ip.ws_port), this);
        netManager.start();
    }

    public void setupUi(){
        //setup ui
        setContentView(R.layout.activity_kitchen);
        inflateBottomBar();
        setupBottomBar();

        //setup adapters
        buildAdapters();

        //setup first fragment
        setupActiveFragment();

        //load menu if present
        try {
            loadMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildAdapters(){
        activesAdapter = new ActiveCommandsAdapter();
        servedAdapter = new CompactCommandsAdapter();
        namesAdapter = new MenuElAdapter();
        addsAdapter = new MenuElAdapter();
    }

    protected void setupActiveFragment(){
        if(!checkCongrats()){
            inflateFragment(ActiveCommandsFragment.newInstance());
            activeRecycler = findViewById(R.id.active_recycler);

            activeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            activeRecycler.setItemAnimator(new DefaultItemAnimator());
            activeRecycler.setAdapter(activesAdapter);

            //attach swipe helper to recyclerview
            SwipeController sc = new SwipeController(this);
            ItemTouchHelper ith = new ItemTouchHelper(sc);
            ith.attachToRecyclerView(activeRecycler);
        }else{
            setupCongratFragment();
        }
    }

    protected void setupServedFragment(){
        inflateFragment(StaticCommandsFragment.newInstance());
        servedRecycler = findViewById(R.id.static_recycler);

        servedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        servedRecycler.setItemAnimator(new DefaultItemAnimator());
        servedRecycler.setAdapter(servedAdapter);
    }

    protected void setupSettingsFragment(){
        inflateFragment(KitchenSettingsFrag.newInstance());
        namesRecycler = findViewById(R.id.names_recycler);
        addsRecycler = findViewById(R.id.adds_recycler);
        namesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        namesRecycler.setItemAnimator(new DefaultItemAnimator());
        namesRecycler.setAdapter(namesAdapter);

        namesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        namesRecycler.setItemAnimator(new DefaultItemAnimator());
        namesRecycler.setAdapter(namesAdapter);
        addsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addsRecycler.setItemAnimator(new DefaultItemAnimator());
        addsRecycler.setAdapter(addsAdapter);

        //setup buttons
        ImageView addFood = findViewById(R.id.add_food_icon);
        ImageView addAdd = findViewById(R.id.add_add_icon);
        ImageView save = findViewById(R.id.save_icon);
        ImageView load = findViewById(R.id.load_icon);

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

                                if(!name.equals("") && namesAdapter.putElement(name))
                                    updateMenu();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setView(editText)
                        .show();
            }
        });

        addAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(Kitchen.this);

                new AlertDialog.Builder(Kitchen.this)
                        .setTitle("Nome dell'aggiunta:")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = editText.getText().toString();

                                if(!name.equals("") && addsAdapter.putElement(name))
                                    updateMenu();
                            }
                        })
                        .setNegativeButton("Annulla", null)
                        .setView(editText)
                        .show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Menu m = Kitchen.this.getMenu();
                if(m.isValid()){
                    new AlertDialog.Builder(Kitchen.this)
                            .setTitle("Salvare il menu realizzato ?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MessageConverter cv = new MessageConverter();

                                    try {
                                        FileOutputStream fos = Kitchen.this.openFileOutput(menuFile, MODE_PRIVATE);
                                        fos.write(cv.menuToString(m).getBytes());
                                        fos.close();
                                        Toast.makeText(Kitchen.this, "Menu salvato", Toast.LENGTH_SHORT).show();
                                    } catch (IOException ioException) {
                                        Toast.makeText(Kitchen.this, "Errore nel salvataggio del menu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Annulla", null)
                            .show();
                }else{
                    Toast.makeText(Kitchen.this, "Menu non valido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadMenu();
                } catch (FileNotFoundException fileNotFound) {
                    Toast.makeText(Kitchen.this, R.string.no_menu_saved, Toast.LENGTH_SHORT).show();
                } catch (IOException ioException) {
                    Toast.makeText(Kitchen.this, R.string.menu_loading_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onEvent(Keys.Event e) {
        //TODO handle events
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        Command c = activesAdapter.getCommand(position);

        if(netManager.confirmCommand(c)){
            Toast.makeText(this, R.string.command_conf_ok, Toast.LENGTH_SHORT).show();
            servedAdapter.putCommand(c);
            activesAdapter.removeCommand(position);

            if(checkCongrats())
                setupCongratFragment();
        }else{
            Toast.makeText(this, R.string.command_conf_error, Toast.LENGTH_SHORT).show();
            activesAdapter.notifyDataSetChanged();
        }
    }

    public Menu getMenu(){
        Menu m = new Menu();

        for(String s : namesAdapter.getElements())
            m.addFood(s);

        for(String s : addsAdapter.getElements())
            m.addAdd(s);

        return m;
    }

    public void loadMenu() throws IOException {
        FileInputStream fis = Kitchen.this.openFileInput(menuFile);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        MessageConverter cv = new MessageConverter();
        Menu m = cv.stringToMenu(sb.toString());
        namesAdapter.clear();
        addsAdapter.clear();

        for(String s : m.getNames())
            namesAdapter.putElement(s);

        for(String s : m.getAdds())
            addsAdapter.putElement(s);

        updateMenu();
    }

    public void onMenuElRemoved(View v){
        ViewGroup vp = (ViewGroup) v.getParent();
        final int recyclerId = ((View) v.getParent().getParent().getParent()).getId();
        final String toRemove = ((TextView) vp.getChildAt(0)).getText().toString();

        new AlertDialog.Builder(Kitchen.this)
                .setTitle("Rimovere "+toRemove+" dal menu ?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(recyclerId == R.id.names_recycler){
                            int index = namesAdapter.indexOf(toRemove);
                            if(index != -1){
                                namesAdapter.removeElement(index);
                                updateMenu();
                            }
                        }else if(recyclerId == R.id.adds_recycler){
                            int index = addsAdapter.indexOf(toRemove);
                            if(index != -1){
                                addsAdapter.removeElement(index);
                                updateMenu();
                            }
                        }
                    }
                })
                .setNegativeButton("Annulla", null)
                .show();
    }

    public void updateMenu(){
        //update menu over the network
        menu = getMenu();
        netManager.broadcastMenu();
    }

    public void onCommand(Command c){
        allarm();
        activesAdapter.putCommand(c);

        if(actualFrag instanceof CongratulationsFragment)
            setupActiveFragment();
    }

    public String getServiceDetails(){
        StringBuilder sb = new StringBuilder();
        sb.append("SomaggiaInFesta ");
        sb.append(DateFormat.getDateTimeInstance().format(new Date()));
        sb.append("\n");
        sb.append("Serviti: \n");

        //TODO
//        List<String> distinctElements = servedAdapter.getCommands().stream()
//                .forEach(Command::getName)
//                .distinct()
//
//        for(String s : distinctElements){
//            int number = 0;
//            for(int i=0; i<served.size(); i++){
//                if(served.get(i).getName().equals(s))
//                    number = number + served.get(i).getNumber();
//                if(i == served.size() - 1)
//                    sb.append(s).append(": ").append(number).append("\n");
//            }
//        }

        sb.append("\n\n\n");
        return sb.toString();
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(Kitchen.this)
                .setTitle(R.string.on_back_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        netManager.closeConnections();
                        Kitchen.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}