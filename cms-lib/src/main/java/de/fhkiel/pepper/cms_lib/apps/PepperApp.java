package de.fhkiel.pepper.cms_lib.apps;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
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
    private Integer currentVersion = 0;
    private Integer latestVersion = 0;
    private String tags = "";
    private String downloadURL = "";

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
    public Integer getCurrentVersion() {
        return currentVersion;
    }

    @SuppressWarnings(value = "unsued")
    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    @SuppressWarnings(value = "unsued")
    public Integer getLatestVersion() {
        return latestVersion;
    }

    @SuppressWarnings(value = "unsued")
    public void setLatestVersion(Integer latestVersion) {
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

    @SuppressWarnings(value = "unsued")
    public String getTags() {
        return tags.trim();
    }

    @SuppressWarnings(value = "unsued")
    public void setTags(String tags) {
        this.tags = tags.trim();
    }

    @SuppressWarnings(value = "unsued")
    public String getDownloadURL() {
        return downloadURL;
    }

    @SuppressWarnings(value = "unsued")
    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getIdentifier(){
        return getIntentPackage() + "/" + getIntentClass();
    }

    public String getHashCode(){
        return new String(Hex.encodeHex(DigestUtils.sha1(getIdentifier())) );
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
            json.put("hashcode", getHashCode());
            json.put("tags", getTags());
            json.put("downloadURL", getDownloadURL());
            json.put("lastestVersion", getLatestVersion());
            json.put("currentVersion", getCurrentVersion());
        } catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }
}
