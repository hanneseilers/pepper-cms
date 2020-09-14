package de.fhkiel.pepper.cms.apps;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import de.fhkiel.pepper.cms.R;
import de.fhkiel.pepper.cms.users.User;

public class AppController implements PepperAppController {
    private static final String TAG = AppController.class.getName();

    private Context context;

    public AppController(Context context) {
        this.context = context;
    }

    @Override
    public boolean startPepperApp(PepperApp app, User user) {
        Log.d(TAG, "trying to start:" + app.getName() + " - " + app.getIntentPackage()+ "/" + app.getIntentClass());
        Intent intent = new Intent();
        intent.setClassName(app.getIntentPackage(), app.getIntentClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {

            JSONObject json = new JSONObject();
            json.put("game", app.toJSONObject().toString());
            if(user != null) {
                json.put("user", user.toJSONObject().toString());
                if( user.getGamedata().containsKey( app.getName() ) ){
                    json.put("data", user.getGamedata().get( app.getName() ).toString() );
                } else {
                    json.put("data", "{}");
                }
            }
            Log.i(TAG, json.toString());
            intent.putExtra("pepperapp", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e){
            Log.e(TAG, "Execption on intent: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public void loadPepperApps(PepperAppInterface callback) {
        new Thread(() -> {

            // TODO: load apps from online source

            // LOADING APPS FROM RESCOURCE FILE
            Log.d(TAG, "Loading apps from rescource file");
            JSONArray jsonPepperApps = readJSONfromRescource(R.raw.pepperapps);
            ArrayList<PepperApp> apps = jsonToPepperApps(jsonPepperApps);
            if(callback!=null){
                Log.d(TAG, "calling callback");
                callback.onPepperAppsLoaded(apps);
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
                        PepperApp app = new PepperApp();
                        app.setName(jsonObject.getString("name"));
                        app.setIntentPackage(jsonObject.getString("intentPackage"));
                        app.setIntentClass(jsonObject.getString("intentClass"));
                        // Add code to add additional rescources here
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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(rescource), Charset.forName("UTF-8")));
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
}
