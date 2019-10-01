package com.example.somaggiainfesta.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.somaggiainfesta.R;

import java.util.ArrayList;
import java.util.List;

public class AddsOrderAdapter extends RecyclerView.Adapter<AddsOrderAdapter.AddViewHolder> {
    private List<String> adds;
    private List<Boolean> checked;

    public AddsOrderAdapter(){
        this.adds = new ArrayList<>();
        this.checked = new ArrayList<>();
    }

    @NonNull
    @Override
    public AddViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_add_row, viewGroup, false);
        return new AddViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddViewHolder addViewHolder, final int i) {
        addViewHolder.name.setText(adds.get(i));
        addViewHolder.checkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked.set(i, !checked.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return adds.size();
    }

    public void removeElement(int position){
        adds.remove(position);
        checked.remove(position);

        try {
            notifyItemRemoved(position);
        }catch (NullPointerException ignored){}
    }

    public boolean putElement(String el){
        for(String s : adds)
            if(s.equals(el))
                return false;

        adds.add(el);
        checked.add(false);

        try {
            notifyItemInserted(adds.size() - 1);
        }catch (NullPointerException ignored){}

        return true;
    }

    public List<String> getCheckedAdds(){
        ArrayList<String> ret = new ArrayList<>();

        for(int i=0; i<adds.size(); i++)
            if(checked.get(i))
                ret.add(adds.get(i));

        return ret;
    }

    //ViewHolder
    public class AddViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private CheckBox checkB;

        public AddViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.order_add);
            checkB = (CheckBox) view.findViewById(R.id.order_checkbox);
        }
    }
}
