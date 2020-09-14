package de.fhkiel.pepper.cms.users;

import org.json.JSONObject;
import java.util.HashMap;

public class User {
    public String salut = "";
    public String prename = "";
    public String lastname = "";
    // TODO birthday
    public HashMap<String, JSONObject> gamedata;    // String is game name, JSONObject ist game settings as json

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

    public HashMap<String, JSONObject> getGamedata() {
        return gamedata;
    }
}
