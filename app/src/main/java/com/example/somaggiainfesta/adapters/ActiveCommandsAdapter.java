package com.example.somaggiainfesta.adapters;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.somaggiainfesta.R;

public class ActiveCommandsAdapter extends CommandsAdapter<ActiveCommandsAdapter.ActiveCommandViewHolder> {

    @NonNull
    @Override
    public ActiveCommandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.active_command_row, viewGroup, false);
        return new ActiveCommandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveCommandViewHolder holder, int position) {
        defaultOnBindViewHolder(holder, position);
    }

    //ViewHolder
    public static class ActiveCommandViewHolder extends CommandsAdapter.CommandViewHolder{
        protected TextView name, added, number;
        public LinearLayout viewForeground;
        RelativeLayout viewBackground;

        ActiveCommandViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.active_name);
            added = view.findViewById(R.id.active_added);
            number = view.findViewById(R.id.active_number);
            viewForeground = view.findViewById(R.id.active_view_foreground);
            viewBackground = view.findViewById(R.id.active_view_background);
        }
    }
}
