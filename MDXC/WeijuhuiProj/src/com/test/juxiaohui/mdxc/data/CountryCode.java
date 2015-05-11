package com.test.juxiaohui.mdxc.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class CountryCode implements Serializable,Comparable{

	public String mEngName = "";
	public String mChinaName = "";
	public String mCode = "";
	public String mShortName = "";
	
	public static CountryCode NULL = new CountryCode("", "", "", "");

	public CountryCode(String engName, String chinaName, String code, String shortName)
	{
		mEngName = engName;
		mChinaName = chinaName;
		mCode = code;
		mShortName = shortName;
	}
	
	public static JSONObject toJSON(CountryCode data)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("engName", data.mEngName);
			obj.put("chinaName", data.mChinaName);
			obj.put("code", data.mCode);
			obj.put("shortName", data.mShortName);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public CountryCode fromJSON(JSONObject json)
	{
		CountryCode data = new CountryCode("", "", "", "");
		try {
			data.mEngName = json.getString("engName");
			data.mChinaName = json.getString("chinaName");
			data.mCode = json.getString("code");
			data.mShortName = json.getString("shortName");
		} catch (JSONException e) {
			e.printStackTrace();
			data = NULL;
		}
		return data;
	}
	
	
	
	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		return mCode.compareToIgnoreCase(((CountryCode)another).mEngName);
	}

	public static List<CountryCode> getDefaultCodes()
	{
//    +95 Myanmar
//        +86 China
//        +65 Singapore
//        +66 Thailand
//        +81 Japan
//        +82 Korea
//        +852 Hong Kong
//    +1 US&Canada
//        +44 United Kindom
//    +61 Australia
		List<CountryCode> codeList = new ArrayList<CountryCode>();

		codeList.add(new CountryCode("", "", "+95", ""));
		codeList.add(new CountryCode("","", "+86", ""));
		codeList.add(new CountryCode("","", "+65", ""));
		codeList.add(new CountryCode("","", "+66", ""));
		codeList.add(new CountryCode("","", "+81", ""));
		codeList.add(new CountryCode("","", "+82", ""));
		codeList.add(new CountryCode("","", "+852", ""));
		codeList.add(new CountryCode("","", "+1", ""));
		codeList.add(new CountryCode("","", "+44", ""));
		codeList.add(new CountryCode("","", "+61", ""));
		return codeList;
	}

	public static List<String> convertCodeListToString(List<CountryCode> codeList)
	{
		List<String> listStr = new ArrayList<String>();
		for (CountryCode code:codeList) {
			listStr.add(code.mCode);
		}
		return listStr;
	}

}
