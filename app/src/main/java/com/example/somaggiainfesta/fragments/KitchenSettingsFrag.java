package com.example.somaggiainfesta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somaggiainfesta.R;

public class KitchenSettingsFrag extends Fragment {

    public static KitchenSettingsFrag newInstance(){
        KitchenSettingsFrag fragment = new KitchenSettingsFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_kitchen_settings, container, false);
    }
}
