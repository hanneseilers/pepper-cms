package de.fhkiel.pepper.cms.apps;

public interface PepperAppController {
    /**
     * Starts a {@link PepperApp} via {@link android.content.Intent}.
     * @param app   {@link PepperApp} to start.
     * @return      true if successful, false otherwise.
     */
    public boolean startPepperApp(PepperApp app);

    /**
     * Function to load available {@link PepperApp}s
     * @param callback  {@link PepperAppInterface} object, which is called if {@link PepperApp}s are loaded.
     */
    public void loadPepperApps(PepperAppInterface callback);
}
