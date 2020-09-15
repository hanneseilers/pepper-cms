package de.fhkiel.pepper.cms_lib.apps;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import de.fhkiel.pepper.cms_lib.repository.Repository;

/**
 * Class to store informations about different apps, available on Pepper robot.
 */
public class PepperApp implements Parcelable {
    private String name;
    private String intentPackage;
    private String intentClass;
    private String currentVersion = "";
    private String latestVersion = "";

    private Repository repository;

    public PepperApp(String name){
        setName(name);
    }

    public PepperApp(Parcel in){
        setName(in.readString());
        setIntentPackage(in.readString());
        setIntentClass(in.readString());
        setCurrentVersion(in.readString());
        setLatestVersion(in.readString());
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

    public static final Parcelable.Creator<PepperApp> CREATOR = new Parcelable.Creator<PepperApp>(){
        @Override
        public PepperApp createFromParcel(Parcel parcel) {
            return new PepperApp(parcel);
        }

        @Override
        public PepperApp[] newArray(int size) {
            return new PepperApp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getIntentPackage());
        parcel.writeString(getIntentClass());
        parcel.writeString(getCurrentVersion());
        parcel.writeString(getLatestVersion());
    }

    public String toString(){
        return getName() + ": " + getIntentPackage() + "/" + getIntentClass() + " (current: " + getCurrentVersion() + ", latest: " + getLatestVersion() + ")";
    }
}
