package com.test.juxiaohui.mdxc.app;

import com.test.juxiaohui.mdxc.data.CountryCode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CountryCodeTestActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	startActivityForResult(new Intent(this,CountryCodeActivity.class), 0);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	if (resultCode == 0) {
    		CountryCode control = (CountryCode)data.getSerializableExtra("control");
//    		Log.d("CountryCode", "ControlEngName  = "+control.mControlEngName);
//    		Log.d("CountryCode", "ControlChinaName  = "+control.mControlChinaName);
//    		Log.d("CountryCode", "ControlshortName  = "+control.mControlshortName);
//    		Log.d("CountryCode", "CountryCode  = "+control.mControlCode);
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
}
