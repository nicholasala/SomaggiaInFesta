package com.example.somaggiainfesta.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

        try {
            notifyItemRemoved(position);
        }catch (NullPointerException ignored){}
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

        try {
            notifyItemInserted(elements.size() - 1);
        }catch (NullPointerException ignored){}

        return true;
    }

    public List<String> getElements(){
        return elements;
    }

    public void clear(){
        elements.clear();
        notifyDataSetChanged();
    }

    //ViewHolder
    static class MenuElViewHolder extends RecyclerView.ViewHolder{
        private TextView name;

        MenuElViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.menu_element);
        }
    }
}
