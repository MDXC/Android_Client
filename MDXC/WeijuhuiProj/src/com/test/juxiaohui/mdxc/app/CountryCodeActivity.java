package com.test.juxiaohui.mdxc.app;

import java.util.ArrayList;

import android.content.Context;
import com.test.juxiaohui.mdxc.data.CountryCode;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.adapter.ControlCodeListAdapter;
import com.test.juxiaohui.mdxc.manager.CountryManager;
import com.test.juxiaohui.mdxc.mediator.IControlSearchActivityMediator;
import com.test.juxiaohui.widget.Sidebar;

public class CountryCodeActivity extends Activity implements IControlSearchActivityMediator{

	public static void startActivity(Activity activity)
	{
		activity.startActivityForResult(new Intent(activity, CountryCodeActivity.class), 0);
	}
	
	private StickyListHeadersListView mControlListView;
	private RelativeLayout mSearchView;
	private EditText mEdtControl;
	private ImageView mImgClear;
	private ControlCodeListAdapter mControlCodeListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}
	
	private void initData(){
		CountryManager.getInstance();
	}
	
	private void initView()
	{
		this.setContentView(R.layout.activity_control);
		addSearchView();
		addControlList();
	}

	@Override
	public void addSearchView() {
		// TODO Auto-generated method stub
		mSearchView = (RelativeLayout) findViewById(R.id.view_search_bar_control);
		mEdtControl = (EditText) mSearchView.findViewById(R.id.et_keyword);
		mEdtControl.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				mControlCodeListAdapter.setFilter(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		mImgClear = (ImageView) mSearchView.findViewById(R.id.iv_clear);
	}

	@Override
	public void addControlList() {
		// TODO Auto-generated method stub
		mControlListView = (StickyListHeadersListView) findViewById(R.id.slv_content_control);
		Sidebar sidebar = (Sidebar)findViewById(R.id.sidebar_control);
		sidebar.setListView(mControlListView.getWrappedList());
		mControlListView.getWrappedList().setFastScrollEnabled(true);
		mControlCodeListAdapter = new ControlCodeListAdapter(getNearbyPort(), getLastSearchControl(), getHostControl(), this,mControlListView);
		mControlListView.setAdapter(mControlCodeListAdapter);
		mControlListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("control", mControlCodeListAdapter.getDataByPosition(arg2));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public void realtimeSearch(String condition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<CountryCode> getNearbyPort() {
		// TODO Auto-generated method stub
		ArrayList<CountryCode> mNearbyPort = new ArrayList<CountryCode>();
		return mNearbyPort;
	}

	@Override
	public ArrayList<CountryCode> getLastSearchControl() {
		// TODO Auto-generated method stub
		ArrayList<CountryCode> mLastSearchControl = new ArrayList<CountryCode>();
		return mLastSearchControl;
	}

	@Override
	public ArrayList<CountryCode> getHostControl() {
		// TODO Auto-generated method stub
		ArrayList<CountryCode> mHostControl = new ArrayList<CountryCode>();
		return mHostControl;
	}

	@Override
	public ArrayList<CountryCode> getSearchResult(String condition) {
		// TODO Auto-generated method stub
		return null;
	}
}
