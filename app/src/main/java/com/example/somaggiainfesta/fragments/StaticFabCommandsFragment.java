package com.example.somaggiainfesta.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somaggiainfesta.R;

public class StaticFabCommandsFragment extends StaticCommandsFragment {
    public static StaticFabCommandsFragment newInstance(){
        return new StaticFabCommandsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_static_commands_fab, container, false);
    }
}
