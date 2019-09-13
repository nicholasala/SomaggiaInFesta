package com.example.somaggiainfesta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somaggiainfesta.R;

public class StaticCommandsFragment extends Fragment {

    public static StaticCommandsFragment newInstance(){
        StaticCommandsFragment fragment = new StaticCommandsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_static_commands, container, false);
    }
}
