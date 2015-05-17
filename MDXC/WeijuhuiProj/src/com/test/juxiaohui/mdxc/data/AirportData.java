package com.test.juxiaohui.mdxc.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/5/1.
 */
public class AirportData implements Comparable{
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


    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Object another) {
        AirportData airportData = (AirportData) another;
        if(portCode.contains(airportData.portName)||portName.contains(airportData.portName)){
            return 0;
        }
        return 0;
    }
}
