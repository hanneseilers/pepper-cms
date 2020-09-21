package de.fhkiel.pepper.cms_lib.apps;

import org.json.JSONException;
import org.json.JSONObject;

import de.fhkiel.pepper.cms_lib.JSONObjectable;

/**
 * Class to store informations about different apps, available on Pepper robot.
 */
public class PepperApp implements JSONObjectable {
    private String name;
    private String intentPackage;
    private String intentClass;
    private String currentVersion = "";
    private String latestVersion = "";

    public PepperApp(String name){
        setName(name);
    }

    @SuppressWarnings(value = "unsued")
    public String getName() {
        return name;
    }

    @SuppressWarnings(value = "unsued")
    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings(value = "unsued")
    public String getCurrentVersion() {
        return currentVersion;
    }

    @SuppressWarnings(value = "unsued")
    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    @SuppressWarnings(value = "unsued")
    public String getLatestVersion() {
        return latestVersion;
    }

    @SuppressWarnings(value = "unsued")
    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    @SuppressWarnings(value = "unsued")
    public String getIntentPackage() {
        return intentPackage;
    }

    @SuppressWarnings(value = "unsued")
    public void setIntentPackage(String intentPackage) {
        this.intentPackage = intentPackage;
    }

    @SuppressWarnings(value = "unsued")
    public String getIntentClass() {
        return intentClass;
    }

    @SuppressWarnings(value = "unsued")
    public void setIntentClass(String itentClass) {
        this.intentClass = itentClass;
    }

    public String getIdentifier(){
        return getIntentPackage() + "/" + getIntentClass();
    }

    public Integer getHashCode(){
        return getIdentifier().hashCode();
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
        try{
            json.put("name", getName());
            json.put("intentPackage", getIntentPackage());
            json.put("intentClass", getIntentClass());
            json.put("currentVersion", getCurrentVersion());
            json.put("latestVersion", getLatestVersion());
        } catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }
}
