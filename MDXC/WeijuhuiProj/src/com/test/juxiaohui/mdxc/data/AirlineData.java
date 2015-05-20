package com.test.juxiaohui.mdxc.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/5/19.
 */
public class AirlineData {
    public String mCode = "";
    public String mName = "";
    public String mLocalName = "";

    public static AirlineData NULL = new AirlineData();

    public static AirlineData fromJSON(JSONObject jsonObject){
        AirlineData airline = new AirlineData();
        try {
            airline.mCode = jsonObject.getString("code");
            airline.mLocalName = jsonObject.getString("localName");
            airline.mName = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return airline;
    }

    public JSONObject toJSON(AirlineData airlineData){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", airlineData.mCode);
            jsonObject.put("localName", airlineData.mLocalName);
            jsonObject.put("name", airlineData.mName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
