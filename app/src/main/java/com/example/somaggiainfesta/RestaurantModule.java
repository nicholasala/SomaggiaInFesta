package com.example.somaggiainfesta;

import android.support.v7.app.AppCompatActivity;

import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.network.KitchenFinder;

//abstract class that represent component of a restaurant (cashdesk, kitchen, ...)
public abstract class RestaurantModule extends AppCompatActivity {
    public abstract void onKitchenInfo(Keys.kitchenState state);
    protected void findKitchen(){ new KitchenFinder(this).execute(); }
}
