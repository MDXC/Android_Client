package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.IAccountActivityMediator;

public class AccountActivity extends Activity implements IAccountActivityMediator,OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentViews();
	}

	
	private void setContentViews() {
		setContentView(R.layout.activity_account);
		addTravelersView();
		addContactView();
		addAccountView();
	}


	
	public void addTravelersView() {
		// TODO Auto-generated method stub
		TextView travelersView = (TextView)findViewById(R.id.travelers_info);
		travelersView.setOnClickListener(this);
	}


	
	public void addContactView() {
		// TODO Auto-generated method stub
		TextView contactView = (TextView)findViewById(R.id.contact_info);
		contactView.setOnClickListener(this);
	}


	
	public void addAccountView() {
		// TODO Auto-generated method stub
		TextView acountView = (TextView)findViewById(R.id.acount_info);
		acountView.setText(UserManager.getInstance().getCachedUsername());
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch(v.getId()) {
		case R.id.travelers_info:
			intent.setClass(this, TravelersListActivity.class);
			startActivity(intent);
			break;
		case R.id.contact_info:
			intent.setClass(this, ContactInfoActivity.class);
			startActivity(intent);
			break;
		}
	}
}
