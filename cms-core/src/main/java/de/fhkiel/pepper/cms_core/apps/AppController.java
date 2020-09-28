package de.fhkiel.pepper.cms_core.apps;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import de.fhkiel.pepper.cms_lib.users.User;
import de.fhkiel.pepper.cms_lib.apps.PepperApp;
import de.fhkiel.pepper.cms_lib.apps.PepperAppController;
import de.fhkiel.pepper.cms_lib.apps.PepperAppInterface;

public class AppController implements PepperAppController {
    private static final String TAG = AppController.class.getName();
    private static final int REQUEST_CODE = 53;

    private Activity activity;
    private String externalStoragePath = "games/db/";
    private HashMap<Integer, PepperApp> apps = new HashMap<>();
    private HashMap<String, Intent>  pendingIntentResults = new HashMap<>();

    public AppController(Activity activity) { this.activity = activity; }

    @Override
    public boolean startPepperApp(PepperApp app, User user) {
        Log.d(TAG, "trying to start:" + app.getName() + " - " + app.getIntentPackage()+ "/" + app.getIntentClass());

        // process pending intent results
        processPrendingIntentResults();

        // create intent
        Intent intent = new Intent();
        intent.setClassName(app.getIntentPackage(), app.getIntentClass());

        intent.putExtra("app", app.toJSONObject().toString() );
        if(user != null && user.getUsername().trim().replace(".", "").length() > 0) {
            intent.putExtra("user", user.toJSONObject().toString());
            if( user.getGamedata().containsKey( app.getName() ) ){
                intent.putExtra("data", user.getGamedata().get( app.getName() ).toString() );
            }
        }

        try {
            activity.startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e){
            Log.e(TAG, "Execption on intent: " + e.getMessage());
            return false;
        }

        // notify about app start
        notifyOnAppStart(app);

        return true;
    }

    @Override
    public void loadPepperApps() {
        new Thread(() -> {

            // TODO: load apps from online source

            // TODO: Modify pepper app list
            String pepperapps = "[" +
                    "  {" +
                    "    \"name\":  \"TestApp\"," +
                    "    \"intentPackage\": \"de.fhkiel.pepper.demo.app\"," +
                    "    \"intentClass\": \"de.fhkiel.pepper.demo.app.MainActivity\"" +
                    "  }" +
                    "]";

            // LOADING APPS FROM RESCOURCE FILE
            Log.d(TAG, "Loading apps from rescource file");
            try {
                JSONArray jsonPepperApps = jsonPepperApps = new JSONArray(pepperapps);
                ArrayList<PepperApp> apps = jsonToPepperApps(jsonPepperApps);

                // save apps
                for(PepperApp app : apps) {
                    int hash = app.getIdentifier().hashCode();
                    this.apps.put(hash, app);
                }

                // process pending events
                processPrendingIntentResults();

                // notify listener
                for (PepperAppInterface listener : PepperAppController.pepperAppInterfaceListener) {
                    Log.d(TAG, "calling callback");
                    listener.onPepperAppsLoaded(apps);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

        }).start();
    }

    /*
     * Converts a @JSONArray into @ArrayList of @PepperApp objects.
     */
    private ArrayList<PepperApp> jsonToPepperApps(JSONArray jsonArray){
        ArrayList<PepperApp> apps = new ArrayList<>();

        try {
            if(jsonArray!=null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d(TAG, "processing json to PepperApp: " + jsonObject.toString());
                    if (jsonObject.has("name") && jsonObject.has("intentPackage") && jsonObject.has("intentClass")) {
                        PepperApp app = new PepperApp(jsonObject.getString("name"));
                        app.setIntentPackage(jsonObject.getString("intentPackage"));
                        app.setIntentClass(jsonObject.getString("intentClass"));
                        // TODO: Add code to add additional rescources here
                        apps.add(app);
                    }
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return apps;
    }

    /*
     * Reading json data into @JSONArray from resource file.
     */
    private JSONArray readJSONfromRescource(int rescource){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(activity.getResources().openRawResource(rescource), Charset.forName("UTF-8")));
        String json = "";
        String line = "";

        try {
            while ((line = bufferedReader.readLine()) != null) {
                json += line;
            }
            bufferedReader.close();
            return new JSONArray(json);
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    private void notifyOnAppStart(PepperApp app){
        for(PepperAppInterface listener : PepperAppController.pepperAppInterfaceListener){
            listener.onAppStarted(app);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "received intent result data, request: " + requestCode + " , result: " + (resultCode == Activity.RESULT_OK ? "ok" : "not ok") );
        if( requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK
                && data != null && data.hasExtra("app") ){

            try {
                JSONObject appData = new JSONObject(data.getStringExtra("app"));
                if(appData.has("hashcode")) {
                    pendingIntentResults.put(appData.getString("hashcode"), data);
                    processPrendingIntentResults();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void setExternalStoragePath(String path) {
        externalStoragePath = path;
    }

    private void processPrendingIntentResults(){
        Log.d(TAG, "processing " + pendingIntentResults.size() + " peding intent results");
        for( String hashcode : pendingIntentResults.keySet() ){
            Intent intent = pendingIntentResults.get(hashcode);
            if( intent.hasExtra("data") ){
                saveAppData(hashcode, intent.getStringExtra("data"), null);
            }
        }
    }

    /**
     * Saves app data to external files dir. And if not available or not writeable, to internal files dir.
     * Exsisting data is overwritten.
     * @param hashcode  {@link String} hash code of app
     * @param data      {@link String} data of app.
     * @param username  {@link String} username data belongs to. If null, data is saved as user 'none'.
     */
    private void saveAppData(String hashcode, String data, String username){
        Log.d(TAG, "save app data for app " + hashcode);
        if(activity != null && data != null) {
            // TODO: Replace username with unique name incl. birthday.
            String appFileName = hashcode + ".json";
            String extFilePtah = externalStoragePath + (username != null ? username : "none");
            File appFile = null;
            if (isExternalStorageWriteable()) {
                appFile = new File(activity.getExternalFilesDir(extFilePtah), appFileName);
            } else {
                appFile = new File(activity.getFilesDir() + "/" + extFilePtah, appFileName);
            }

            // save data
            try {
                Log.d(TAG, "saving app data to " + appFile + ":\n" + data);
                FileOutputStream outputStream = new FileOutputStream(appFile);
                outputStream.write(data.getBytes());
                outputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "Cannot write to file: " + e);
            }
        }
    }

    private static boolean isExternalStorageWriteable() {
        String extStorageState = Environment.getExternalStorageState();
        return ( Environment.MEDIA_MOUNTED.equals(extStorageState) &&
                !Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState) );
    }

}
