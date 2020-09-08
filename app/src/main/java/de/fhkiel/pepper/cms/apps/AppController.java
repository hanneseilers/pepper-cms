package de.fhkiel.pepper.cms.apps;

import android.content.Context;
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

public class AppController implements PepperAppController {
    private static final String TAG = AppController.class.getName();

    private Context context;

    public AppController(Context context) {
        this.context = context;
    }

    @Override
    public boolean startPepperApp(PepperApp app) {
        Log.i(TAG, "starting:" + app.getName());
        Log.i(TAG, app.getItentCategory()+ ":" + app.getItentAction());
        return false;
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
                    if (jsonObject.has("name") && jsonObject.has("intentAction") && jsonObject.has("intentCategory")) {
                        PepperApp app = new PepperApp();
                        app.setName(jsonObject.getString("name"));
                        app.setItentAction(jsonObject.getString("intentAction"));
                        app.setItentCategory(jsonObject.getString("intentCategory"));
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