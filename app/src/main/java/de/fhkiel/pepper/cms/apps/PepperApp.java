package de.fhkiel.pepper.cms.apps;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import de.fhkiel.pepper.cms.JSONObjectable;
import de.fhkiel.pepper.cms.repository.Repository;

/**
 * Class to store informations about different apps, available on Pepper robot.
 */
public class PepperApp implements JSONObjectable {
    private String name;
    private String intentPackage;
    private String intentClass;
    private String currentVersion = "";
    private String latestVersion = "";

    private Repository repository;

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

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", getName());
            json.put("currentVersion", getCurrentVersion());
            json.put("latestVersion", getLatestVersion());
            json.put("intentPackage", getIntentPackage());
            json.put("intentClass", getIntentClass());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
