package com.test.juxiaohui.mdxc.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.common.dal.IUtilServer;
import com.test.juxiaohui.mdxc.data.CityData;
import com.test.juxiaohui.mdxc.server.UtilServer;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;

/**
 * Created by yihao on 15/5/9.
 */
public class  UtilManager {
    IUtilServer mServer;
    private static UtilManager mInstance = null;
    private static final String PREF_CURRENCY = "pref_currency";
    private static final String PREF_CONTACT = "pref_contact";
    public static UtilManager getInstance(){
        if(mInstance == null){
            mInstance = new UtilManager();
        }
        return mInstance;
    }

    private UtilManager()
    {
        mServer = new UtilServer();


    }

    /**
     * 计算汇率
     * @param srcCurrency 源货币
     * @param destCurrency 目标货币
     * @return
     */
    public float getExchangeRate(String srcCurrency, String destCurrency){
        return mServer.getExchangeRate(srcCurrency, destCurrency);
    }

    /**
     * 设置货币
     * @param currency
     */
    public void setCurrency(String currency)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DemoApplication.applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_CURRENCY, currency).commit();
    }

    /**
     * 返回货币
     * @return
     */
    public String getCurrency(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DemoApplication.applicationContext);
        return preferences.getString(PREF_CURRENCY, "USD");
    }
    
    
    
    public void setContactIndex(String index){
    	 SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DemoApplication.applicationContext);
    	 preferences.edit().putString(PREF_CONTACT, index).commit();
    }
    
    public String getContctIndex(){
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DemoApplication.applicationContext);
    	return preferences.getString(PREF_CONTACT, "0");
    }
}
