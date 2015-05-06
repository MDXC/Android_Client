package com.test.juxiaohui.mdxc.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.mdxc.data.ControlCode;

public class ControlManager {

	    private ArrayList<ControlCode> mControlCode = new ArrayList<ControlCode>();
	    private static ControlManager mInstance = null;
	    public static ControlManager getInstance()
	    {
	        if(null == mInstance)
	        {
	            mInstance = new ControlManager();

	        }
	        return mInstance;
	    }
	    
	    
	    private ControlManager () {
	    	createFromFile();
	    }

	    public ArrayList<ControlCode> getSearchResult(String condition) {

	        if(condition==null||condition.length()==0)
	        {
	            return mControlCode;
	        }
	        else
	        {
	            ArrayList<ControlCode> results = (ArrayList<ControlCode>) mControlCode.clone();
	            ArrayList<ControlCode> temp = new ArrayList<ControlCode>();
	            for(int i=0; i<condition.length(); i++)
	            {
	                for(int j=0; j<results.size(); j++)
	                {
	                    if((results.get(j).mControlCode.length()>i
	                    		&&(results.get(j).mControlCode.contains(condition)))
	                    		||(results.get(j).mControlEngName.length()>i && results.get(j).mControlEngName.toLowerCase().contains(condition.toLowerCase()))
	                    		||(results.get(j).mControlChinaName.length()>i && results.get(j).mControlChinaName.contains(condition))
                                )
	                    {
	                        temp.add(results.get(j));
	                    }
	                }
	                results = (ArrayList<ControlCode>) temp.clone();
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
		            ControlCode mControl = new ControlCode();
		            mControl.mControlEngName = arrs[0].trim();
		            mControl.mControlChinaName = arrs[1].trim();
		            mControl.mControlshortName = arrs[2].trim();
		            mControl.mControlCode = arrs[3].trim().replace("00", "+");
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
