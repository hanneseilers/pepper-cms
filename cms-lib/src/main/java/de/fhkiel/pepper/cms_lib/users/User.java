package de.fhkiel.pepper.cms_lib.users;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;


public class User implements Parcelable {

    private String salut = "";
    private String prename = "";
    private String lastname = "";
    private Date birthday = new Date();
    private HashMap<String, JSONObject> gamedata = new HashMap<>();    // String is game name, JSONObject ist game settings as json

    public HashMap<String, JSONObject> getGamedata() {
        return gamedata;
    }

    public User(){}

    public User(Parcel in){
        setSalut(in.readString());
        setPrename(in.readString());
        setLastname(in.readString());
        setBirthday( new Date(in.readLong()) );
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

    public void addGameData(String name, JSONObject json){
        if(this.gamedata == null){
            this.gamedata = new HashMap<>();
        }

        this.gamedata.put(name, json);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){

        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getSalut());
        parcel.writeString(getPrename());
        parcel.writeString(getLastname());
        parcel.writeString(getUsername());
        parcel.writeLong(getBirthday().getTime());
    }

    public String toString(){
        return getUsername() + ": " + getSalut() + " " + getPrename() + " " + getLastname() + " (birthday: " + getBirthday() + ", games: " + getGamedata().size() + ")";
    }
}
