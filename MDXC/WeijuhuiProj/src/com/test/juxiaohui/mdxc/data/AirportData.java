package com.test.juxiaohui.mdxc.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/5/1.
 */
public class AirportData {
    public String portCode = "";
    public String portName = "";
    public String cityId = "";

    public static AirportData NULL = new AirportData();

    public static AirportData fromJSON(JSONObject jsonObject)
    {
        AirportData airportData = new AirportData();
        try {
            airportData.portCode = jsonObject.getString("code");
            airportData.portName = jsonObject.getString("name");
            airportData.cityId = jsonObject.getString("cityId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return airportData;
    }

    public static JSONObject toJSON(AirportData airportData){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", airportData.portCode);
            jsonObject.put("name", airportData.portName);
            jsonObject.put("cityId", airportData.portCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
