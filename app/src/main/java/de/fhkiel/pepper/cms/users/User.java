package de.fhkiel.pepper.cms.users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import de.fhkiel.pepper.cms.JSONObjectable;

public class User implements JSONObjectable {

    public String salut = "";
    public String prename = "";
    public String lastname = "";
    public Date birthday = new Date();
    public HashMap<String, JSONObject> gamedata = new HashMap<>();    // String is game name, JSONObject ist game settings as json

    public HashMap<String, JSONObject> getGamedata() {
        return gamedata;
    }

    public String getUsername(){
        return getPrename().replace(" ", "") + "." + getLastname().replace(" ", "");
    }

    public String getSalut() {
        return salut;
    }

    public String getPrename() {
        return prename;
    }

    public String getLastname() {
        return lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("salut", getSalut());
            json.put("prename", getPrename());
            json.put("lastname", getLastname());
            json.put("birthday", getBirthday().getTime());
            json.put("username", getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
