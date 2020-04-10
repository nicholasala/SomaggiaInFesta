package it.somaggia.somaggiainfesta.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.somaggia.somaggiainfesta.R;;

public class OrderFragment extends Fragment {

    public static OrderFragment newInstance(){
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_order, container, false);
    }
}
