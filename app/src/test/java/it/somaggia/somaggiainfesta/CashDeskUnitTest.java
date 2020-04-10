package it.somaggia.somaggiainfesta;

import it.somaggia.somaggiainfesta.model.Command;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CashDeskUnitTest {
    CashDesk cd;

    @Before
    public void init(){
         cd = new CashDesk();
    }

    private void populateCommands() {
        cd.putCommand(new Command(1, "Panino salsiccia", "10.27.0.6", new String[]{"maionese", "ketchup", "cipolle"}));
        cd.putCommand(new Command(2, "Patatine", "10.27.0.7", new String[]{"maionese"}));
        cd.putCommand(new Command(3, "Caviale e ketchup", "10.27.0.6", new String[]{"cipolle"}));
    }

    public void confirmCommand(int id){
        try{
            cd.onCommandConfirm(id);
        }catch (Exception ignored){}
    }

    @Test
    public void commandsNumberFlowTest() {
        cd.buildAdapters();

        populateCommands();

        assertEquals(cd.activeCommands(), 3);
        assertEquals(cd.servedCommands(), 0);

        confirmCommand(2);

        assertEquals(cd.activeCommands(), 2);
        assertEquals(cd.servedCommands(), 1);

        confirmCommand(1);
        confirmCommand(3);

        assertEquals(cd.activeCommands(), 0);
        assertEquals(cd.servedCommands(), 3);
    }

    @Test
    public void commandsDetailsFlowTest(){
        cd.buildAdapters();

        populateCommands();

        ArrayList<Command> activesCommands = new ArrayList<>(cd.getActiveCommands());

        confirmCommand(1);
        confirmCommand(2);
        confirmCommand(3);

        ArrayList<Command> servedCommands = new ArrayList<>(cd.getServedCommands());

        for(int i=0; i<servedCommands.size(); i++)
            assertEquals(activesCommands.get(i), servedCommands.get(i));
    }
}