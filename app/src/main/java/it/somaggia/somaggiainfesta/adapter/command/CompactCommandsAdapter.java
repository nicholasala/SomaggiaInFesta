package it.somaggia.somaggiainfesta.adapter.command;

import it.somaggia.somaggiainfesta.model.Command;

//Static commands with unique name values
public class CompactCommandsAdapter extends StaticCommandsAdapter {

    @Override
    public int putCommand(Command c) {
        for(int i=0; i<commands.size(); i++){
            Command element = commands.get(i);
            if(element.getName().equals(c.getName())){
                element.setNumber(element.getNumber() + c.getNumber());
                commands.set(i, element);
                notifyDataSetChanged();
                return i;
            }
        }

        return super.putCommand(c);
    }
}
