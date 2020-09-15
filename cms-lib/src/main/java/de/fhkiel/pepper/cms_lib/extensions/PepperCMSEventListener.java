package de.fhkiel.pepper.cms_lib.extensions;

import de.fhkiel.pepper.cms_lib.apps.PepperApp;
import de.fhkiel.pepper.cms_lib.users.User;

/**
 * Interface for extensions to implement to get event data from cms
 */
public interface PepperCMSEventListener {

    /**
     * Request, if a app can start.
     * @param app   {@link PepperApp} to start.
     * @return  Should return true, to start the app. If false returned, app is not started.
     */
    boolean onRequestAppStart(PepperApp app);

    /**
     * Function to get restart delay fo an App.
     * Called, if app start request failed, this function will be called to get delay when to try a retry.
     * @param app   {@link PepperApp} to aks request delay for.
     * @return
     */
    int getAppRestartDelay(PepperApp app);

    /**
     * Called, if app will be started
     * @param app
     */
    void onAppStarted(PepperApp app);

    /**
     * Called if an update for a {@link PepperApp} is available
     * @param app   {@link PepperApp} update is available for.
     */
    void onAppUpdateAvailable(PepperApp app);

    /**
     * Called if app update starts
     * @param app   {@link PepperApp} app updated is started for.
     */
    void onAppUpdate(PepperApp app);

    /**
     * Called if {@link PepperApp} is updated.
     * @param app   {@link PepperApp} that is updated.
     */
    void onApUpdated(PepperApp app);

    /**
     * Called if systems gains robot focus
     */
    void onRobotFocusGained();

    /**
     * Called if system looses robot system.
     */
    void onRobotFocusLost();

    /**
     * Called to request extension start.
     */
    void onStart();

    /**
     * Called to request extension stop.
     */
    void onStop();

    /**
     * called if autheticated user changed.
     * @param user  {@link User} of new user.
     */
    void onUserChanged(User user);
}
