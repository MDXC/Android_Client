package com.test.juxiaohui.mdxc.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.mdxc.data.CityData;
import com.test.juxiaohui.mdxc.server.CitySearchServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by yihao on 15/4/8.
 */
public class CityManager {
    private ArrayList<CityData> mCities = new ArrayList<CityData>();
    private static CityManager mInstance = null;

    public static final int MAX_RECENT_CITY = 3;
    private static final String PREF_RECENT_CITIES = "recent_cities";
    public static CityManager getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new CityManager();

        }
        return mInstance;
    }

    public ArrayList<CityData> getSearchResult(String condition) {

        if(isNeedUpdate())
        {
            mCities = CitySearchServer.getInstance().getSearchResult("");
            cacheCities();
        }
        else
        {
            if(mCities.size() == 0)
            {
                readFromCache();
            }

        }

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
                    if(results.get(j).cityName.length()>i&&results.get(j).cityName.substring(i, i+1).equalsIgnoreCase(condition.substring(i, i+1)))
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

    private boolean isNeedUpdate()
    {
        return false;
    }

    private void cacheCities()
    {

    }

    public void readFromCache()
    {
        try {
            InputStream is = DemoApplication.applicationContext.getAssets().open("cities_server.txt");
            InputStreamReader isr=new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            char []buffer = new char[is.available()];
            isr.read(buffer);
            String str = String.valueOf(buffer);

            CityData[] tempDatas = null;
            try {
                JSONArray array = new JSONArray(str);
                tempDatas = new CityData[array.length()];
                for(int i=0; i<array.length(); i++)
                {
                    JSONObject json = array.getJSONObject(i);
                    CityData data = new CityData();
                    data.cityCode = json.getString("code");
                    data.cityName = json.getString("name");
                    tempDatas[i] = data;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                tempDatas = null;
            }

            if(tempDatas!=null)
            {
                Arrays.sort(tempDatas);
                mCities.clear();
                for(CityData data:tempDatas)
                {
                    mCities.add(data);
                }

            }
            is.close();
            br.close();
            isr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
	public ArrayList<CityData> getNearbyPort()
	{
		return CitySearchServer.getInstance().getNearbyPort();
	}
	
	public List<CityData> getLastSearchCities()
	{
		return CitySearchServer.getInstance().getLastSearchCities();
	}
	
	public ArrayList<CityData> getHotCities()
	{
		return CitySearchServer.getInstance().getHotCities();
	}


    /**
     *  添加 最近搜索过的城市名
     * @param cityData
     */
    public void addRecentCity(CityData cityData)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DemoApplication.applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        //mRecentCities.add(cityName);
        //editor.putStringSet(PREF_RECENT_CITIES, mRecentCities).commit();
        try {
            JSONArray jsonArray = new JSONArray(preferences.getString(PREF_RECENT_CITIES, "[]"));
            LinkedList<CityData> cityDataLinkedList = fromJSONArray(jsonArray);
            if(cityDataLinkedList.size() == MAX_RECENT_CITY){
                cityDataLinkedList.removeLast();
            }
            cityDataLinkedList.addFirst(cityData);
            editor.putString(PREF_RECENT_CITIES, toJSONArray(cityDataLinkedList).toString()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<CityData> getRecentCity(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DemoApplication.applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        ArrayList<CityData> result = new ArrayList<CityData>();
        try {
            JSONArray jsonArray = new JSONArray(preferences.getString(PREF_RECENT_CITIES, ""));
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CityData cityData = CityData.fromJSON(jsonObject);
                result.add(cityData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  result;
    }

    private LinkedList<CityData> fromJSONArray(JSONArray array)
    {
        LinkedList<CityData> cityDataLinkedList = new LinkedList<CityData>();
        for(int i=0; i<array.length(); i++){
            try {
                JSONObject jsonObject = array.getJSONObject(i);
                cityDataLinkedList.add(CityData.fromJSON(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cityDataLinkedList;
    }

    private JSONArray toJSONArray(LinkedList<CityData> cityDataLinkedList)
    {
        JSONArray jsonArray = new JSONArray();
        Iterator<CityData> iterator = cityDataLinkedList.iterator();
        while (iterator.hasNext()){
            jsonArray.put(CityData.toJSON(iterator.next()));
        }
        return jsonArray;
    }
	

}
