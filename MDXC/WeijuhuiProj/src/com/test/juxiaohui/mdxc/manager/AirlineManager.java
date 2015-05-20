package com.test.juxiaohui.mdxc.manager;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.mdxc.data.AirlineData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yihao on 15/5/19.
 */
public class AirlineManager {

    private static AirlineManager mInstance = null;
    private List<AirlineData> mAirlineList = new ArrayList<AirlineData>();
    private HashMap<String, AirlineData> mAirlineMap = new HashMap<String, AirlineData>();
    public static AirlineManager getInstance(){
        if(mInstance == null){
            mInstance = new AirlineManager();
        }
        return mInstance;
    }

    private AirlineManager(){
        readFromCache();
    }

    private void readFromCache(){
        try {
            InputStream is = DemoApplication.applicationContext.getAssets().open("airlines.txt");
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
                    AirlineData airlineData = AirlineData.fromJSON(json);
                    mAirlineList.add(airlineData);
                    mAirlineMap.put(airlineData.mCode, airlineData);
                }

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

    public AirlineData getAirlineByCode(String code){
        if(mAirlineMap.containsKey(code)){
            return mAirlineMap.get(code);
        }
        else {
            return AirlineData.NULL;
        }
    }


}
