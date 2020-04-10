package it.somaggia.somaggiainfesta.adapter.command;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.somaggia.somaggiainfesta.model.Command;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandsAdapter<A extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<A>{
    protected List<Command> commands;

    public CommandsAdapter(){
        commands = new ArrayList<>();
    }

    public void defaultOnBindViewHolder(@NonNull CommandViewHolder holder, int position) {
        Command command = commands.get(position);
        holder.name.setText(command.getName());

        StringBuilder sb = new StringBuilder();
        String[] added = command.getAdded();

        if(added.length > 0){
            for(int i=0; i<added.length - 1; i++){
                sb.append(added[i]);
                sb.append(" - ");
            }

            sb.append(added[added.length-1]);
        }

        holder.added.setText(sb.toString());
        holder.number.setText(String.valueOf(command.getNumber()));
    }

    @Override
    public int getItemCount() {
        return commands.size();
    }

    public int putCommand(Command c){
        commands.add(c);
        notifyItemInserted(commands.size() - 1);
        return commands.size() - 1;
    }

    public void removeCommand(int position){
        commands.remove(position);
        notifyItemRemoved(position);
    }

    public Command getCommand(int position){
        return commands.get(position);
    }

    public List<Command> getCommands(){ return commands; }

    public Command removeCommandById(int id){
        for(int i=0; i<commands.size(); i++){
            Command c = getCommand(i);
            if(c.getId() == id){
                removeCommand(i);
                return c;
            }
        }

        return null;
    }

    static abstract class CommandViewHolder extends RecyclerView.ViewHolder{
        protected TextView name, added, number;

        protected CommandViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
