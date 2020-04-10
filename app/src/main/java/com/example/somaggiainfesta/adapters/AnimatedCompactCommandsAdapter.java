package com.example.somaggiainfesta.adapters;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.example.somaggiainfesta.data.Command;

import java.util.ArrayList;

//Static commands with animation on the new arrived elements
public class AnimatedCompactCommandsAdapter extends CompactCommandsAdapter {
    private ArrayList<Integer> notViewed = new ArrayList<Integer>();

    @Override
    public void onBindViewHolder(@NonNull StaticCommandViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int itemColor = Color.WHITE;

        if(notViewed.contains(position))
            itemColor = Color.GREEN;

        holder.cont.setBackgroundColor(itemColor);
    }

    public void viewCommands(){
        notViewed.clear();
    }

    public boolean hasCommandToView(){
        return notViewed.size() > 0;
    }

    @Override
    public int putCommand(Command c) {
        notViewed.add(super.putCommand(c));
        notifyDataSetChanged();
        return notViewed.get(notViewed.size() - 1);
    }
}
