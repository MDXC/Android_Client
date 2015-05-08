package com.test.juxiaohui.mdxc.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/5/1.
 */
public class AirportData {
    String portCode = "";
    String portName = "";
    String cityId = "";

    public static AirportData NULL = new AirportData();

    public AirportData fromJSON(JSONObject jsonObject)
    {
        AirportData airportData = new AirportData();
        try {
            airportData.portCode = jsonObject.getString("code");
            airportData.portName = jsonObject.getString("name");
            airportData.portCode = jsonObject.getString("cityId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return airportData;
    }


}
