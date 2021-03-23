package de.fhkiel.pepper.cms_core.apps;

public class PepperCMSRunnable implements Runnable {

    private static final String TAG = PepperCMSRunnable.class.getName();

    public boolean finished = false;
    private Runnable runnable;

    public PepperCMSRunnable(Runnable runnable){
        super();
        this.runnable = runnable;
    }


    @Override
    public void run() {
        runnable.run();
        finished = true;
        this.notify();
    }
}
