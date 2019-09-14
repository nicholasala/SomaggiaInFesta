package com.example.somaggiainfesta.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.somaggiainfesta.R;

import java.util.ArrayList;
import java.util.List;

public class MenuElAdapter extends RecyclerView.Adapter<MenuElAdapter.MenuElViewHolder> {
    private List<String> elements;

    public MenuElAdapter(){
        this.elements = new ArrayList<>();
    }

    @NonNull
    @Override
    public MenuElViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_element_row, viewGroup, false);
        return new MenuElViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuElViewHolder commandViewHolder, int i) {
        commandViewHolder.name.setText(elements.get(i));
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public void removeElement(int position){
        elements.remove(position);
        notifyItemRemoved(position);
    }

    public int indexOf(String s){
        for(int i=0; i<elements.size(); i++)
            if(s.equals(elements.get(i)))
                return i;

        return -1;
    }

    public boolean putElement(String el){
        for(String s : elements)
            if(s.equals(el))
                return false;

        elements.add(el);
        notifyItemInserted(elements.size() - 1);
        return true;
    }

    public List<String> getElements(){
        return elements;
    }

    //ViewHolder
    public class MenuElViewHolder extends RecyclerView.ViewHolder{
        private TextView name;

        public MenuElViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.menu_element);
        }
    }
}
