package com.example.somaggiainfesta.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somaggiainfesta.R;

public class StaticCommandsFragment extends Fragment {
    public boolean fab;
    public boolean newItemAnimation;

    public static StaticCommandsFragment newInstance(boolean fab, boolean newItemAnimation){
        StaticCommandsFragment fragment = new StaticCommandsFragment();
        fragment.fab = fab;
        fragment.newItemAnimation = newItemAnimation;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        if(!this.fab)
            return inflater.inflate(R.layout.fragment_static_commands, container, false);
        else
            return inflater.inflate(R.layout.fragment_static_commands_fab, container, false);
    }
}
