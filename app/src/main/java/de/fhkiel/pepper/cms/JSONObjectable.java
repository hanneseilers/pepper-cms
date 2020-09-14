package de.fhkiel.pepper.cms;

import org.json.JSONObject;

public interface JSONObjectable {

    /**
     * @return JSONObject
     */
    default JSONObject toJSONObject(){
        return new JSONObject();
    }
}
