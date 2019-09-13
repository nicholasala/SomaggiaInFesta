package com.example.somaggiainfesta.network;

import com.example.somaggiainfesta.data.Command;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import okio.ByteString;

//tool for convert commands between json and Command class
public class MessageConverter {

    public Command getCommand(String c){
        //TODO String to JSON to Command
        return null;
    }

    public String getString(Command c){
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
}
