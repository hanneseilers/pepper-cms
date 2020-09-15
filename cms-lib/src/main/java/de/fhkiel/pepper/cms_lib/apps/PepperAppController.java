package de.fhkiel.pepper.cms_lib.apps;

import de.fhkiel.pepper.cms_lib.users.User;

public interface PepperAppController {
    /**
     * Starts a {@link PepperApp} via {@link android.content.Intent}.
     *
     * @param app {@link PepperApp} to start.
     * @param user {@link User} authenticated, null if none found.
     * @return true if successful, false otherwise.
     */
    default boolean startPepperApp(PepperApp app, User user) {
        return false;
    }

    /**
     * Starts a {@link PepperApp} via {@link android.content.Intent}.
     *
     * @param app {@link PepperApp} to start.
     * @return true if successful, false otherwise.
     */
    default boolean startPepperApp(PepperApp app){
        return startPepperApp(app, null);
    }

    /**
     * Function to load available {@link PepperApp}s
     *
     * @param callback {@link PepperAppInterface} object, which is called if {@link PepperApp}s are loaded.
     */
    default void loadPepperApps(PepperAppInterface callback) {}
}
