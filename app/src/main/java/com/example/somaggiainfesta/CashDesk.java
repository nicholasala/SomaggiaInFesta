package com.example.somaggiainfesta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.somaggiainfesta.adapters.StaticComAdapter;
import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.fragments.StaticCommandsFragment;

import java.util.ArrayList;
import java.util.List;

public class CashDesk extends RestaurantModule{
    private TextView infoText;
    private Button retryButton;
    private BottomNavigationView bottomNavigationView;
    private StaticComAdapter activesAdapter;
    private StaticComAdapter servedAdapter;
    private RecyclerView activeRecycler;
    private RecyclerView servedRecycler;

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
    public void onKitchenInfo(Keys.kitchenState state) {
        switch (state){
            case NETERR:
                infoText.setText(R.string.network_error);
                retryButton.setVisibility(View.VISIBLE);
                break;
            case FOUND:
                setContentView(R.layout.activity_cashdesk);
                bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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
                        }
                        return true;
                    }
                });

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
                final EditText editText = new EditText(CashDesk.this);
                final Spinner s = new Spinner(CashDesk.this);
                List<String> varSpinnerData;
                List<String> myArraySpinner = new ArrayList<String>();

                myArraySpinner.add("red");
                myArraySpinner.add("green");
                myArraySpinner.add("blue");

                varSpinnerData = myArraySpinner;

                new AlertDialog.Builder(CashDesk.this)
                        .setTitle("Nome del piatto:")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = editText.getText().toString();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setView(editText)
                        .setView(s)
                        .show();
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
}
