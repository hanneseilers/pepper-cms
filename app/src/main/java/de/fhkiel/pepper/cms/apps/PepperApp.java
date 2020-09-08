package de.fhkiel.pepper.cms.apps;

import de.fhkiel.pepper.cms.repository.Repository;

/**
 * Class to store informations about different apps, available on Pepper robot.
 */
public class PepperApp {
    private String name;
    private String itentAction;
    private String itentCategory;
    private String currentVersion;
    private String latestVersion;

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
    public String getItentAction() {
        return itentAction;
    }

    @SuppressWarnings(value = "unsued")
    public void setItentAction(String itentAction) {
        this.itentAction = itentAction;
    }

    public String getItentCategory() {
        return itentCategory;
    }

    @SuppressWarnings(value = "unsued")
    public void setItentCategory(String itentCategory) {
        this.itentCategory = itentCategory;
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
}
