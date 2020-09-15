package de.fhkiel.pepper.cms_lib.apps;

import java.util.ArrayList;

public interface PepperAppInterface {
    /**
     * Callback, {@link PepperAppController} uses, if {@link PepperApp}s are loaded.
     *
     * @param apps List of loaded available {@link PepperApp}s
     */
    default void onPepperAppsLoaded(ArrayList<PepperApp> apps) {}
}
