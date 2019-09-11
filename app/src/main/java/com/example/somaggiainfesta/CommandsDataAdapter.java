package com.example.somaggiainfesta;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//TODO https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28

public class CommandsDataAdapter extends RecyclerView.Adapter<CommandsDataAdapter.CommandViewHolder> {
    private List<Command> commands;

    CommandsDataAdapter(List<Command> commands){
        this.commands = commands;
    }

    @NonNull
    @Override
    public CommandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.command_row, viewGroup, false);

        return new CommandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandViewHolder commandViewHolder, int i) {
        Command command = commands.get(i);
        commandViewHolder.name.setText(command.getName());

        StringBuilder sb = new StringBuilder();
        String[] added = command.getAdded();

        for(int a=0; a<added.length; a++){
            sb.append(added[a]);
            sb.append(" - ");
        }

        if(added.length > 0)
            sb.append(added[added.length-1]);

        commandViewHolder.added.setText(sb.toString());
        commandViewHolder.number.setText(String.valueOf(command.getNumber()));
    }

    @Override
    public int getItemCount() {
        return commands.size();
    }

    //ViewHolder
    public class CommandViewHolder extends RecyclerView.ViewHolder{
        private TextView name, added, number;
        public LinearLayout viewForeground;
        public RelativeLayout viewBackground;

        public CommandViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            added = (TextView) view.findViewById(R.id.added);
            number = (TextView) view.findViewById(R.id.number);
            viewForeground = view.findViewById(R.id.view_foreground);
            viewBackground = view.findViewById(R.id.view_background);
        }
    }
}
