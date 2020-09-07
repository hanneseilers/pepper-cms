package de.fhkiel.pepper.cms.apps;

import java.util.ArrayList;

public interface PepperAppInterface {
    public void onPepperAppsLoaded(ArrayList<PepperApp> apps);
    public void setPepperAppController(PepperAppController controller);
}
