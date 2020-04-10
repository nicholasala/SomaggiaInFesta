package it.somaggia.somaggiainfesta.adapter.command;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.somaggia.somaggiainfesta.model.Command;
import it.somaggia.somaggiainfesta.R;;

public class StaticCommandsAdapter extends CommandsAdapter<StaticCommandsAdapter.StaticCommandViewHolder> {

    @NonNull
    @Override
    public StaticCommandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.static_command_row, viewGroup, false);
        return new StaticCommandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaticCommandViewHolder holder, int position) {
        defaultOnBindViewHolder(holder, position);

        Command command = commands.get(position);
        String id  = String.valueOf(command.getId());
        if(command.hasCashDesk())
            id += "~" + command.getCashdesk().substring(command.getCashdesk().lastIndexOf('.') + 1);

        holder.id.setText(id);
    }

    //ViewHolder
    static class StaticCommandViewHolder extends CommandsAdapter.CommandViewHolder{
        protected TextView name, added, number, id;
        protected LinearLayout cont;

        StaticCommandViewHolder(View view){
            super(view);
            cont = view.findViewById(R.id.static_cont);
            name = view.findViewById(R.id.static_name);
            added = view.findViewById(R.id.static_added);
            number = view.findViewById(R.id.static_number);
            id = view.findViewById(R.id.static_command_id);
        }
    }
}
