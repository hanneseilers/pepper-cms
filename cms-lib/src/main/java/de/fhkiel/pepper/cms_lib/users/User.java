package de.fhkiel.pepper.cms_lib.users;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import de.fhkiel.pepper.cms_lib.JSONObjectable;

/**
 * User representation
 */
public class User implements JSONObjectable {

    private String salut = "";
    private String prename = "";
    private String lastname = "";
    private Date birthday = new Date();
    private HashMap<String, JSONObject> gamedata = new HashMap<>();    // String is game name, JSONObject ist game settings as json

    public HashMap<String, JSONObject> getGamedata() {
        return gamedata;
    }

    public User(){}

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

    public void setSalut(String salut) {
        this.salut = salut;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String toString(){
        return toJSONObject().toString();
    }

    /**
     * @return JSONObject
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("salut", getSalut());
            json.put("prename", getPrename());
            json.put("lastname", getLastname());
            json.put("username", getUsername());
            json.put("birthday", getBirthday().getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
