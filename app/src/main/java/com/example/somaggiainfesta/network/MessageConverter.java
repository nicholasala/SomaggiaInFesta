package com.example.somaggiainfesta.network;

import com.example.somaggiainfesta.data.Command;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//tool for convert commands between json and Command class
public class MessageConverter {

    public Command stringToCommand(String s){
        try {
            JSONObject json = new JSONObject(s);
            Command converted = new Command(json.getInt("id"), json.getString("name"));
            JSONArray jAdded = json.getJSONArray("added");

            String[] sAdded = new String[jAdded.length()];
            for(int i=0; i<jAdded.length(); i++)
                sAdded[i] = jAdded.getString(i);

            converted.setAdded(sAdded);
            return converted;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String commandToString(Command c){
        JSONObject converted = new JSONObject();
        JSONArray added = new JSONArray();

        try {
            converted.put("id", c.getId());
            converted.put("name", c.getName());

            for(String s : c.getAdded())
                added.put(s);

            converted.put("added", added);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return converted.toString();
    }

    public String commandToConfString(Command c){
        JSONObject converted = new JSONObject();

        try {
            converted.put("id", c.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return converted.toString();
    }
}
