package com.example.somaggiainfesta.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somaggiainfesta.R;

public class ActiveCommandsFragment extends Fragment {

    public static ActiveCommandsFragment newInstance(){
        ActiveCommandsFragment fragment = new ActiveCommandsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_active_commands, container, false);
    }
}
