package com.example.somaggiainfesta.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.R;

import java.util.ArrayList;
import java.util.List;

public class StaticComAdapter extends RecyclerView.Adapter<StaticComAdapter.StaticCommandViewHolder> {
    private List<Command> commands;
    private int viewed = -1;

    public StaticComAdapter(Boolean newItemAnimation){
        this.commands = new ArrayList<>();
        if(newItemAnimation)
            this.viewed = 0;
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

        if(added.length > 0){
            for(int a=0; a<added.length - 1; a++){
                sb.append(added[a]);
                sb.append(" - ");
            }

            sb.append(added[added.length-1]);
        }

        commandViewHolder.added.setText(sb.toString());
        commandViewHolder.number.setText(String.valueOf(command.getNumber()));
        if(command.hasCashDesk()){
            commandViewHolder.id.setText(String.valueOf(command.getId() + "~" + command.getCashdesk().substring(command.getCashdesk().lastIndexOf('.') + 1)));
        }else{
            commandViewHolder.id.setText(String.valueOf(command.getId()));
        }

        if(viewed != -1 && i > viewed - 1){
            commandViewHolder.cont.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return commands.size();
    }

    public List<Command> getCommands(){ return commands; }

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

    public void viewCommands(){
        if(viewed != -1)
            viewed = commands.size();
    }

    public boolean hasCommandToView(){
        if(viewed != -1){
            return viewed < commands.size();
        }

        return false;
    }

    //ViewHolder
    public class StaticCommandViewHolder extends RecyclerView.ViewHolder{
        private TextView name, added, number, id;
        private LinearLayout cont;

        public StaticCommandViewHolder(View view){
            super(view);
            cont = (LinearLayout) view.findViewById(R.id.static_cont);
            name = (TextView) view.findViewById(R.id.static_name);
            added = (TextView) view.findViewById(R.id.static_added);
            number = (TextView) view.findViewById(R.id.static_number);
            id = (TextView) view.findViewById(R.id.static_command_id);
        }
    }
}
