package com.example.somaggiainfesta.data;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private List<String> names;
    private List<String> adds;

    public Menu(){
        this.names = new ArrayList<>();
        this.adds = new ArrayList<>();
    }

    public void addFood(String f){
        this.names.add(f);
    }

    public void addAdd(String a){
        this.adds.add(a);
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getAdds() {
        return adds;
    }

    public boolean isValid(){
        return names.size() > 0 || adds.size() > 0;
    }
}
