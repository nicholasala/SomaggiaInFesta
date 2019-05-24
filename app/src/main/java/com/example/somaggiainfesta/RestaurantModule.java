package com.example.somaggiainfesta;

import android.support.v7.app.AppCompatActivity;

abstract class RestaurantModule extends AppCompatActivity {
    abstract void onKitchenInfo(Keys.kitchenState state);
    protected void findKitchen(){ new KitchenFinder(this).execute(); }
}
