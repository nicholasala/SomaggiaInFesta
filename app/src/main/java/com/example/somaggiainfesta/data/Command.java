package com.example.somaggiainfesta.data;

import java.util.Arrays;
import java.util.Objects;

public class Command {
    private int id;
    private String cashdesk;
    private String name;
    private String[] added;
    private int number;

    public Command(int id, String name) {
        this.id = id;
        this.name = name;
        this.added = new String[]{};
    }

    public Command(int id, String name, String cashdesk, String[] added) {
        this.id = id;
        this.cashdesk = cashdesk;
        this.name = name;
        this.added = added;
    }

    public Command(int id, String name, String cashdesk, int number) {
        this.id = id;
        this.cashdesk = cashdesk;
        this.name = name;
        this.number = number;
        this.added = new String[]{};
    }

    public Command(int id, String name, String cashdesk, String[] added, int number) {
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

    public String getCashdesk() {
        return cashdesk;
    }

    public void setCashdesk(String cashdesk) {
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

    public boolean hasCashDesk(){
        return this.cashdesk != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return id == command.id &&
                number == command.number &&
                Objects.equals(cashdesk, command.cashdesk) &&
                Objects.equals(name, command.name) &&
                Arrays.equals(added, command.added);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, cashdesk, name, number);
        result = 31 * result + Arrays.hashCode(added);
        return result;
    }
}
