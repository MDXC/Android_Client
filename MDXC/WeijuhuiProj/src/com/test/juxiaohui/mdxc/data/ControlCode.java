package com.test.juxiaohui.mdxc.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ControlCode implements Serializable,Comparable{

	public String mControlEngName = "";
	public String mControlChinaName = "";
	public String mControlCode = "";
	public String mControlshortName = "";
	
	private static ControlCode NULL = new ControlCode();
	
	public static JSONObject toJSON(ControlCode data)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("ControlEngName", data.mControlEngName);
			obj.put("ControlChinaName", data.mControlChinaName);
			obj.put("ControlCode", data.mControlCode);
			obj.put("ControlshortName", data.mControlshortName);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public ControlCode fromJSON(JSONObject json)
	{
		ControlCode data = new ControlCode();
		try {
			data.mControlEngName = json.getString("ControlEngName");
			data.mControlChinaName = json.getString("ControlChinaName");
			data.mControlCode = json.getString("ControlCode");
			data.mControlshortName = json.getString("ControlshortName");
		} catch (JSONException e) {
			e.printStackTrace();
			data = NULL;
		}
		return data;
	}
	
	
	
	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		return mControlCode.compareToIgnoreCase(((ControlCode)another).mControlCode);
	}

}
