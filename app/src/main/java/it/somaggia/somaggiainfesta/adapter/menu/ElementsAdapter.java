package it.somaggia.somaggiainfesta.adapter.menu;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementsAdapter<A extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<A>{
    protected List<String> elements;

    public ElementsAdapter(){
        elements = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return elements.size();
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

    public void removeElement(int position){
        elements.remove(position);
        notifyItemRemoved(position);
    }

    public void clear(){
        elements.clear();
        notifyDataSetChanged();
    }

    static abstract class ElementViewholder extends RecyclerView.ViewHolder{
        protected TextView name;

        protected ElementViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
