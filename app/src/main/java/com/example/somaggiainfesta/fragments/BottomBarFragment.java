package com.example.somaggiainfesta.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somaggiainfesta.R;

public class BottomBarFragment extends Fragment {

    public static BottomBarFragment newInstance(){
        BottomBarFragment fragment = new BottomBarFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_bottom_bar, container, false);
    }
}
