package de.fhkiel.pepper.cms_lib.apps;

import android.content.Intent;

import java.util.ArrayList;

import de.fhkiel.pepper.cms_lib.users.User;

/**
 * Interface of objects that can control the app handling.
 * Implementing functions to load and start {@link PepperApp}s.
 */
public interface PepperAppController {

    ArrayList<PepperAppInterface> pepperAppInterfaceListener = new ArrayList<>();

    /**
     * Starts a {@link PepperApp} via {@link android.content.Intent}.
     *
     * @param app {@link PepperApp} to start.
     * @param user {@link User} authenticated, null if none found.
     * @return true if successful, false otherwise.
     */
    boolean startPepperApp(PepperApp app, User user);

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
     * Function to load available {@link PepperApp}
     * Notifies all listeners, if apps loaded.
     */
    void loadPepperApps();

    default void addPepperAppInterfaceListener(PepperAppInterface listener){
        if(!pepperAppInterfaceListener.contains(listener)){
            pepperAppInterfaceListener.add(listener);
        }
    };

    default void removePepperAppInterfaceListener(PepperAppInterface listener){
        if(pepperAppInterfaceListener.contains(listener)){
            pepperAppInterfaceListener.remove(listener);
        }
    }

    /**
     * Needs to be called, if intent return result. Handles whatever needs to be done with the rsult of an app.
     * @param requestCode   {@link Integer} of returns request code
     * @param resultCode    {@link Integer} of returns result code (for example {@link android.app.Activity}.RESULT_OK [-1] if app exsited gracefully)
     * @param data          {@link String} of data to handle (for example store) for an app.
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * Sets path where to store game files.     *
     * @param path  {@link String} of path
     */
    void setExternalStoragePath(String path);

}
