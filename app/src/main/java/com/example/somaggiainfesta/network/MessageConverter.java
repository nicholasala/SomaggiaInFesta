package com.example.somaggiainfesta.network;

import com.example.somaggiainfesta.data.Command;
import com.example.somaggiainfesta.data.Keys;
import com.example.somaggiainfesta.data.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//tool for convert commands between json - Command - Menu
//TODO https://msgpack.org/
public class MessageConverter {

    public String getHandshakeText(){
        try{
            JSONObject message = new JSONObject();
            message.put("code", Keys.MessageCode.handShake);
            message.put("message", "Hello!");
            return message.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Command stringToCommand(String s){
        try {
            JSONObject json = new JSONObject(s);
            JSONArray jAdded = json.getJSONArray("added");

            String[] sAdded = new String[jAdded.length()];
            for(int i=0; i<jAdded.length(); i++)
                sAdded[i] = jAdded.getString(i);

            return new Command(json.getInt("id"), json.getString("name"), sAdded, json.getInt("number"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String commandToString(Command c){
        JSONObject converted = new JSONObject();
        JSONArray added = new JSONArray();

        try {
            converted.put("code", Keys.MessageCode.command);
            converted.put("id", c.getId());
            converted.put("name", c.getName());
            converted.put("number", c.getNumber());

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
            converted.put("code", Keys.MessageCode.confirmCommand);
            converted.put("id", c.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return converted.toString();
    }

    public String menuToString(Menu m){
        JSONObject menu = new JSONObject();
        JSONArray names = new JSONArray();
        JSONArray adds = new JSONArray();

        for(String s : m.getNames())
            names.put(s);

        for(String s : m.getAdds())
            adds.put(s);

        try {
            menu.put("code", Keys.MessageCode.menu);
            menu.put("names", names);
            menu.put("added", adds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return menu.toString();
    }

    public Menu stringToMenu(String s){
        try {
            JSONObject json = new JSONObject(s);
            Menu m = new Menu();
            JSONArray jNames = json.getJSONArray("names");
            JSONArray jAdded = json.getJSONArray("added");

            for(int i=0; i<jNames.length(); i++)
                m.addFood(jNames.getString(i));

            for(int i=0; i<jAdded.length(); i++)
                m.addAdd(jAdded.getString(i));

            return m;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getId(String s){
        try {
            return new JSONObject(s).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
