package com.example.somaggiainfesta;

import java.util.ArrayList;

public class Command {
    private int id;
    private int cashdesk;
    private String name;
    private ArrayList<String> added;

    public Command(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Command(int id, int cashdesk, String name) {
        this.id = id;
        this.cashdesk = cashdesk;
        this.name = name;
    }

    public Command(int id, int cashdesk, String name, ArrayList<String> added) {
        this.id = id;
        this.cashdesk = cashdesk;
        this.name = name;
        this.added = added;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCashdesk() {
        return cashdesk;
    }

    public void setCashdesk(int cashdesk) {
        this.cashdesk = cashdesk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAdded() {
        return added;
    }

    public void setAdded(ArrayList<String> added) {
        this.added = added;
    }
}
