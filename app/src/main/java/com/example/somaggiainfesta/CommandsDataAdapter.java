package com.example.somaggiainfesta;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

//TODO https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28

public class CommandsDataAdapter extends RecyclerView.Adapter<CommandsDataAdapter.CommandViewHolder> {
    private List<Command> commands;

    @NonNull
    @Override
    public CommandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommandViewHolder commandViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //ViewHolder
    public class CommandViewHolder extends RecyclerView.ViewHolder{

        public CommandViewHolder(View view){
            super(view);

        }
    }
}
