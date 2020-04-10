package com.example.somaggiainfesta.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.somaggiainfesta.R;

//Static commands with animation on the new arrived elements
public class StaticAnimatedCommandsAdapter extends StaticCommandsAdapter {
    private int viewed;

    public StaticAnimatedCommandsAdapter(){
        super();
        this.viewed = 0;
    }

    @NonNull
    @Override
    public StaticCommandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.static_command_row, viewGroup, false);
        return new StaticCommandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaticCommandViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int itemColor = Color.WHITE;

        if(position > viewed - 1)
            itemColor = Color.GREEN;

        holder.cont.setBackgroundColor(itemColor);
    }

    public void viewCommands(){
        viewed = commands.size();
    }

    public boolean hasCommandToView(){
        return viewed < commands.size();
    }

    public int commandsToViewNumber(){
        return commands.size() - viewed;
    }
}
