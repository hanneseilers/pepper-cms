package de.fhkiel.pepper.cms.apps;

import android.content.res.Resources;

import de.fhkiel.pepper.cms.repository.Repository;

public class PepperApp {
    private String name;
    private Resources iconResource;
    private String itentAction;
    private String itentCategory;
    private String currentVersion;
    private String latestVersion;

    private Repository repository;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resources getIconResource() {
        return iconResource;
    }

    public void setIconResource(Resources iconResource) {
        this.iconResource = iconResource;
    }

    public String getItentAction() {
        return itentAction;
    }

    public void setItentAction(String itentAction) {
        this.itentAction = itentAction;
    }

    public String getItentCategory() {
        return itentCategory;
    }

    public void setItentCategory(String itentCategory) {
        this.itentCategory = itentCategory;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }
}
