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
    public String mDepartAirportName = "";
    public String mDepartAirportCode = "";
    public String mArrivalAirportName = "";
    public String mArrivalAirportCode = "";
    public String mDepartCityName = "";
    public String mDepartCityCode = "";
    public String mArrivalCityName = "";
    public String mArrivalCityCode = "";
    /**
     * 航班号
     */
    public String mNumbers = "";
    /**
     * 航空公司代码
     */
    public String mAirlineCode = "";
    /**
     * 航空公司名称
     */
    public String mAirlineName = "";
    /**
     * 飞行器名称
     */
    public String mAircraft = "";
    public Date mDepartTime = new Date();
    public Date mArrivalTime = new Date();

    public int mDurTime = 0;

    /**
     * 座舱类型，"Economy", "Business"
     */
    public String mCabinType = "";

    public static JSONObject toJSON(RouteData routeData){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("number", routeData.mNumbers);
            jsonObject.put("airlineCode", routeData.mAirlineCode);
            jsonObject.put("airlineName", routeData.mAirlineName);
            jsonObject.put("fromCityCode", routeData.mDepartCityCode);
            jsonObject.put("fromCityName", routeData.mDepartCityName);
            jsonObject.put("toCityCode", routeData.mArrivalCityCode);
            jsonObject.put("toCityName", routeData.mArrivalCityName);
            jsonObject.put("fromAirportCode", routeData.mDepartAirportCode);
            jsonObject.put("fromAirportName", routeData.mDepartAirportName);
            jsonObject.put("toAirportCode", routeData.mArrivalAirportCode);
            jsonObject.put("toAirportName", routeData.mArrivalAirportName);
            jsonObject.put("fromTime", FlightData.FORMAT_SEARCH.format(routeData.mDepartTime));
            jsonObject.put("toTime", FlightData.FORMAT_SEARCH.format(routeData.mArrivalTime));
            jsonObject.put("duration", routeData.mDurTime);
            jsonObject.put("cabinType", routeData.mCabinType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static RouteData fromJSON(JSONObject jsonObject){
        RouteData routeData = new RouteData();
        try {
            routeData.mNumbers = jsonObject.getString("number");
            routeData.mAirlineCode = jsonObject.getString("airlineCode");
            routeData.mAirlineName = jsonObject.getString("airlineName");
            routeData.mDepartCityCode = jsonObject.getString("fromCityCode");
            routeData.mDepartCityName = jsonObject.getString("fromCityName");
            routeData.mArrivalCityCode = jsonObject.getString("toCityCode");
            routeData.mArrivalCityName = jsonObject.getString("toCityName");
            routeData.mDepartAirportCode = jsonObject.getString("fromAirportCode");
            routeData.mDepartAirportName = jsonObject.getString("fromAirportName");
            routeData.mArrivalAirportCode = jsonObject.getString("toAirportCode");
            routeData.mArrivalAirportName = jsonObject.getString("toAirportName");
            try {
                routeData.mDepartTime = FlightData.FORMAT_SEARCH.parse(jsonObject.getString("fromTime").replace("T", " "));
                routeData.mArrivalTime = FlightData.FORMAT_SEARCH.parse(jsonObject.getString("toTime").replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            routeData.mNumbers = jsonObject.getString("duration");
            routeData.mNumbers = jsonObject.getString("cabinType");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routeData;
    }
    /**
     *
     * @param flight
     *   "KA870": {
        "airline": "KA",
        "number": "KA870",
        "aircraft": "333",
        "fromCity": "HKG",
        "toCity": "SHA",
        "fromAirport": "HKG",
        "toAirport": "PVG",
        "fromTime": "2015-05-28T21:15:00",
        "toTime": "2015-05-28T23:40:00",
        "duration": 145
        },
     * @param fromAirports
     *"fromAirports": {
            "HKG": {
            "code": "HKG",
            "name": "HONG KONG",
            "localName": "赤腊角国际机场",
            "shortLocalName": "赤腊角",
            "cityId": 58
            },
        ...
        }
     * @param toAirports
     * "toAirports": {
        "HKG": {
        "code": "HKG",
        "name": "HONG KONG",
        "localName": "赤腊角国际机场",
        "shortLocalName": "赤腊角",
        "cityId": 58
        },

     * @param cities
     *     "cities": {
                "SHA": {
                "id": 2,
                "code": "SHA",
                "name": "Shanghai",
                "localName": "上海",
                "countryId": 1,
                "provinceId": 2,
                "countryName": "中国",
                "dCity": false,
                "aCity": false,
                "tCity": false,
                "domesticCity": true
                },
                ...
            }
     * @param airlines
     * "airlines": {
        "CI": {
        "code": "CI",
        "name": "China Airlines",
        "localName": "中华航空公司",
        "shortLocalName": "中华航空"
        },

     * @return
     */
    public static RouteData fromJSON(JSONObject flight, JSONObject fromAirports, JSONObject toAirports, JSONObject cities, JSONObject airlines){
        RouteData routeData = new RouteData();
        try {
            routeData.mNumbers = flight.getString("number");
            routeData.mAirlineCode = flight.getString("airline");
            if(flight.has("airlineName")){
                routeData.mAirlineName = flight.getString("airlineName");
            }
            else {
                routeData.mAirlineName = AirlineManager.getInstance().getAirlineByCode(routeData.mAirlineCode).mName;
            }
            routeData.mDepartCityCode = flight.getString("fromCity");
            routeData.mDepartCityName = cities.getJSONObject(routeData.mDepartCityCode).getString("name");
            routeData.mArrivalCityCode = flight.getString("toCity");
            routeData.mArrivalCityName = cities.getJSONObject(routeData.mArrivalCityCode).getString("name");
            routeData.mDepartAirportCode = flight.getString("fromAirport");
            routeData.mDepartAirportName = fromAirports.getJSONObject(routeData.mDepartAirportCode).getString("name");
            routeData.mArrivalAirportCode = flight.getString("toAirport");
            routeData.mArrivalAirportName = toAirports.getJSONObject(routeData.mArrivalAirportCode).getString("name");

            routeData.mAircraft = flight.getString("aircraft");

            try {
                routeData.mDepartTime = FlightData.FORMAT_SEARCH.parse(flight.getString("fromTime").replace("T", " "));
                routeData.mArrivalTime = FlightData.FORMAT_SEARCH.parse(flight.getString("toTime").replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            routeData.mDurTime = flight.getInt("duration");
            routeData.mCabinType = flight.getString("cabinType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routeData;
    }
}
