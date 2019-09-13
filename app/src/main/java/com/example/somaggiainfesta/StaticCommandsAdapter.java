package com.example.somaggiainfesta;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StaticCommandsAdapter extends RecyclerView.Adapter<StaticCommandsAdapter.StaticCommandViewHolder> {
    private List<Command> commands;

    StaticCommandsAdapter(){
        this.commands = new ArrayList<>();
    }

    @NonNull
    @Override
    public StaticCommandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.static_command_row, viewGroup, false);
        return new StaticCommandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaticCommandViewHolder commandViewHolder, int i) {
        Command command = commands.get(i);
        commandViewHolder.name.setText(command.getName());

        StringBuilder sb = new StringBuilder();
        String[] added = command.getAdded();

        for(int a=0; a<added.length - 1; a++){
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

    public void removeCommand(int position){
        commands.remove(position);
        notifyItemRemoved(position);
    }

    public Command getCommand(int position){
        return commands.get(position);
    }

    public void putCommand(Command c){
        commands.add(c);
        notifyItemInserted(commands.size() - 1);
    }

    //ViewHolder
    public class StaticCommandViewHolder extends RecyclerView.ViewHolder{
        private TextView name, added, number;

        public StaticCommandViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.static_name);
            added = (TextView) view.findViewById(R.id.static_added);
            number = (TextView) view.findViewById(R.id.static_number);
        }
    }
}
