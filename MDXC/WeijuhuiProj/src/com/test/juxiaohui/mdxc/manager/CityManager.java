package com.test.juxiaohui.mdxc.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.mdxc.data.AirportData;
import com.test.juxiaohui.mdxc.data.CityData;
import com.test.juxiaohui.mdxc.server.CitySearchServer;
import com.test.juxiaohui.utils.DoubleArrayTrie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

/**
 * Created by yihao on 15/4/8.
 */
public class CityManager {
    private ArrayList<CityData> mCityList = new ArrayList<CityData>();
    private SparseIntArray mCityKeyMap = new SparseIntArray();
    private List<String> mCityKeyList = new ArrayList<String>();
    DoubleArrayTrie mCityTrie = new DoubleArrayTrie();

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

//        if(isNeedUpdate())
//        {
//            mCityList = CitySearchServer.getInstance().getSearchResult("");
//            cacheCities();
//        }
//        else
//        {
//            if(mCityList.size() == 0)
//            {
//                readFromCache();
//            }
//
//        }
//
//        if(condition==null||condition.length()==0)
//        {
//            return mCityList;
//        }
//        else
//        {
//            ArrayList<CityData> results = (ArrayList<CityData>) mCityList.clone();
//            ArrayList<CityData> temp = new ArrayList<CityData>();
//            for(int i=0; i<condition.length(); i++)
//            {
//                for(int j=0; j<results.size(); j++)
//                {
//                    if(results.get(j).cityName.length()>i&&results.get(j).cityName.substring(i, i+1).equalsIgnoreCase(condition.substring(i, i+1)))
//                    {
//                        temp.add(results.get(j));
//                    }
//                }
//                results = (ArrayList<CityData>) temp.clone();
//                temp.clear();
//            }
//            return results;
//        }
//        ArrayList<String> posList = mCityTrie.FindAllWords(condition);
//        ArrayList<CityData> result = new ArrayList<CityData>();
//        for(Integer pos:posList){
//            result.add(mCityList.get(pos));
//        }
        ArrayList<CityData> result = new ArrayList<CityData>();
        for(CityData city:mCityList){
            if(city.cityName.toLowerCase().contains(condition)){
                result.add(city);
                continue;
            }
            for(AirportData airport:city.airportList){
                if(airport.portName.toLowerCase().contains(condition)){
                    result.add(city);
                    continue;
                }
            }
        }
        return result;

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

//        HashMap<String, ArrayList<AirportData>> portMap = new HashMap<String,ArrayList< AirportData>>();
//        try {
//            InputStream is = DemoApplication.applicationContext.getAssets().open("all_airports.txt");
//            InputStreamReader isr=new InputStreamReader(is, "UTF-8");
//            BufferedReader br = new BufferedReader(isr);
//            char []buffer = new char[is.available()];
//            isr.read(buffer);
//            String str = String.valueOf(buffer);
//
//            //AirportData[] tempDatas = null;
//
//            try {
//                JSONArray array = new JSONArray(str);
//                //tempDatas = new AirportData[array.length()];
//                for(int i=0; i<array.length(); i++)
//                {
//                    JSONObject json = array.getJSONObject(i);
//                    AirportData data = new AirportData();
//                    data.portCode = json.getString("code");
//                    data.portName = json.getString("name");
//                    data.cityId = json.getString("cityId");
//                    ArrayList<AirportData> ports;
//                    if(!portMap.containsKey(data.cityId)){
//                        ports = new ArrayList<AirportData>();
//                        portMap.put(data.cityId, ports);
//
//                    }
//                    ports = portMap.get(data.cityId);
//                    ports.add(data);
//                    //tempDatas[i] = data;
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                //tempDatas = null;
//            }
//
//
//            is.close();
//            br.close();
//            isr.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        try {
//            InputStream is = DemoApplication.applicationContext.getAssets().open("cities_server.txt");
//            InputStreamReader isr=new InputStreamReader(is, "UTF-8");
//            BufferedReader br = new BufferedReader(isr);
//            char []buffer = new char[is.available()];
//            isr.read(buffer);
//            String str = String.valueOf(buffer);
//
//            try {
//                JSONArray array = new JSONArray(str);
//                for(int i=0; i<array.length(); i++)
//                {
//                    JSONObject json = array.getJSONObject(i);
//                    if(null!=portMap.get(json.getString("id"))){
//                        JSONArray jsonArray = new JSONArray();
//                        for(AirportData port:portMap.get(json.getString("id"))){
//                            jsonArray.put(AirportData.toJSON(port));
//                        }
//                        json.put("airports", jsonArray);
//                    }
//                }
//
//                FileOutputStream fos = new FileOutputStream("mnt/sdcard/new_cities.txt");
//                fos.write(array.toString().getBytes());
//                fos.close();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            is.close();
//            br.close();
//            isr.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        try {
            mCityList.clear();
            mCityKeyMap.clear();
            mCityKeyList.clear();
            InputStream is = DemoApplication.applicationContext.getAssets().open("new_cities.txt");
            InputStreamReader isr=new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            char []buffer = new char[is.available()];
            isr.read(buffer);
            String str = String.valueOf(buffer);

            try {
                JSONArray array = new JSONArray(str);
                for(int i=0; i<array.length(); i++)
                {
                    JSONObject json = array.getJSONObject(i);
                    //convert JSONObject to CityData
                    CityData data = CityData.fromJSON(json);
                    if(data.airportList.size()>0)
                    {
                        mCityList.add(data);
                        mCityKeyList.add(data.cityName.toLowerCase());
                        //mCityKeyMap.append(mCityKeyMap.size(), i);
                        //mCityKeyList.add(data.cityCode);

                    }

                }
//                for(){
//
//                }
//                mCityTrie.build(mCityKeyList);

            } catch (JSONException e) {
                e.printStackTrace();
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
            Iterator<CityData> iter = cityDataLinkedList.iterator();
            while (iter.hasNext()){
                if(iter.next().cityName.equals(cityData.cityName)){
                    return;
                }
            }
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
