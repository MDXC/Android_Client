package com.test.juxiaohui.mdxc.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.mdxc.data.CountryCode;

public class CountryManager {

	    private ArrayList<CountryCode> mControlCode = new ArrayList<CountryCode>();
	    private static CountryManager mInstance = null;
	    public static CountryManager getInstance()
	    {
	        if(null == mInstance)
	        {
	            mInstance = new CountryManager();

	        }
	        return mInstance;
	    }
	    
	    
	    private CountryManager() {
	    	createFromFile();
	    }

	    public ArrayList<CountryCode> getSearchResult(String condition) {

	        if(condition==null||condition.length()==0)
	        {
	            return mControlCode;
	        }
	        else
	        {
	            ArrayList<CountryCode> results = (ArrayList<CountryCode>) mControlCode.clone();
	            ArrayList<CountryCode> temp = new ArrayList<CountryCode>();
	            for(int i=0; i<condition.length(); i++)
	            {
	                for(int j=0; j<results.size(); j++)
	                {
	                    if((results.get(j).mCode.length()>i
	                    		&&(results.get(j).mCode.contains(condition)))
	                    		||(results.get(j).mEngName.length()>i && results.get(j).mEngName.toLowerCase().contains(condition.toLowerCase()))
	                    		||(results.get(j).mChinaName.length()>i && results.get(j).mChinaName.contains(condition))
                                )
	                    {
	                        temp.add(results.get(j));
	                    }
	                }
	                results = (ArrayList<CountryCode>) temp.clone();
	                temp.clear();
	            }
	            return results;
	        }
	    }

		private void createFromFile()
		{	
			
			try {
				InputStream is = DemoApplication.applicationContext.getAssets().open("controls.txt");
		        InputStreamReader isr=new InputStreamReader(is, "UTF-8");
		        BufferedReader br = new BufferedReader(isr);
		        
		        String line="";
		        String[] arrs=null;
		        while ((line=br.readLine())!=null) {
		            arrs=line.split(",");
		            CountryCode mControl = new CountryCode("","","","");
		            mControl.mEngName = arrs[0].trim();
		            mControl.mChinaName = arrs[1].trim();
		            mControl.mShortName = arrs[2].trim();
		            mControl.mCode = arrs[3].trim().replace("00", "+");
		            mControlCode.add(mControl);
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
