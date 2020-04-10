package com.example.somaggiainfesta.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.R;

import java.util.ArrayList;
import java.util.List;

public class ActiveComAdapter extends RecyclerView.Adapter<ActiveComAdapter.ActiveCommandViewHolder> {
    private List<Command> commands;

    public ActiveComAdapter(){
        this.commands = new ArrayList<>();
    }

    @NonNull
    @Override
    public ActiveCommandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.active_command_row, viewGroup, false);
        return new ActiveCommandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveCommandViewHolder commandViewHolder, int i) {
        Command command = commands.get(i);
        commandViewHolder.name.setText(command.getName());

        StringBuilder sb = new StringBuilder();
        String[] added = command.getAdded();

        if(added.length > 0){
            for(int a=0; a<added.length - 1; a++){
                sb.append(added[a]);
                sb.append(" - ");
            }

            sb.append(added[added.length-1]);
        }

        commandViewHolder.added.setText(sb.toString());
        commandViewHolder.number.setText(String.valueOf(command.getNumber()));
    }

    @Override
    public int getItemCount() {
        return commands.size();
    }

    public void removeCommand(int position){
        commands.remove(position);

        try {
            notifyItemRemoved(position);
        }catch (NullPointerException ignored){}
    }

    public Command getCommand(int position){
        return commands.get(position);
    }

    public void putCommand(Command c){
        commands.add(c);

        try {
            notifyItemInserted(commands.size() - 1);
        }catch (NullPointerException ignored){}
    }

    //ViewHolder
    public class ActiveCommandViewHolder extends RecyclerView.ViewHolder{
        private TextView name, added, number;
        public LinearLayout viewForeground;
        public RelativeLayout viewBackground;

        public ActiveCommandViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.active_name);
            added = (TextView) view.findViewById(R.id.active_added);
            number = (TextView) view.findViewById(R.id.active_number);
            viewForeground = view.findViewById(R.id.active_view_foreground);
            viewBackground = view.findViewById(R.id.active_view_background);
        }
    }
}
