package com.example.somaggiainfesta.adapters;

import com.example.somaggiainfesta.data.Command;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StaticComCommandsAdapterUnitTest {
    StaticCommandsAdapter sa;

    @Before
    public void init(){
        sa = new StaticCommandsAdapter(true);
    }

    @Test
    public void commandsNumberTest(){
        assertEquals(sa.getItemCount(), 0);

        sa.putCommand(new Command(1, "Panino salsiccia", "10.27.0.6", new String[]{"maionese", "ketchup", "cipolle"}));
        sa.putCommand(new Command(2, "Patatine", "10.27.0.7", new String[]{"maionese"}));

        assertEquals(sa.getItemCount(), 2);

        sa.putCommand(new Command(3, "Patatine", "10.27.0.7", new String[]{"maionese"}));

        assertEquals(sa.getItemCount(), 3);

        sa.removeCommandById(1);
        sa.removeCommandById(2);

        assertEquals(sa.getItemCount(), 1);

        sa.putCommand(new Command(4, "Panino salsiccia", "10.27.0.6", new String[]{"maionese", "ketchup", "cipolle"}));

        assertEquals(sa.getItemCount(), 2);

        sa.removeCommandById(3);
        sa.removeCommandById(4);

        assertEquals(sa.getItemCount(), 0);
    }

    @Test
    public void viewCommandsTest(){
        assertEquals(sa.commandsToViewNumber(), 0);

        sa.putCommand(new Command(1, "Panino salsiccia", "10.27.0.6", new String[]{"maionese", "ketchup", "cipolle"}));
        sa.putCommand(new Command(2, "Patatine", "10.27.0.7", new String[]{"maionese"}));

        assertEquals(sa.commandsToViewNumber(), 2);
        assertTrue(sa.hasCommandToView());

        sa.viewCommands();

        assertEquals(sa.commandsToViewNumber(), 0);
        assertFalse(sa.hasCommandToView());
        assertEquals(sa.getItemCount(), 2);

        sa.putCommand(new Command(3, "Caviale e ketchup", "10.27.0.6", new String[]{"cipolle"}));
        sa.putCommand(new Command(4, "Caviale e maionese", "10.27.0.6", new String[]{}));
        sa.putCommand(new Command(5, "Patatine", "10.27.0.8", new String[]{"maionese"}));

        assertEquals(sa.commandsToViewNumber(), 3);
        assertTrue(sa.hasCommandToView());
        assertEquals(sa.getItemCount(), 5);

        sa.viewCommands();

        assertEquals(sa.commandsToViewNumber(), 0);
        assertFalse(sa.hasCommandToView());
    }

    @Test
    public void removeCommandByIdTest(){
        assertNull(sa.removeCommandById(1));
        assertNull(sa.removeCommandById(18));

        Command c1 = new Command(3, "Caviale e ketchup", "10.27.0.6", new String[]{"cipolle"});
        Command c2 = new Command(4, "Caviale e maionese", "10.27.0.6", new String[]{});

        sa.putCommand(c1);
        sa.putCommand(c2);

        assertEquals(sa.removeCommandById(3), c1);
        assertEquals(sa.removeCommandById(4), c2);
        assertNull(sa.removeCommandById(3));
        assertNull(sa.removeCommandById(4));
    }
}