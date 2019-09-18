package com.example.somaggiainfesta.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.R;

import java.util.ArrayList;
import java.util.List;

public class StaticComAdapter extends RecyclerView.Adapter<StaticComAdapter.StaticCommandViewHolder> {
    private List<Command> commands;

    public StaticComAdapter(){
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
        commandViewHolder.id.setText(String.valueOf(command.getId()));
    }

    @Override
    public int getItemCount() {
        return commands.size();
    }

    public Command removeCommandById(int id){
        for(int i=0; i<commands.size(); i++){
            Command c = commands.get(i);
            if(c.getId() == id){
                commands.remove(i);
                notifyItemRemoved(i);
                return c;
            }
        }

        return null;
    }

    public void putCommand(Command c){
        commands.add(c);
        notifyItemInserted(commands.size() - 1);
    }

    //ViewHolder
    public class StaticCommandViewHolder extends RecyclerView.ViewHolder{
        private TextView name, added, number, id;

        public StaticCommandViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.static_name);
            added = (TextView) view.findViewById(R.id.static_added);
            number = (TextView) view.findViewById(R.id.static_number);
            id = (TextView) view.findViewById(R.id.static_command_id);
        }
    }
}
