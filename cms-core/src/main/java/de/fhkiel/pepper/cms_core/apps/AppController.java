package de.fhkiel.pepper.cms_core.apps;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private static final int REQUEST_FILE_CODE = 24149;

    private static final String localAppsPath = "config/";
    private static final String localAppsFile = "apps.local";

    private Activity activity;
    private String externalStoragePath = "games/db/";
    private HashMap<Integer, PepperApp> apps = new HashMap<>();
    private HashMap<Integer, PepperApp> updatableApps = new HashMap<>();
    private FileDescriptor appDataFile = null;
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
            String gameData = loadAppData(app.getHashCode(), user);
            if( gameData != null ){
                intent.putExtra("data", gameData );
            }
        }

        try {
            Log.d(TAG, "starting intent: " + intent);
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
        Log.d(TAG, "Loading Pepper apps from rescources");

        ArrayList<String> pepperapps = new ArrayList<>();

        // TODO: load rescources apps from online source


        // load apps from string data
        for( String data : pepperapps ){
            addPepperApps(data);
        }

    }

    /**
     * Function to load available {@link PepperApp}
     * Notifies all listeners, if apps loaded.
     *
     * @param load If set to true, apps are also loaded from online rescource
     */
    @Override
    public void getPepperApps(boolean load) {
        Log.d(TAG, "getting Pepper apps.");
        this.apps.clear();
        this.updatableApps.clear();

        new Thread(() -> {
            if (load){
                loadPepperApps();
            }

            // get offline rescources
            // TODO

            String test = "[" +
                    "  {" +
                    "    \"name\":  \"TestApp\"," +
                    "    \"intentPackage\": \"de.fhkiel.pepper.demo.app\"," +
                    "    \"intentClass\": \"de.fhkiel.pepper.demo.app.MainActivity\"," +
                    "    \"tags\": \"default category, something, else\"," +
                    "    \"downloadURL\": \"https://google.de\"" +
                    "  }" +
                    "]";

            // add Apps from string
            // TODO: Use file rescource
            addPepperApps(test);

            // process pending events
            processPrendingIntentResults();

            // save results
            savePepperApps();

            // notify listener
            for (PepperAppInterface listener : PepperAppController.pepperAppInterfaceListener) {
                Log.d(TAG, "calling callback");
                listener.onPepperAppsLoaded(this.apps);
            }

        }).start();
    }

    /**
     * Adds {@link PepperApp}s to list from {@link String} data.
     * @param data  JSON formatted {@link String} data.
     */
    private void addPepperApps(String data){
        try {

            JSONArray jsonPepperApps = jsonPepperApps = new JSONArray(data);
            ArrayList<PepperApp> appsArray = jsonToPepperApps(jsonPepperApps);

            // save apps
            for(PepperApp app : appsArray) {
                addPepperApp(app);
            }

        } catch (JSONException e){
            e.printStackTrace();
            Log.d(TAG, "Cannot read app data string:\n" + data);
        }
    }

    /**
     * Adds a {@link PepperApp} to {@link HashMap}.
     * @param app   {@link PepperApp} to add
     */
    private void addPepperApp(PepperApp app){
        int hash = app.getIdentifier().hashCode();

        if( this.apps.containsKey(hash) ){
            // check for newer version
            PepperApp localApp = this.apps.get(hash);
            if( !localApp.getCurrentVersion().equals(app.getLatestVersion()) ){
                app.setCurrentVersion( localApp.getCurrentVersion() );
                this.updatableApps.put(hash, app);
            }
        }

        // add app to list
        this.apps.put(hash, app);
    }

    /**
     * Saves {@link HashMap} of {@link PepperApp}s to file.
     */
    private void savePepperApps(){

        // create json array of loaded apps
        JSONArray jsonArray = new JSONArray();
        for( int hash : this.apps.keySet() ){
            PepperApp app = this.apps.get(hash);
            JSONObject jsonObject = app.toJSONObject();
            jsonArray.put(jsonObject);
        }

        // save data to file
        File config = getFile(localAppsPath, localAppsFile);
        saveFileData(config, jsonArray.toString());
    }

    /**
     * Converts {@link JSONArray} with {@link JSONObject}s of {@link PepperApp}s to list.
     * @param jsonArray {@link JSONArray} to parse
     * @return          {@link ArrayList}, empty list if json is not valid. Invalid app data is skipped, until other json is correct.
     */
    private ArrayList<PepperApp> jsonToPepperApps(JSONArray jsonArray){
        ArrayList<PepperApp> apps = new ArrayList<>();

        try {
            if(jsonArray!=null) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    // create object from array content and parse into PepperApp object
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PepperApp app = jsonToPepperApp(jsonObject);

                    if( app != null ) {
                        apps.add(app);
                    }

                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return apps;
    }

    /**
     * Converts {@link JSONObject} to {@link PepperApp}
     * @param jsonObject {@link JSONObject} to parse
     * @return           {@link PepperApp}, null if json is not valid
     */
    private PepperApp jsonToPepperApp(JSONObject jsonObject){
        Log.d(TAG, "processing json to PepperApp: " + jsonObject.toString());
        try {
            if (jsonObject.has("name") && jsonObject.has("intentPackage") && jsonObject.has("intentClass")) {
                PepperApp app = new PepperApp(jsonObject.getString("name"));
                app.setIntentPackage(jsonObject.getString("intentPackage"));
                app.setIntentClass(jsonObject.getString("intentClass"));
                return app;
            }
        } catch (JSONException e){
            e.printStackTrace();
            Log.w(TAG, "Cannot parse json object of app:\n" + jsonObject.toString());
        }
        return null;
    }


    /**
     * Gets list of {@link PepperApp}s, ready for update.
     *
     * @return {@link HashMap} of {@link PepperApp}s.
     */
    @Override
    public HashMap<Integer, PepperApp> getUpdateablePepperApps() {
        return this.updatableApps;
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

        } else if(requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK){
            try {
                Uri uri = data.getData();
                this.appDataFile = activity.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor();
            } catch (FileNotFoundException e){
                Log.e(TAG, "file not found:\n" + e);
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

                // get user from intent
                User user = null;
                if(intent.hasExtra("user")){
                    try {
                        user = User.fromJSONObject(new JSONObject(intent.getStringExtra("user")));
                    } catch (JSONException e){
                        Log.e(TAG, "cannot read user data from intent result:\n" + intent.getStringExtra("user"));
                    }
                }

                // save app data to file
                saveAppData(hashcode, intent.getStringExtra("data"), user);

            }
        }
    }

    /**
     * Saves app data to external files dir. And if not available or not writeable, to internal files dir.
     * Exsisting data is overwritten.
     * @param hashcode  {@link String} hash code of app
     * @param data      {@link String} data of app.
     * @param user      {@link User} data belongs to. If null, data is saved as user 'none'.
     */
    private void saveAppData(String hashcode, String data, User user){
        Log.d(TAG, "save data for app " + hashcode);
        Log.d(TAG, "user: " + (user != null ? user.toJSONObject().toString() : "none"));

        File appFile = getGameFile(hashcode, user);
        saveFileData(appFile, data);
    }

    private void saveFileData(File file, String data){
        if( data != null ) {
            try {

                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(data.getBytes());
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.w(TAG, "Cannot write data to file: " + file.getName());
            }
        }
    }

    /**
     * Loads the {@link User}s {@link PepperApp} data from stored file.
     * @param hashcode  Hashcode of {@link PepperApp}
     * @param user      {@link User} to load data for
     * @return          {@link String} of app data, null if no data found.
     */
    private String loadAppData(String hashcode, User user){
        Log.d(TAG, "load data for app " + hashcode);
        Log.d(TAG, "user: " + user.toJSONObject().toString());

        File appFile = getGameFile(hashcode, user);
        return loadFileData(appFile);
    }

    /**
     * Loads {@link String} data from file.
     * @param file  {@link File} to load data from
     * @return      {@link String} content of file, null if not found
     */
    private String loadFileData(File file){
        if(file != null){
            try {

                FileInputStream fileInputStream = new FileInputStream(file);
                DataInputStream inputStream = new DataInputStream(fileInputStream);
                BufferedReader reader = new BufferedReader(new InputStreamReader((inputStream)));
                String line;
                String data = "";
                while ((line = reader.readLine()) != null) {
                    data += line;
                }
                inputStream.close();
                return data;

            } catch (IOException e){
                e.printStackTrace();
                Log.w(TAG, "Cannot read data from file: " + file.getName());
            }
        }
        return null;
    }

    /**
     * Gets {@link PepperApp} data file for one {@link User}.
     * @param hashcode  Hashcode of {@link PepperApp}
     * @param user      {@link User} to get the data file for
     * @return          Data {@link File}
     */
    private File getGameFile(String hashcode, User user){
        if(activity != null){
            String appFileName = hashcode + ".json";
            String extFilePath = externalStoragePath + (user != null ? user.getUserFilePathName() : "none");
            return getFile(extFilePath, appFileName);
        }
        return null;
    }

    /**
     * Gets a {@link File} from storage.
     * If possible from external storage.
     * @param path      {@link String} of relative path. (No leading slash!)
     * @param filename  {@link String} of filename
     * @return          {@link File} or null, if not found.
     */
    private File getFile(String path, String filename){
        File appFile = null;
        if(activity != null){
            appFile = new File(activity.getFilesDir() + "/" + path, filename);
            if (isExternalStorageWriteable()) {
                appFile = new File(activity.getExternalFilesDir(path), filename);
            }
        }
        return appFile;
    }

    /**
     * Checks wheter or not extranal device is available and writeable
     * @return  True if external storage is writeable, false otherwise.
     */
    private static boolean isExternalStorageWriteable() {
        String extStorageState = Environment.getExternalStorageState();
        return ( Environment.MEDIA_MOUNTED.equals(extStorageState) &&
                !Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState) );
    }

}
