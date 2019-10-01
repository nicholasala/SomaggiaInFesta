package com.example.somaggiainfesta;

import com.example.somaggiainfesta.data.Command;

import org.junit.Test;

import static org.junit.Assert.*;

public class CashDeskUnitTest {

    @Test
    public void commandsFlow() {
        CashDesk cd = new CashDesk();
        cd.buildAdapters();

        cd.putCommand(new Command(1, "Panino"));
        cd.putCommand(new Command(2, "Panino"));
        cd.putCommand(new Command(3, "Panino"));

        assertEquals(cd.activeCommands(), 3);
        assertEquals(cd.servedCommands(), 0);

        try{
            cd.onCommandConfirm(2);
        }catch (Exception ignored){}

        assertEquals(cd.activeCommands(), 2);
        assertEquals(cd.servedCommands(), 1);

        try{
            cd.onCommandConfirm(1);
        }catch (Exception ignored){}

        try{
            cd.onCommandConfirm(3);
        }catch (Exception ignored){}

        assertEquals(cd.activeCommands(), 0);
        assertEquals(cd.servedCommands(), 3);
    }
}