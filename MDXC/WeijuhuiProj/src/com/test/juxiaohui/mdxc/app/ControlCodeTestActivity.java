package com.test.juxiaohui.mdxc.app;

import com.test.juxiaohui.mdxc.data.ControlCode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ControlCodeTestActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	startActivityForResult(new Intent(this,ControlCodeActivity.class), 0);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	if (resultCode == 0) {
    		ControlCode control = (ControlCode)data.getSerializableExtra("control");
    		Log.d("ControlCode", "ControlEngName  = "+control.mControlEngName);
    		Log.d("ControlCode", "ControlChinaName  = "+control.mControlChinaName);
    		Log.d("ControlCode", "ControlshortName  = "+control.mControlshortName);
    		Log.d("ControlCode", "ControlCode  = "+control.mControlCode);
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
}
