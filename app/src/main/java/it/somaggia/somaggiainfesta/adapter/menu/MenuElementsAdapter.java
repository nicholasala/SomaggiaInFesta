package it.somaggia.somaggiainfesta.adapter.menu;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.somaggia.somaggiainfesta.R;

public class MenuElementsAdapter extends ElementsAdapter<MenuElementsAdapter.MenuElementViewHolder> {

    @NonNull
    @Override
    public MenuElementViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_element_row, viewGroup, false);
        return new MenuElementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuElementViewHolder commandViewHolder, int i) {
        commandViewHolder.name.setText(elements.get(i));
    }

    //ViewHolder
    static class MenuElementViewHolder extends ElementsAdapter.ElementViewholder{
        MenuElementViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.menu_element);
        }
    }
}
