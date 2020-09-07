package de.fhkiel.pepper.cms.apps;

import android.content.res.Resources;

public class PepperApp {
    private String name;
    private Resources iconResource;
    private String itentAction;
    private String itentCategory;

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
}
