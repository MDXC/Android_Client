package com.test.juxiaohui.mdxc.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/3/13.
 */
public class RouteData {
    public String mDepartAirport = "";
    public String mArrivalAirport = "";
    public String mDepartCity = "";
    public String mArrivalCity = "";
//    public String mDepartDate;
//    public String mArrivalDate;
    public String mDepartTime = "";
    public String mArrivalTime = "";
    public String mNumbers = "";
    public String mAirplanes = "";

    public static JSONObject toJSON(RouteData routeData){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("departAirport", routeData.mDepartAirport);
            jsonObject.put("arrivalAirport", routeData.mArrivalAirport);
            jsonObject.put("departCity", routeData.mDepartCity);
            jsonObject.put("arrivalCity", routeData.mArrivalCity);
            jsonObject.put("departTime", routeData.mDepartTime);
            jsonObject.put("arrivalTime", routeData.mArrivalTime);
            jsonObject.put("numbers", routeData.mNumbers);
            jsonObject.put("airplanes", routeData.mAirplanes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static RouteData fromJSON(JSONObject jsonObject){
        RouteData routeData = new RouteData();
        try {
            routeData.mDepartAirport = jsonObject.getString("departAirport");
            routeData.mArrivalAirport = jsonObject.getString("arrivalAirport");
            routeData.mDepartCity = jsonObject.getString("departCity");
            routeData.mArrivalCity = jsonObject.getString("arrivalCity");
            routeData.mDepartTime = jsonObject.getString("departTime");
            routeData.mArrivalTime = jsonObject.getString("arrivalTime");
            routeData.mNumbers = jsonObject.getString("numbers");
            routeData.mAirplanes = jsonObject.getString("airplanes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routeData;
    }
}
