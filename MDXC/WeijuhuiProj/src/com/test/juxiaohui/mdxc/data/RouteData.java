package com.test.juxiaohui.mdxc.data;

import com.test.juxiaohui.mdxc.manager.AirlineManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by yihao on 15/3/13.
 */
public class RouteData {
    public String mDepartAirport = "";
    public String mArrivalAirport = "";
    public String mDepartCity = "";
    public String mArrivalCity = "";
    public String mNumbers = "";
    public String mAirlineCode = "";
    public String mAirlineName = "";
    public String mAircraft = "";
    public Date mDepartTime = new Date();
    public Date mArrivalTime = new Date();

    public int mDurTime = 0;

    public static JSONObject toJSON(RouteData routeData){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("number", routeData.mNumbers);
            jsonObject.put("airline", routeData.mAirlineCode);
            jsonObject.put("airlineName", routeData.mAirlineName);
            jsonObject.put("fromCity", routeData.mDepartCity);
            jsonObject.put("toCity", routeData.mArrivalCity);
            jsonObject.put("fromAirport", routeData.mDepartAirport);
            jsonObject.put("toAirport", routeData.mArrivalAirport);
            jsonObject.put("fromTime", FlightData.FORMAT_SEARCH.format(routeData.mDepartTime));
            jsonObject.put("toTime", FlightData.FORMAT_SEARCH.format(routeData.mArrivalTime));
            jsonObject.put("duration", routeData.mDurTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static RouteData fromJSON(JSONObject jsonObject){
        RouteData routeData = new RouteData();
        try {
            routeData.mNumbers = jsonObject.getString("number");
            routeData.mAirlineCode = jsonObject.getString("airline");
            if(jsonObject.has("airlineName")){
                routeData.mAirlineName = jsonObject.getString("airlineName");
            }
            else {
                routeData.mAirlineName = AirlineManager.getInstance().getAirlineByCode(routeData.mAirlineCode).mName;
            }
            routeData.mDepartCity = jsonObject.getString("fromCity");
            routeData.mArrivalCity = jsonObject.getString("toCity");
            routeData.mDepartAirport = jsonObject.getString("fromAirport");
            routeData.mArrivalAirport = jsonObject.getString("toAirport");

            try {
                routeData.mDepartTime = FlightData.FORMAT_SEARCH.parse(jsonObject.getString("fromTime").replace("T", " "));
                routeData.mArrivalTime = FlightData.FORMAT_SEARCH.parse(jsonObject.getString("toTime").replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            routeData.mDurTime = jsonObject.getInt("duration");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routeData;
    }
}
