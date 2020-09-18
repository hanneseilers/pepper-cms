package de.fhkiel.pepper.cms;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;

import java.util.ArrayList;

import de.fhkiel.pepper.cms.apps.AppController;
import de.fhkiel.pepper.cms_lib.apps.PepperApp;
import de.fhkiel.pepper.cms_lib.apps.PepperAppController;
import de.fhkiel.pepper.cms_lib.apps.PepperAppInterface;
import de.fhkiel.pepper.cms_lib.users.User;

public class MainActivity extends AppCompatActivity implements RobotLifecycleCallbacks, PepperAppInterface {
    private static final String TAG = MainActivity.class.getName();

    private PepperAppController appController;
    private ArrayList<PepperApp> pepperApps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "app started");
        QiSDK.register(this, this);

        // Creating cms app controller
        appController = new AppController(getBaseContext());
        appController.addPepperAppInterfaceListener(this);
        Log.d(TAG, "app controller created");

        // ---- UI LOGIC BEGIN ----

        // TODO: add ui locgic
        setContentView(R.layout.activity_main);

        // ---- UI LOGIC END ----

        // after loading ui, get available apps
        Log.i(TAG, "loading apps");
        appController.loadPepperApps();
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    private void showAppsUi(ArrayList<PepperApp> apps){
        runOnUiThread(() -> {

            // TODO: handle loadig apps into ui

            // simple example
            LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
            layout.removeAllViewsInLayout();
            for(PepperApp app : apps){
                Button button = new Button(this);
                button.setText(app.getName());
                button.setOnClickListener(view -> {
                    appController.startPepperApp(app, new User());
                });
                layout.addView(button);
            }

        });
    }
    /**
     * Callback, {@link PepperAppController} uses, if {@link PepperApp}s are loaded.
     *
     * @param apps List of loaded available {@link PepperApp}s
     */
    @Override
    public void onPepperAppsLoaded(ArrayList<PepperApp> apps) {
        Log.i(TAG, "Apps loaded " + apps.size());
        this.pepperApps = apps;
        showAppsUi(this.pepperApps);
    }

    /**
     * Called, if app will be started
     *
     * @param app
     */
    @Override
    public void onAppStarted(PepperApp app) {toast(app.getName() + " started");}

    /**
     * Called if an update for a {@link PepperApp} is available
     *
     * @param app {@link PepperApp} update is available for.
     */
    @Override
    public void onAppUpdateAvailable(PepperApp app) {toast("ready for update: " + app.getName());}

    /**
     * Called if app update starts
     *
     * @param app {@link PepperApp} app updated is started for.
     */
    @Override
    public void onAppUpdate(PepperApp app) {toast("updating: " + app.getName());}

    /**
     * Called if {@link PepperApp} is updated.
     *
     * @param app {@link PepperApp} that is updated.
     */
    @Override
    public void onAppUpdated(PepperApp app) {toast("updated: " + app.getName());}

    /**
     * Called when focus is gained
     *
     * @param qiContext the robot context
     */
    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Log.d(TAG, "robot focus gained");
        runOnUiThread(() -> {
            findViewById(R.id.layoutActivityMain).invalidate();
        }); // redraw whole layout if focus comes back
    }

    /**
     * Called when focus is lost
     */
    @Override
    public void onRobotFocusLost() {
        Log.d(TAG, "robot focus lost");
    }

    /**
     * Called when focus is refused
     *
     * @param reason the reason
     */
    @Override
    public void onRobotFocusRefused(String reason) {}

    /**
     * Called to show a toast.
     * @param text
     */
    private void toast(CharSequence text){
        runOnUiThread(() -> {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });
    }
}