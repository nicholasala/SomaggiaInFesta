package com.example.somaggiainfesta.data;

public class Command {
    private int id;
    private int cashdesk;
    private String name;
    private String[] added;
    private int number;

    public Command(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Command(int id, String name, int cashdesk) {
        this.id = id;
        this.cashdesk = cashdesk;
        this.name = name;
    }

    public Command(int id, String name, int cashdesk, String[] added) {
        this.id = id;
        this.cashdesk = cashdesk;
        this.name = name;
        this.added = added;
    }

    public Command(int id, String name, int cashdesk, String[] added, int number) {
        this.id = id;
        this.cashdesk = cashdesk;
        this.name = name;
        this.added = added;
        this.number = number;
    }

    public Command(int id, String name, String[] added, int number) {
        this.id = id;
        this.name = name;
        this.added = added;
        this.number = number;
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

    public String[] getAdded() {
        return added;
    }

    public void setAdded(String[] added) {
        this.added = added;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
