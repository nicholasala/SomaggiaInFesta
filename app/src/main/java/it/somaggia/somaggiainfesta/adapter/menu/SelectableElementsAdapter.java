package it.somaggia.somaggiainfesta.adapter.menu;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import it.somaggia.somaggiainfesta.R;;

import java.util.ArrayList;
import java.util.List;

public class SelectableElementsAdapter extends ElementsAdapter<SelectableElementsAdapter.SelectableElementViewHolder> {
    private List<Boolean> checked;

    public SelectableElementsAdapter(){
        super();
        this.checked = new ArrayList<>();
    }

    @NonNull
    @Override
    public SelectableElementViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_add_row, viewGroup, false);
        return new SelectableElementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectableElementViewHolder selectableElementViewHolder, final int i) {
        selectableElementViewHolder.name.setText(elements.get(i));
        selectableElementViewHolder.checkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked.set(i, !checked.get(i));
            }
        });
    }

    public boolean putElement(String el){
        if(super.putElement(el)){
            checked.add(false);
            notifyItemInserted(elements.size() - 1);
            return true;
        }

        return false;
    }

    public List<String> getCheckedElementsName(){
        ArrayList<String> ret = new ArrayList<>();

        for(int i = 0; i< elements.size(); i++)
            if(checked.get(i))
                ret.add(elements.get(i));

        return ret;
    }

    //ViewHolder
    static class SelectableElementViewHolder extends ElementsAdapter.ElementViewholder{
        private CheckBox checkB;

        SelectableElementViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.order_add);
            checkB = view.findViewById(R.id.order_checkbox);
        }
    }
}
