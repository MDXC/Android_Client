package com.test.juxiaohui.mdxc.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CityData implements Serializable,Comparable
{
	public String cityName = "";
	public String cityCode = "";
	public String cityId = "";
	public String countryName = "";
	public String countryCode = "";
	public String provinceName = "";
	public String provinceCode = "";
	
	//离我的距离 单位米  赤道长40075000米 long型可以
	public long distanceFromMe = 0;
	
	public String portName = "";
	public String portCode = "";

	public ArrayList<AirportData> airportList = new ArrayList<AirportData>();

	public static CityData NULL = new CityData();

	public CityData(String cityId, String cityName, String cityCode)
	{
		this.cityId = cityId;
		this.cityName = cityName;
		this.cityCode = cityCode;

	}

	public CityData()
	{

	}

	public static JSONObject toJSON(CityData data)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", data.cityName);
			obj.put("code", data.cityCode);
			obj.put("id", data.cityId);
			JSONArray airportList = new JSONArray();
			for(AirportData port:data.airportList){
				airportList.put(AirportData.toJSON(port));
			}
			obj.put("airports", airportList);

			//obj.put("portsList", data.airportList);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static CityData fromJSON(JSONObject json)
	{
		CityData data = new CityData();
		try {
			data.cityCode = json.getString("code");
			data.cityName = json.getString("name");
			data.cityId = json.getString("id");
			if(json.has("airports")){
				JSONArray arrayAirports = json.getJSONArray("airports");
				for(int i=0; i<arrayAirports.length(); i++){
					JSONObject jsonObject = arrayAirports.getJSONObject(i);
					data.airportList.add(AirportData.fromJSON(jsonObject));
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			data = NULL;
		}
		return data;
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
		return cityName.compareToIgnoreCase(((CityData)another).cityName);
	}
}




