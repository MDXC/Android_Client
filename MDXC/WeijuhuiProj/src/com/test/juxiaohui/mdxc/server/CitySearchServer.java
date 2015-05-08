package com.test.juxiaohui.mdxc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import android.content.Context;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.common.dal.ICitySearchServer;
import com.test.juxiaohui.mdxc.data.AirportData;
import com.test.juxiaohui.mdxc.data.CityData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CitySearchServer implements ICitySearchServer {
//	public String[] mHotCityList = {"Yangon", "Mandalay", "Nay Pyi Taw", "Hongkong", "Singarpore", "Bangkok",
//	"Beijing", "Kunming", "Guangzhou"};
	private static CitySearchServer mInstance = null;
	private HashSet<String> mRecentCities = new HashSet<String>();
	private static final String PREF_RECENT_CITIES = "recent_cities";
	private ArrayList<CityData> mHotCityList = new ArrayList<CityData>();
	public static final int MAX_RECENT_CITY = 3;
	public static CitySearchServer getInstance()
	{
		if(mInstance == null)
		{
			mInstance = new CitySearchServer();
		}
		return mInstance;
	}
	
	public CitySearchServer()
	{
		createFromFile();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DemoApplication.applicationContext);
		//preferences.getStringSet(PREF_RECENT_CITIES, mRecentCities);
	}
	

	@Override
	public ArrayList<CityData> getNearbyPort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CityData> getLastSearchCities() {
		// TODO Auto-generated method stub
		Iterator<String> iterator = mRecentCities.iterator();
		List<CityData> result = new ArrayList<CityData>();
		while (iterator.hasNext())
		{
			CityData data = new CityData();
			data.cityName = iterator.next();
			result.add(data);
		}
		return result;
	}

	@Override
	public ArrayList<CityData> getHotCities() {
		if(mHotCityList.size()==0)
		{
			mHotCityList.add(new CityData("522", "Yangon", "RGN"));
			mHotCityList.add(new CityData("4080", "Mandalay", "MDL"));
			mHotCityList.add(new CityData("", "Nay Pyi Taw", ""));
			mHotCityList.add(new CityData("58", "Hong Kong", "HKG"));
			mHotCityList.add(new CityData("73", "Singapore", "SIN"));
			mHotCityList.add(new CityData("359", "Bangkok", "BKK"));
			mHotCityList.add(new CityData("1", "Beijing", "BJS"));
			mHotCityList.add(new CityData("34", "Kunming", "KMG"));
			mHotCityList.add(new CityData("32", "Guangzhou", "CAN"));
		}
		return mHotCityList;
	}

	@Override
	public ArrayList<CityData> getSearchResult(String condition) {
		if(condition==null||condition.length()==0)
		{
			return mCities;
		}
		else
		{
			ArrayList<CityData> results = (ArrayList<CityData>) mCities.clone();
			ArrayList<CityData> temp = new ArrayList<CityData>();
			for(int i=0; i<condition.length(); i++)
			{
				for(int j=0; j<results.size(); j++)
				{
					if(results.get(j).cityName.substring(i, i+1).equalsIgnoreCase(condition.substring(i, i+1)))
					{
						temp.add(results.get(j));
					}
				}
				results = (ArrayList<CityData>) temp.clone();
				temp.clear();
			}
			return results;
		}
	}

	@Override
	public List<AirportData> getAirportsinCity(String cityCode) {
		return null;
	}


	private ArrayList<CityData> mCities = new ArrayList<CityData>();
	private void createFromFile()
	{	
		
		try {
			InputStream is = DemoApplication.applicationContext.getAssets().open("cities.txt");
	        InputStreamReader isr=new InputStreamReader(is, "UTF-8");
	        BufferedReader br = new BufferedReader(isr);
	        
	        String line="";
	        String[] arrs=null;
	        while ((line=br.readLine())!=null) {
	            arrs=line.split(",");
	            CityData city = new CityData();
	            city.cityName = arrs[2].trim();
	            city.portCode = arrs[0].trim();
	            mCities.add(city);
	        }
	        is.close();
	        br.close();
	        isr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}





}
